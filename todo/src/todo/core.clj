(ns todo.core
    (:require [clojure.string :as str]
              [clojure.java.io :as io])
    (:use clojure.tools.cli)
    (:gen-class)
    ) 

(defn read-file
    "Read in a file as a sequence of lines"
    [file-name]
    (with-open
        [rdr (io/reader file-name)]
        (doall (line-seq rdr))))

(defn process-file 
     "Read in a file and parse it into a todo-list"
     [line-func file-name]
     (map-indexed line-func (read-file file-name)))

(defn trim-split
    [line]
    (map str/trim (str/split line #"\t")))

(defn is-done?
    "Return true if the todo is done, false otherwise"
    [todo]
    (= (:is-done todo) true))

(defn has-id?
    "Return true if the id matches this todo item"
    [todo id]
    (= (:id todo) id))

(defn to-todo
    [line-no line]
    (let [line (trim-split line)
          is-done (first line)
          task (str/join (rest line))]
        {:is-done (= is-done "X") :task task :id (+ line-no 1)}))

(defn list-todos
    [todos]
    (if (empty? todos)
        (println "Your list is empty!")
        (doseq [item todos]
            (println 
                (format "%d. %s %s" (:id item) (if (is-done? item) "[X]" "[ ]") (:task item))
                ))))

(defn write-todos
    [todos file-name]
    (with-open
        [out (io/writer file-name)]
        (doseq 
            [item todos]
            (.write out 
                (format "%s\t%s\n" (if (is-done? item) "X" "N") (:task item))
            )))
    todos)

(defn create
    [todos task]
    (cons {:is-done false :task task :id (+ (count todos) 1)} todos))

(defn mark-done
    [todos id]
    (map
        #(if (has-id? % id)
            (assoc % :is-done true)
            %) todos))

(defn run-s-command
    [todos task file-name]
    (let [k (first task), v (last task)]
        (cond
            (= v nil) todos
            (= k :add) (write-todos (create todos v) file-name)
            (= k :check) (write-todos (mark-done todos v) file-name) 
            :default todos
        )))

(defn run-command
    "Run the commands passed on the cli"
    [todos options file-name]
    (if (empty? options) 
        todos
        (recur (run-s-command todos (first options) file-name) (rest options) file-name)))

(defn run-app
    "Run the specified commands in the options and print the list"
    [todos options file]
    (list-todos (run-command todos options file)))

(defn get-default-file
    "Return the default todo file"
    []
    (str/join (list (System/getProperty "user.home") "/.todos")))

(defn -main [& args]
    ;(write-todos (mark-done (process-file to-todo "/tmp/blah") 1) "/tmp/out"))
    (let
        [[options args banner]
            (cli args
                ["-f" "--file" "The file to use" :default (get-default-file)]
                ["-h" "--help" "Print this usage information" :default false :flag true]
                ["-a" "--add" "Create a new item todo"]
                ["-c" "--check" "Check an item as done" :parse-fn #(Integer. %)]
                ["-l" "--list" "Show all the todo items" :default false]),
         file (:file options)
        ]

        ; First check if we need to print usage info
        (when (:help options)
                (println banner)
                (System/exit 0))

        ; Otherwise run the command
        (run-app (process-file to-todo file) options file)))

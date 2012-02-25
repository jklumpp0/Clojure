(ns todo.core
    (:require [clojure.string :as str])
    (:use [clojure.tools.cli]
          [todo.io])
    (:gen-class)
    ) 

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
    "Write out the todo items to a file, but first apply the format for parsing"
    [todos file-name]
    (write-file file-name 
        (map #(format "%s\t%s\n" (if (is-done? %) "X" "N") (:task %)) todos))
    todos)

(defn create
    [todos task]
    (sort-by :id
        (cons {:is-done false :task task :id (+ (count todos) 1)} todos)))

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

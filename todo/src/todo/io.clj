(ns todo.io
    (:require [clojure.string :as str]
              [clojure.java.io :as io])
    (:import java.io.FileNotFoundException)
    (:gen-class)
    ) 

(defn ^{:private true} create-file
    "Create a non-existent file"
    [file-name]
    (try 
        (with-open [out (io/writer file-name)])
        (catch FileNotFoundException e
            (println "Could not create non-existent file" file-name)
            (System/exit 0))))

(defn open-file
    "Open or create a file"
    [file-name]
    (try 
        (if (.exists (io/file file-name))
            (try
                (io/reader file-name)
                (catch FileNotFoundException e
                    (do 
                        (println "Could not open the file" file-name)
                        (System/exit 0)
                )))
            (do
                (create-file file-name)
                (io/reader file-name)))))

(defn read-file
    "Read in a file as a sequence of lines"
    [file-name]
    (with-open
        [rdr (open-file file-name)]
        (doall (line-seq rdr))))

(defn process-file 
     "Read in a file and parse it into a todo-list"
     [line-func file-name]
     (map-indexed line-func (read-file file-name)))

(defn write-file
    "Write a sequence to a file"
    [file-name items]
    (with-open [out (io/writer file-name)]
        (doseq
            [item items]
            (.write out item))))

(ns todo.io
    (:require [clojure.string :as str]
              [clojure.java.io :as io])
    (:import java.io.FileNotFoundException)
    (:gen-class)
    ) 

(defn open-file
    "Open or create a file"
    [file-name]
    (try 
        (io/reader file-name)
        (catch FileNotFoundException e
            (do (with-open [out (io/writer file-name)]) (io/reader file-name)))))

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

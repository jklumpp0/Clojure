(ns todo.core
    (:require [clojure.string :as str])
    (:import (java.io BufferedReader)))

(defn process-file 
     "Read in a file and parse it into a todo-list"
     [line-func file-name]
     (map line-func (line-seq (clojure.java.io/reader file-name))))

(defn trim-split
    [line]
    (map str/trim (str/split line #"\t")))

(defn to-todo
    [line]
    (let [line (trim-split line)]
        (if (= (first line) "Hello")
            "hi!"
            "bye")))

(defn -main [& args]
    (println (process-file to-todo "/tmp/blah")))
    
    

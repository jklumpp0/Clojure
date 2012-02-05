; Data types and operations
; REGEX
(def hard-pattern (re-pattern "([0-9]+)[-$]"))

(def pattern #"([0-9]+)-?")

(def phone "012-345-7689")

(def matcher (re-matcher pattern phone))

(println (re-matches pattern phone))

(println (re-find matcher))
(println (re-find matcher))
(println (re-seq pattern phone))

; Keywords
(println "Check if string and keyword are equal: " (= :hi "hi"))
(println "Check if using keyword to convert string makes them equal: " (= :hi (keyword "hi")))



; Lists
(def my-list '(1 2 3 5 8 13))
(println "The front of the list is: " (peek my-list))
(println "The new list is: " (pop my-list))

; Vectors
(def my-vec (vec my-list))
(def my-vec (assoc my-vec 3 15))
(println "After associating 15 with spot 3" my-vec)

; Maps
(def my-map {:color "blue", :hex "#0000FF"})
(println "The color: " (my-map :color))
(println "Non-existant key: " (my-map :unknown))
(println "Non-existant key - nil?: " (nil? (my-map :unknown)))

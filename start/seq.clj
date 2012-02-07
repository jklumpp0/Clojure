; Sequences
; A sequence of ints
(defn make-ints 
    [max]
    (loop [acc nil n max]
        (if (zero? n)
            acc
            (recur (cons n acc) (dec n))
        )
    )
)

(println (make-ints 10))


; Lazy sequences
(defn squares 
    ([] (squares 1))
    ([val] (lazy-seq (cons (* val val) (squares (inc val)))))
    )

(println (take 10 (squares)))

; Sequence construction
(println (seq {:a 1 :b 2 :c 3}))
(println (take 10 (repeatedly #(rand-int 10))))
(println (distinct '(1 3 7 2 2 3 5 8)))
(println "Not a person:" (filter 
    #(not (string? %)) '("John" 3 4 7 "Doe" \a)))
(println (interpose \. '("java" "io" "File")))
(println "Apply!" (apply * (range 1 10)))

(def my-list (map (fn [val] (do (println val) val)) '(1 2 3 4 5)))
(def my-list-result (doall my-list))
(println my-list-result)

(def reverse-map (reduce #(assoc % %2 (reverse %2)) {} '("John" "Doe")))
(println "Mapping each key to its reverse:" reverse-map)
(println "John's reverse:" (reverse-map "John"))

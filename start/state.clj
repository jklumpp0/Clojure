; Refs
(def john (ref 1000))
(def jane (ref 1000))
(def bank (ref 10000))

(defn transfer
    "Transfer from one account to another"
    [from-acct to-acct amount]
    (dosync
        (alter to-acct + amount)
        (alter from-acct - amount)
        )
)

(transfer john jane 500)
(println "John has " @john)
(println "Jane has " @jane)

; Atoms
(def result (atom 3))
(println "De-ref atom: " @result)
(println "Result of swap!:" (swap! result + 7))
(println "Now: " @result)
(println "Result of reset!" (reset! result (+ 2 3)))
(println "Now: " @result)


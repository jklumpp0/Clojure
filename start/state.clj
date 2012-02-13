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

; Agents
(def secret-agent (agent 100))
(println "Value of agent is:" @secret-agent)
(send secret-agent + 20)
(println "He now has the value:" @secret-agent)

; Create another agent that continues
(def test-agent (agent 30))
(set-error-mode! test-agent :continue)
(set-error-handler! test-agent (fn [agt err] (println "The exception was:" err)))
(send test-agent (fn [arg] (throw (new RuntimeException "My Agent Exception"))))
(println "The agent is: " @test-agent)
(send test-agent + 30)
(println "The agent is: " @test-agent)

; Shutdown the agents
(shutdown-agents)

; Print when an identity changes
(defn print-change [key identity old-val new-val]
    (println "The value of" identity "changed from" old-val "to" new-val))
(def name (ref "John Doe"))
(add-watch name :watch0 print-change)
(dosync
    (ref-set name "Jane Doe"))
(remove-watch name :watch0)
(dosync
    (ref-set name "John Doe"))

(let 
    [x 10 y 15]
    (println (+ x y)))

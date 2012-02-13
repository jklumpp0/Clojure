(ns jk)

(defn fac 
    "Caclulate the factorial"
    ([n] (fac n 1))
    ([n total] 
        (if (< n 2) total
                    (fac (- n 1) (* n total))
        )
    )
)

(defn recur-fac
    "Calculate the factorial using tail recursion"
    ([n] (fac n 1))
    ([n total]
        (if (n < 2) total
                    (recur (- n 1) (* n total))
        )
    )
)

; A curried function
(def double (partial * 2))

; Compose the curry with a print
(def print-double (comp (partial println "Doubling the input is: ") double))

; Print the results of simple factorial
(println (fac 3))
(println (fac 4))

; Print results using tail-recursion factorial
(println (recur-fac 3))
(println (recur-fac 4))

; Print the doubling of 4 and 6
(print-double 4)
(print-double 6)


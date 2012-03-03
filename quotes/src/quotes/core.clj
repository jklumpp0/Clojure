(ns quotes.core
    (require [clojure.java.io :as io]
             [net.cgrand.enlive-html :as html]
             [clojure.string :as s])
    (import [java.net URI])
    (:gen-class))

(defn- get-site
    [site]
        (html/xml-resource site))

(def ^{:private true} yahoo-api "//query.yahooapis.com/v1/public/yql?q=")

(defn- get-quote-url 
    [ & quotes]
    (let 
        [desired (s/join "," (map #(format "\"%s\"" %) (flatten quotes)))
        query (format "select symbol,Ask from yahoo.finance.quotes where symbol in (%s)&env=store://datatables.org/alltableswithkeys" desired)]
    (URI. "http" (str yahoo-api query) nil)))

(defn- make-sym-map
    [quote]
    (let [content (first (:content quote))]
        {:symbol (-> quote (:attrs) (:symbol)) :ask (-> content :content first)}
        ))

(defn get-syms
    [& quotes]
    (let
        [content (get-site (get-quote-url quotes))]
        (merge (map make-sym-map (html/select content [:quote])))))

(defn -main
    [& args]
        (println (get-syms args)))

(ns jeopardy.server.http-endpoints
  (:require [clojure.pprint :refer [pprint]]
            [clojure.data.json :as json]
            [org.httpkit.server :as http-server]))

(defn get-headers
  []
  {"Content-Type" "application/json"
   "Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Methods" "*"})


(defn http-handler! [request]
  (pprint request)

  (let [uri (:uri request)
        body (when (:body request)
               (slurp (:body request)))]

    (cond (= uri "/to-upper-case")
          {:status  200
           :headers (get-headers)
           :body    "{\"a\": 39}"}

          :else
          {:status  200
           :headers (get-headers)
           :body    "{\"a\": 42}"})))


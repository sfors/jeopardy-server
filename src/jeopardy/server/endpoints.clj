(ns jeopardy.server.endpoints
  (:require [clojure.pprint :refer [pprint]]
            [clojure.data.json :as json]))

(defn handler! [request]
  (pprint request)

  (let [uri (:uri request)
        body (when (:body request)
               (slurp (:body request)))]

    (println (json/read-str body :key-fn keyword))

    (cond (= uri "/to-upper-case")
          {:status  200
           :headers {"Content-Type" "application/json"}
           :body    "{\"a\": 39}"}

          :else
          {:status  200
           :headers {"Content-Type" "application/json"}
           :body    "{\"a\": 42}"}))
  )

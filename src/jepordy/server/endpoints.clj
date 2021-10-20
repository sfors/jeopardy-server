(ns jepordy.server.endpoints
  (:require [clojure.pprint :refer [pprint]]))

;json/write-string

(defn handler! [request]
  (pprint request)

  (let [uri (:uri request)
        body (when (:body request)
               (slurp (:body request)))]

    ; headern det enda som är dynamiskt, lägga till key-values där
    (cond (= (:uri request) "/to-upper-case")
          {:status 200
           :headers {"Content-Type" "application/json"}
           :body "{\"a\": 42}"}
          :else
          {:status 200
           :headers {"Content-Type" "application/json"}
           :body "{\"a\": 39}"}
          )
    )


  )
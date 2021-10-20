(ns jepordy.server.endpoints
  (:require [clojure.pprint :refer [pprint]]))

(defn handler! [request]
  (pprint request)

  (let [uri (:uri request)
        body (when (:body request)
               (slurp (:body request)))]

    (println body)

    (cond (= uri "/to-upper-case")

          {:status  200
           :headers {"Content-Type" "text/html"}
           :body    "<h1>Upper case</h1>"}


          :else
          {:status  200
           :headers {"Content-Type" "text/html"}
           :body    "<h1>Not upper case</h1>"})

    )
  )
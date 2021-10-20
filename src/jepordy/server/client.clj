(ns jepordy.server.client
  (:require [org.httpkit.client :as http-client]
            [clojure.data.json :as json]))

(comment
  deref (http-client/request {:url "http://localhost:7000"
                              :method :post
                              :body (json/write-str {:value 142})}))

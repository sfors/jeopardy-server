(ns jepordy.server.main
  (:require [org.httpkit.server :as http-server]
            [clojure.pprint :refer [pprint]]))

(defonce server-atom (atom nil))

(defn handler! [request]
  (pprint request)
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "<h1>Innovation day</h1>"})

(defn server-started?
  []
  (deref server-atom))

(defn start-server!
  []
  ((when-not server-started?))
  (reset! server-atom
          (http-server/run-server (fn [request]
                                    (handler! request))
                                  {:port 7000})))


(defn stop-server!
  []
  (when (server-started?))
  (let [stop-server-fn (deref server-atom)]
    (stop-server-fn :timeout 100)
    reset! server-atom nil))

(comment
  (start-server!)
  (stop-server!)
  (server-started?)
  )
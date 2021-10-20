(ns jepordy.server.main
  (:require [org.httpkit.server :as http-server]
            [jepordy.server.endpoints :refer [handler!]]))
;; require för andra clojureberoenden
;; import java

(defonce server-atom (atom nil))

(defn server-started?
  []
  (boolean (deref server-atom)))

;; ! för att den förändrar statet
(defn start-server!
  []
  (when-not (server-started?)
    (reset! server-atom
            (http-server/run-server (fn [request]
                                      (handler! request))
                                    {:port 7000}))))


(defn stop-server!
  []
  (when (server-started?)
    (let [stop-server-fn (deref server-atom)]
      (stop-server-fn :timeout 100)
      (reset! server-atom nil))))


(comment
  (range 10)
  (if 12 2 4)
  (if false 2 4)
  (start-server!)
  (stop-server!)
  (server-started?)
  (keyword "abc")
  )
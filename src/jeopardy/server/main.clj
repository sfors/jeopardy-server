(ns jeopardy.server.main
  (:require [org.httpkit.server :as http-server]
            [jeopardy.server.handler :refer [handler!]]))

(defonce server-atom (atom nil))

(defn server-started?
  []
  (boolean (deref server-atom)))

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
  (start-server!)
  (stop-server!)
  (server-started?)
  )


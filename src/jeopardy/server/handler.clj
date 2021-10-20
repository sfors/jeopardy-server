(ns jeopardy.server.handler
  (:require [clojure.pprint :refer [pprint]]
            [jeopardy.server.http-endpoints :refer [http-handler!]]
            [jeopardy.server.websocket-endpoint :refer [websocket-handler!]]))

(defn handler!
  [request]
  (pprint request)
  (if (:websocket? request)
    (websocket-handler! request)
    (http-handler! request)))

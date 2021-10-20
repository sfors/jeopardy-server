(ns jeopardy.server.endpoints
  (:require [clojure.pprint :refer [pprint]]
            [clojure.data.json :as json]
            [org.httpkit.server :as http-server]))

(defonce user->channels-atom (atom {}))

(defonce state-atom (atom {:users {}}))

(comment
  (deref state-atom)
  (deref user->channels-atom)
  )

(defn new-connection
  [channel name]
  (println "New connection:" name)
  (swap! user->channels-atom (fn [user->channel]
                               (assoc user->channel name channel)))
  (swap! state-atom assoc-in [:users name] {})
  (let [user->channels (deref user->channels-atom)]
    (doseq [channel (vals user->channels)]
      (http-server/send! channel
                         (str {:data {:event :new-connection
                                      :names (keys user->channels)}})))))


(defn receive-new-message
  [name text]
  (doseq [channel (:channels (deref channels-atom))]
    (http-server/send! channel (str "Some message got to the server"))))


(defn receive-data-from-channel
  [channel data]
  (println "Received data from client")
  (let [{action :action name :name text :text} (read-string data)]
    (println "Action" action "Name" name "Text" text)
    (condp = action
      :connect (new-connection channel name)
      :new-message (receive-new-message name text))))

(defn websocket-handler!
  [request]
  (println "Websocket request received!")
  (http-server/with-channel
    request channel
    (http-server/on-close channel
                          (fn [status]
                            (println "channel closed: " status)))
    (http-server/on-receive channel
                            (fn [data]
                              (receive-data-from-channel channel data)))))

(defn http-handler! [request]
  (println "HTTP request received!")

(defn get-headers
  []
  {"Content-Type" "application/json"
   "Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Methods" "*"})


(defn handler! [request]
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
           :body    "{\"a\": 42}"}))
  )

(defn handler!
  [request]
  (pprint request)
  (if (:websocket? request)
    (websocket-handler! request)
    (http-handler! request)))
(ns jeopardy.server.websocket-endpoint
  (:require [org.httpkit.server :as http-server]
            [jeopardy.server.state :as state]))

(defonce user->channels-atom (atom {}))

(comment
  (deref user->channels-atom)
  )

(defn get-channels
  []
  (vals (deref user->channels-atom)))

(defn get-user
  [channel]
  (get (clojure.set/map-invert (deref user->channels-atom))
       channel))

(defn new-connection
  [channel name]
  (println "New connection:" name)
  (swap! user->channels-atom (fn [user->channel]
                               (assoc user->channel name channel)))
  (state/add-user! name)
  (let [user->channels (deref user->channels-atom)]
    (doseq [channel (vals user->channels)]
      (http-server/send! channel
                         (str {:data {:event :new-connection
                                      :names (keys user->channels)}})))))


(defn receive-new-message
  [name text]
  (doseq [channel (get-channels)]
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
                            (println "channel closed: " status)
                            (state/remove-user! (get-user channel))))
    (http-server/on-receive channel
                            (fn [data]
                              (receive-data-from-channel channel data)))))


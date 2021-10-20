(ns jeopardy.server.websocket-endpoint
  (:require [org.httpkit.server :as http-server]
            [jeopardy.server.state :as state]))

(defonce channels->username-atom (atom {}))

(comment
  (deref channels->username-atom)
  )

(defn get-channels
  []
  (vals (deref channels->username-atom)))

(defn get-username
  [channel]
  (get (deref channels->username-atom)
       channel))

(defn new-connection
  [channel {username :username}]
  (println "New connection:" username)
  (swap! channels->username-atom assoc channel username)
  (state/add-user! username)
  (let [channels->username (deref channels->username-atom)]
    (doseq [channel (keys channels->username)]
      (http-server/send! channel
                         (str {:data {:event :new-connection
                                      :names (vals channels->username)}})))))


(defn received-message
  [username data]
  (state/received-message! username data)
  (doseq [channel (get-channels)]
    (http-server/send! channel (str "Some message got to the server"))))


(defn receive-data-from-channel
  [channel message]
  (println "Received data from client" message)
  (let [data (read-string message)]
    (if-let [username (get-username channel)]
      (received-message username data)
      (new-connection channel data))))

(defn websocket-handler!
  [request]
  (println "Websocket request received!")
  (http-server/with-channel
    request channel
    (do                                                     ; body
      (http-server/on-close channel
                            (fn [status]
                              (println "channel closed: " status)
                              (state/remove-user! (get-username channel))
                              (swap! channels->username-atom dissoc channel)))
      (http-server/on-receive channel
                              (fn [message]
                                (receive-data-from-channel channel message))))))


(ns jeopardy.server.state)

(defonce state-atom (atom {}))

(defn add-user!
  [username]
  (swap! state-atom assoc-in [:users username] {}))

(defn remove-user!
  [username]
  (swap! state-atom update :users
         (fn [users]
           (dissoc users username))))
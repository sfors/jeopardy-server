(ns jeopardy.server.game)

(defn get-default-cards
  []
  [{:points 100 :answer "Carl gustav" :question "What king?" :flipped? false}
   {:points 200 :answer "Carl gustav" :question "What king?" :flipped? false}
   {:points 300 :answer "Carl gustav" :question "What king?" :flipped? false}]
  )

(defn get-game
  []
  {:board {:categories [{:title "kings" :id 1 :cards (get-default-cards)}
                        {:title "programming" :id 2 :cards (get-default-cards)}
                        {:title "animals" :id 3 :cards (get-default-cards)}]}
   :teams [{:color :red :name "team1" :money 100}
           {:color :blue :name "team2" :money 200}
           {:color :green :name "team3" :money 300}]
   }
  )

(comment
  (get-game))
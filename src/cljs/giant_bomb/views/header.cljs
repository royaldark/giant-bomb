(ns giant-bomb.views.header
  (:require [re-com.core :as re-com]
            [giant-bomb.views.icon :as v-icon]))

(defn header []
  [re-com/h-box
   :gap "1rem"
   :children [[re-com/box
               :child [:strong
                       [v-icon/icon :bomb]
                       [:span {:style {:padding-left "1rem"}} "Giant Bomb"]]
               :size "auto"]]
   :width "100%"
   :class "site-header"])

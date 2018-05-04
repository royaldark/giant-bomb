(ns giant-bomb.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-com.core :as re-com]
            [giant-bomb.api :as api]
            [giant-bomb.subs :as subs]
            [giant-bomb.views.header :as v-header]
            [giant-bomb.views.search-panel :as v-search-panel]))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    ;; Dev hack: kick off immediate API request on load
    (re-frame/dispatch [::giant-bomb.api/search-games "final fantasy"])
    [re-com/v-box
     :height "100%"
     :width "100%"
     :justify :center
     :children [[v-header/header]
                (case @active-panel
                  :home-panel [v-search-panel/search-panel]
                  [:div "404"])]]))

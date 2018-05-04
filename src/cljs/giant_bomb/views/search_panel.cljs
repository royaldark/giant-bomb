(ns giant-bomb.views.search-panel
  (:require  [cljs-time.core :as time]
             [cljs-time.format :as timef]
             [reagent.core :as reagent]
             [re-frame.core :as re-frame]
             [re-com.core :as re-com]
             [giant-bomb.api :as api]
             [giant-bomb.subs :as subs]
             [giant-bomb.views.icon :as v-icon]))

;; Game Search Results

(defn game-title [game]
  (let [release-year (some->> (:original_release_date game)
                              (timef/parse api/date-format)
                              (time/year))]
    [re-com/title
     :level :level2
     :label [:div [:strong (:name game)]
             (when release-year (str " (" release-year ")"))]]))

(defn game-platforms [game]
  (let [platforms (sort (map :abbreviation (:platforms game)))]
    [re-com/h-box
     :children (->> platforms
                    (map (fn [platform]
                           [:div {:class "game-platform"} platform])))]))

(defn game-rental-options []
  [:strong "1 day: $2.99" [:br] "1 week: $5.99"])

(defn game-rental-pane [game]
  (let [expanded (reagent/atom false)]
    (fn []
      [re-com/h-box
       :children [[re-com/button
                   :label [:span [v-icon/icon :gamepad] " Rent"]
                   :on-click #(swap! expanded not)]
                  (when @expanded
                    [re-com/box
                     :child [game-rental-options]])]])))

(defn game-tile [idx game]
  [re-com/h-box
   :size "10rem"
   :gap "1rem"
   :style {:background-color (if (even? idx) "white" "whitesmoke")}
   :children [[re-com/box
               :size "10rem"
               :justify :center
               :child [:img {:src (-> game :image :thumb_url)}]]
              [re-com/v-box
               :children [[game-title game]
                          [game-platforms game]
                          (:deck game)
                          [game-rental-pane game]]]]])

(defn search-results []
  (let [results (re-frame/subscribe [::subs/search-results])]
    [re-com/v-box
     :children (concat
                [[re-com/title
                  :level :level2
                  :label (str (:number_of_total_results @results 0) " Results")]]
                (map-indexed game-tile (:results @results)))]))

;; Game Search Bar

(defn search-games!
  "Triggers a Giant Bomb API request to search games matching the given query
  string."
  [query]
  (re-frame/dispatch [::api/search-games query]))

(defn debounce
  "Simple window.setTimeout()-based debounce implementation. Ensures `f` is
  never invoked unless at least `delay` milliseconds have elapsed since the last
  invocation."
  [f delay]
  (let [timeout-id (atom nil)]
    (fn [& args]
      (when-let [timeout-id @timeout-id]
        (.clearTimeout js/window timeout-id))

      (let [set-timeout (.bind (.-setTimeout js/window) js/window)]
        (reset! timeout-id (apply set-timeout search-games! delay args))))))

(defn search-bar []
  (let [query (reagent/atom "")]
    (fn []
      [re-com/input-text
       :placeholder "Search games..."
       :model query
       :change-on-blur? false
       :on-change (debounce search-games! 200)])))

;; Game Search Panel

(defn search-panel []
  [re-com/v-box
   :gap "1em"
   :class "site-body"
   :children [[search-bar]
              [search-results]]])
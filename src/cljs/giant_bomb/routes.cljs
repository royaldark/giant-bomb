(ns giant-bomb.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:require [accountant.core :as accountant]
            [giant-bomb.events :as events]
            [goog.events :as gevents]
            [goog.history.EventType :as EventType]
            [re-frame.core :as re-frame]
            [secretary.core :as secretary]))

(defn hook-browser-navigation! []
  (accountant/configure-navigation!
   {:nav-handler (fn [path]
                   (println "nav handler:" path)
                   (secretary/dispatch! path))
    :path-exists? (fn [path]
                    (println "path exists?" path)
                    (secretary/locate-route path))}))

(defn app-routes []
  (defroute "/" []
    (re-frame/dispatch [::events/set-active-panel :home-panel]))

  ;; Configure HTML5 History-based routing
  (hook-browser-navigation!)
  (accountant/dispatch-current!))

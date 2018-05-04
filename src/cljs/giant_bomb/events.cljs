(ns giant-bomb.events
  (:require [re-frame.core :as re-frame]
            [giant-bomb.api :as api]
            [giant-bomb.db :as db]
            [day8.re-frame.http-fx]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(api/reg-api-fx
 ::api/search-games
 (fn-traced [cofx [_ query]]
   {:http-xhrio {:method :get
                 :uri "search"
                 :params {:query     query
                          :resources :game}
                 :on-success [::api/search-games-success]
                 :on-failure [::api/search-games-failure]}}))

(re-frame/reg-event-db
 ::api/search-games-success
 (fn-traced [db [_ result]]
   (assoc db ::api/search-games-result result)))

(re-frame/reg-event-db
 ::api/search-games-failure
 (fn-traced [db [_ error]]
   ;; TODO: React to errors
   db))

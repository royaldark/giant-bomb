(ns giant-bomb.subs
  (:require [giant-bomb.api :as api]
            [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::search-results
 (fn [db]
   (::api/search-games-result db)))

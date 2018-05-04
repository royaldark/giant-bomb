(ns giant-bomb.api
  (:require [ajax.core :as ajax]
            [ajax.protocols :refer [Interceptor]]
            [cljs-time.format :as tf]
            [re-frame.core :as re-frame]))

(def date-format
  "A cljs-time formatter which matches the pattern of dates expected/returned by
  the Giant Bomb API"
  ;; Example value: "1987-12-18 00:00:00"
  (tf/formatter "yyyy-MM-dd hh:mm:ss"))

(def base-url
  ;; Giant Bomb API doesn't support CORS, and I didn't want to set up JSONP, so
  ;; I am using a free (and definitely insecure) reverse proxy that simply adds
  ;; CORS headers to proxied requests
  "https://cors-anywhere.herokuapp.com/https://www.giantbomb.com/api")

(defn reg-api-fx
  "Like `re-frame/reg-event-fx`, but tailored for API calls to the Giant Bomb
  API. Automatically sets request/response format and adds API key to request."
  ([id handler]
   (reg-api-fx id nil handler))
  ([id interceptors handler]
   (re-frame/reg-event-fx
    id
    interceptors
    (fn [{:keys [db] :as cofx} event]
      (let [fx (handler cofx event)]
        (-> fx
            (update :http-xhrio merge {:format          (ajax/json-request-format)
                                       :response-format (ajax/json-response-format {:keywords? true})})
            (update-in [:http-xhrio :params] assoc :api_key (:api-key db) :format :json)
            (update-in [:http-xhrio :uri] #(str base-url "/" % "/"))))))))


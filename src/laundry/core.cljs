(ns laundry.core
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponentk defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [laundry.navbar :refer [nav]]))

(enable-console-print!)


;; define your app data so that it doesn't get over-written on reload

(defcomponent app [data owner]
  (render [_]
    (html [:div#container (om/build nav {:location "Cary Hall West" :washer-active true :dryer-active false :machines []} (:nav nav))])))

(om/root app
         {}
         {:target (.getElementById js/document "app")})


(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

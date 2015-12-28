(ns laundry.machines
  (:require [om-tools.core :refer-macros [defcomponent defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [cljs.core.async :refer [put! chan <!]]
            [laundry.api :refer [get-machines]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(defn status-to-color [status]
  (case status
    "In use" "#f44336"
    "End of cycle" "#c5e1a5"
    "Almost Done" "#ffab40"
    "Almost done" "#ffab40"
    "Finished" "#c5e1a5"
    "Out of order" "#9E9E9E"
    "Available" "#4caf50"
    "Not online" "#9E9E9E"
    "Payment in progress" "#f44336"
    "Ready to start" "#c5e1a5"))



(defcomponent machine-card [machine owner]
  (render [_]
    (html [:div.mdl-cell.mdl-cell--4-col
           [:div.demo-card-event.mdl-card.mdl-shadow--2dp {:style {:background (status-to-color (:status machine))}}
            [:div.mdl-card__title
             [:h3.card-content (:name machine)
              [:br]
              (:status machine)
              [:br]
              (:time machine)]]]])))

(defcomponentk machine-grid [[:data machines] state]
  (render [_]
    ;(println machines)
   (html [:div.mdl-grid
          (om/build-all machine-card machines)])))

(ns laundry.navbar
  (:require [om-tools.core :refer-macros [defcomponent defcomponentk]]
            [om-tools.dom :as dom :include-macros true]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [cljs.core.async :refer [put! chan <!]]
            [laundry.api :refer [get-machines]]
            [laundry.machine-display :refer [machine-grid]])
  (:require-macros [cljs.core.async.macros :refer [go]]))



(def hall-names ["Cary Hall West", "Earhart Hall", "Harrison Hall", "Hawkins Hall", "Hillenbrand Hall", "McCutcheon Hall",
                  "Meredith Northwest", "Meredith Southeast", "Owen Hall", "Shreve Hall", "Tarkington Hall", "Third Street Suites", "Wiley Hall",
                  "Windsor - Duhme", "Windsor - Warren"])


(defn toggle-drawer []
  (dommy/toggle-class! (sel1 ".mdl-layout__drawer") "is-visible")
  (dommy/toggle-class! (sel1 ".mdl-layout__obfuscator") "is-visible"))

(defn split-machines [machines type]
  (keep #(if (= (:type %)type)%)machines))

(defcomponentk nav [[:data washer-active dryer-active location machines] state]
  (will-mount [this])
  (did-mount [this]
    (swap! state assoc :washer-active washer-active :dryer-active dryer-active :location location :machines machines)
    (go (swap! state assoc :machines (<! (get-machines location))))
    ;this is done to update the mdl content, see dyanmic content for mdl in docs
    (.upgradeDom js/componentHandler))
  (render [this]
    (html [:div.mdl-layout.mdl-js-layout.mdl-layout--fixed-header.mdl-layout--fixed-tabs
           [:header.mdl-layout__header
            [:div.mdl-layout__header-row
             [:span.mdl-layout-title (:location @state)]]
            [:div.mdl-layout__tab-bar
             (if (= true (:washer-active @state))
              (list [:a.mdl-layout__tab.is-active "Washers"]
                    [:a.mdl-layout__tab {:on-click #(swap! state assoc :washer-active false :dryer-active true)}"Dryers"])
              (list [:a.mdl-layout__tab {:on-click #(swap! state assoc :washer-active true :dryer-active false)}"Washers"]
                    [:a.mdl-layout__tab.is-active "Dryers"]))]]
           [:div.mdl-layout__drawer
            [:span.mdl-layout-title "Halls"]
            [:nav.mdl-navigation (for [hall hall-names]
                                  [:a.mdl-navigation__link{:on-click #(swap! state assoc :location hall
                                                                       (toggle-drawer)
                                                                       (go (swap! state assoc :machines (<! (get-machines hall)))))}hall])]]
           [:main.mdl-layout__content
            (let [washer-class (if (= true (:washer-active @state))"is-active" "") dryer-class (if (= true (:dryer-active @state))"is-active" "")]
              (list
                [:section.mdl-layout__tab-panel#washer-tab {:class washer-class}
                  [:div
                   [:div (om/build machine-grid {:machines (split-machines (:machines @state) "Washer")})]]]

                [:section.mdl-layout__tab-panel#dryer-tab {:class dryer-class}
                  [:div-page.content
                   (om/build machine-grid {:machines (split-machines (:machines @state) "Dryer")})]]))]])))



(enable-console-print!)

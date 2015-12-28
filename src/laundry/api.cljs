(ns laundry.api
  (:require-macros [cljs.core.async.macros :refer [go]])
 (:require [cljs-http.client :as http]
           [cljs.core.async :refer [<! chan ]]))

(def api-base "https://purdue-laundry-api.herokuapp.com/laundry")

(def location-to-api-route {"Cary Hall West" "cary" "Earhart Hall" "earhart" "Harrison Hall" "harrison" "Hawkins Hall" "hawkins" "Hillenbrand Hall" "hillenbrand" "McCutcheon Hall" "mccutcheon"
                            "Meredith Northwest" "meredith_nw" "Meredith Southeast" "meredith_se" "Owen Hall" "owen" "Shreve Hall" "shreve" "Tarkington Hall" "tarkington" "Third Street Suites" "third" "Wiley Hall" "wiley"
                            "Windsor - Duhme" "windsor_duhme" "Windsor - Warren" "windsor_warren"})

(defn get-machines [location]
  (let [url (str api-base "/" (location-to-api-route location))]
   (http/get url {:channel (chan 1 (map :body))})))


(enable-console-print!)

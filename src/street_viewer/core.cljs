(ns street-viewer.core
  (:require [reagent.core :as reagent]))

(enable-console-print!)

(defonce app-state (reagent/atom {:street "" :city ""}))

(def base-url
  "http://maps.googleapis.com/maps/api/streetview?size=600x400&location=")

(defn address-url
  [street city]
  (str street ", " city))

(def api-key
  (.get (js/URLSearchParams.
          js/document.location.search)
        "api-key"))

(defn street-view-url
  "Takes a street and a city and returns a fully formed URL that can be used to
   make a call to the Google Street View API"
  [street city]
  (str base-url (address-url street city) "&key=" api-key))

(defn save-input-state!
  [state-key event]
  (let [input-value (.-value (.-target event))]
    (swap!
      app-state
      assoc state-key input-value)))

(defn input
  "A form component with an on-change listener that updates an atom
   based on user input"
  [state-key]
  [:input {:value (get @app-state state-key)
           :on-change (partial save-input-state! state-key)}])

(defn map-view
  []
  [:div.map-view
   [:p "Street: " [input :street]]
   [:p "City: " [input :city]]
   [:img {:src (street-view-url (@app-state :street) (@app-state :city))}]])

(reagent/render-component [map-view]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  (swap! app-state update-in [:__figwheel_counter] inc))

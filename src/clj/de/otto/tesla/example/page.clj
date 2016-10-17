(ns de.otto.tesla.example.page
  (:require [com.stuartsierra.component :as c]
            [de.otto.tesla.stateful.handler :as handlers]
            [matthiasn.systems-toolbox.switchboard :as sb]
            [matthiasn.systems-toolbox-sente.server :as sente]
            [de.otto.tesla.example.spec]
            [de.otto.tesla.example.store :as st]
            [hiccup.core :as hiccup]))

(defn index-page
  [_put-fn]
  (hiccup/html
    [:html
     {:lang "en"}
     [:head
      [:meta {:name "viewport" :content "width=device-width, minimum-scale=1.0"}]
      [:title "Counter"]
      [:link {:href "/css/example.css" :rel "stylesheet"}]]
     [:body
      [:h1 "Example running in a Tesla microservice"]
      [:div#counter]
      [:script {:src "/js/build/example.js"}]]]))

(defonce switchboard (sb/component :server/switchboard))

(defn restart!
  "Starts or restarts system by asking switchboard to fire up the provided ws-cmp,
   a scheduler component and the ptr component, which handles and counts messages
   about mouse moves."
  [handler {:keys [port ip]}]
  (let [sente-cfg {:index-page-fn index-page
                   :routes-fn     (fn [_put-fn] [(handlers/handler handler)])
                   :relay-types   #{:cnt/inc :cnt/dec :cnt/add :cnt/remove}
                   :port          port
                   :host          ip}]
    (sb/send-mult-cmd
      switchboard
      [[:cmd/init-comp
        #{(sente/cmp-map :server/ws-cmp sente-cfg)
          (st/cmp-map :server/store-cmp)}]
       [:cmd/route {:from :server/ws-cmp :to #{:server/store-cmp}}]
       [:cmd/route {:from :server/store-cmp :to #{:server/ws-cmp}}]])))

(defn get-config [config element default-value]
  (get-in config [:config element] default-value))

(defn parser-integer-config [config element default-value]
  (try
    (Integer. (get-config config element default-value))
    (catch NumberFormatException e default-value)))

(defn server-config [config]
  {:port (parser-integer-config config :server-port 3000)
   :ip   (get-config config :server-bind "0.0.0.0")})

(defrecord Page [config handler]
  c/Lifecycle
  (start [self]
    (restart! handler (server-config config))
    self)
  (stop [self]
    self))

(defn new-page [] (map->Page {}))

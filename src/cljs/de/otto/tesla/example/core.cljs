(ns de.otto.tesla.example.core
  (:require [de.otto.tesla.example.store :as store]
            [de.otto.tesla.example.ui :as cnt]
            [matthiasn.systems-toolbox.switchboard :as sb]
            [matthiasn.systems-toolbox-sente.client :as sente]))

(enable-console-print!)

(defonce switchboard (sb/component :client/switchboard))

(def sente-cfg {:relay-types #{:cnt/inc :cnt/dec :cnt/add :cnt/remove}})

(defn init
  []
  (sb/send-mult-cmd
    switchboard
    [[:cmd/init-comp
      #{(sente/cmp-map :client/ws-cmp sente-cfg) ; WebSocket communication
        (store/cmp-map :client/store-cmp)
        (cnt/cmp-map :client/cnt-cmp)}]
     [:cmd/route {:from :client/cnt-cmp :to #{:client/ws-cmp}}]
     [:cmd/route {:from :client/ws-cmp :to #{:client/store-cmp}}]
     [:cmd/observe-state {:from :client/store-cmp :to :client/cnt-cmp}]]))

(init)

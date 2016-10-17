(ns de.otto.tesla.example.store
  (:require [de.otto.tesla.example.spec]
            [clojure.tools.logging :as log]))

(defn msg-handler
  "Update message counter, then emit same message, which is then routed to
   client store to facilitate UI update."
  [{:keys [current-state msg msg-type]}]
  (let [new-state (assoc-in current-state [msg-type]
                            (inc (get current-state msg-type 0)))]
    (log/info "msg-handler received" msg new-state)
    {:new-state new-state
     :emit-msg  msg}))

(defn cmp-map
  "Generates component map for state-cmp."
  [cmp-id]
  {:cmp-id      cmp-id
   :state-fn    (fn [_put-fn] {:state (atom {})})
   :handler-map {:cnt/inc    msg-handler
                 :cnt/dec    msg-handler
                 :cnt/add    msg-handler
                 :cnt/remove msg-handler}})


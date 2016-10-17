(ns de.otto.tesla.example.example-system
  (:require [de.otto.tesla.system :as system]
            [de.otto.tesla.example.page :as page]
            [com.stuartsierra.component :as component])
  (:gen-class))

(defn example-system [runtime-config]
  (-> (system/base-system (merge {:name "httpkit-example"} runtime-config))
      (assoc :example-page
             (component/using (page/new-page) [:handler :config :app-status]))))

(defn -main
  "starts up the production system."
  [& args]
  (system/start (example-system {})))

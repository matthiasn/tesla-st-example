(defproject de.otto/tesla-st-example "0.1.18"
  :description "a simple example of an application build with tesla-microservice."
  :url "https://github.com/otto-de/tesla-examples"
  :license {:name "Apache License 2.0"
            :url  "http://www.apache.org/license/LICENSE-2.0.html"}
  :scm {:name "git"
        :url  "https://github.com/otto-de/tesla-examples"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                 [org.clojure/clojurescript "1.9.229"]
                 [de.otto/tesla-microservice "0.1.18"]
                 [matthiasn/systems-toolbox "0.6.1-alpha8"]
                 [matthiasn/systems-toolbox-ui "0.6.1-alpha10"]
                 [matthiasn/systems-toolbox-sente "0.6.1-alpha12"]
                 [hiccup "1.0.5"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [ch.qos.logback/logback-core "1.1.3"]
                 [ch.qos.logback/logback-classic "1.1.3"]]
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]
                   :plugins      [[lein-ancient "0.5.4"]]}}

  :main ^:skip-aot de.otto.tesla.example.example-system

  :clean-targets ^{:protect false} ["resources/public/js/build/" "target/"]

  :source-paths ["src/cljc" "src/clj/"]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :cljsbuild
  {:builds [{:id           "release"
             :source-paths ["src/cljc" "src/cljs"]
             :figwheel     true
             :compiler     {:main          "de.otto.tesla.example.core"
                            :asset-path    "js/build"
                            :optimizations :advanced
                            :output-to     "resources/public/js/build/example.js"}}]})

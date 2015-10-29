(defproject api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-time "0.9.0"] ; required due to bug in lein-ring
                 [http-kit "2.1.19"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [environ "1.0.0"]
                 [postgresql "9.1-901.jdbc4"]
                 [com.stuartsierra/component "0.2.3"]
                 [metosin/compojure-api "0.22.0"]]
  :ring {:handler api.handler/app}
  :uberjar-name "server.jar"
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [reloaded.repl "0.2.0"]]
                   :plugins [[lein-ring "0.9.6"]]
                   :main api.server}})
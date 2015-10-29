(ns api.server
  (:gen-class)
  (:require [compojure.api.middleware :refer [wrap-components]]
            [ring.adapter.jetty :refer [run-jetty]]
            [api.handler :refer [app]]
            [api.data :refer [create-db-schema]]
            [environ.core :refer [env]]))

(defn run-web-server [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (println (format "Starting web server on port %d." port))
    (run-jetty app {:port port :join? false})))

(defn run [& [port]]
  ;; (when is-dev?
  ;;   (run-auto-reload))
  (run-web-server port))

(defn -main [& [port]]
  (create-db-schema)
  (run port))

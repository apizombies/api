(ns api.server
  (:gen-class)
  (:require [compojure.api.middleware :refer [wrap-components]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :as reload]
            [api.handler :refer [app]]
            [api.data :refer :all]
            [environ.core :refer [env]]))

(def handler
  (reload/wrap-reload #'app))

(defn run-web-server [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (println (format "Starting web server on port %d." port))
    (run-jetty handler {:port port :join? false})))

(defn run [& [port]]
  ;; (when is-dev?
  ;;   (run-auto-reload))
  (run-web-server port))

(defn -main [& [port]]
  (create-db-schema)
  (run port))

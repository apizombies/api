(ns api.integrations
  (:require [org.httpkit.client :as http]))

(def base-url "") ;; TODO

(def github-endpoint (str base-url "/github"))
(def slack-endpoint (str base-url "/slack"))

(defn- add-to-github-org [github-id]
  (let [url (str github-endpoint "/" github-id)]
    (http/post url)))

(defn- delete-from-github-org [github-id]
  (let [url (str github-endpoint "/" github-id)]
    (http/delete url)))

(defn- add-to-slack [username]
  (let [url (str slack-endpoint "/" username)]
    (http/post url)))

(defn- delete-from-slack [username]
  (let [url (str slack-endpoint "/" username)]
    (http/delete url)))

(defn send-notification [notification employee]
  (case notification
    :new-employee (do (add-to-github-org (:github employee))
                      (add-to-slack (:username employee)))
    :deleted-employee (do (delete-from-github-org (:github employee))
                          (delete-from-slack (:username employee)))))

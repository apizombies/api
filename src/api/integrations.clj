(ns api.integrations
  (:require [org.httpkit.client :as http]))

(def base-url "http://apizombies-int.herokuapp.com")

(def google-accounts-endpoint ()) ;; TODO
(def github-endpoint (str base-url "/github"))
(def slack-endpoint (str base-url "/slack"))

(defn- google-mail [username]
  (str username "@apizombies.lol"))

(defn- add-to-google-accounts [username name last-name]
  ;;TODO
  )

(defn- delete-from-google-accounts [username]
  ;;TODO
  )

(defn- add-to-github-org [github-id]
  (let [url (str github-endpoint "/" github-id)]
    (http/put url)))

(defn- delete-from-github-org [github-id]
  (let [url (str github-endpoint "/" github-id)]
    (http/delete url)))

(defn- add-to-slack [email]
  (let [url (str slack-endpoint "/" email)]
    (http/put url)))

(defn- delete-from-slack [email]
  (let [url (str slack-endpoint "/" email)]
    (http/delete url)))

(defn send-notification [notification employee]
  (let [user-gmail (google-mail (:username employee))]
    (case notification
      :new-employee (do (add-to-google-accounts (:username employee)
                                                (:name employee)
                                                (:last-name employee))
                        (add-to-github-org (:github employee))
                        (add-to-slack user-gmail)))
      :deleted-employee (do (delete-from-google-accounts (:username employee))
                            (delete-from-github-org (:github employee))
                            (delete-from-slack user-gmail))))

(ns api.integrations
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [api.data :refer [add-google-acc]]))

(def base-url "http://apizombies-int.herokuapp.com")

(def google-endpoint (str base-url "/google"))
(def github-endpoint (str base-url "/github"))
(def slack-endpoint (str base-url "/slack"))

(defn- google-mail [username]
  (str username "@apizombies.lol"))

(defn- add-to-google-accounts [username name last-name email]
  (let [url (str google-endpoint "/" username)]
    (http/post url
               {:query-params {:name name :last_name last-name :email email}
                :headers {"Content-Type" "application/json"}}
               (fn [{:keys [status headers body error opts]}]
                 (let [json-response (json/read-str body :key-fn keyword)]
                   (add-google-acc username
                                   (:email json-response)
                                   (:password json-response)))))))

(defn- delete-from-google-accounts [username]
  (let [url (str google-endpoint "/" username)]
    (http/delete url)))

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

(defn- send-slack-msg [username]
  (let [url (str slack-endpoint "/message/orimarti@gmail.com")]
    (http/post url {:query-params {:message (str "Hola Uri, " username " has joined")}})))

(defn send-notification [notification employee]
  (let [user-gmail (google-mail (:username employee))]
    (case notification
      :new-employee (do (add-to-google-accounts (:username employee)
                                                (first (clojure.string/split (:name employee) #" "))
                                                (second (clojure.string/split (:name employee) #" "))
                                                (:email employee))
                        (add-to-github-org (:github employee))
                        (Thread/sleep 10000)
                        (add-to-slack user-gmail)
                        (send-slack-msg (:username employee)))
      :deleted-employee (do (delete-from-google-accounts (:username employee))
                            (delete-from-github-org (:github employee))
                            (delete-from-slack user-gmail)))))

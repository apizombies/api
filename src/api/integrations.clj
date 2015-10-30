(ns api.integrations)

(def base-url "") ;; TODO

(defn- add-to-github-org [employee]
  (println "Adding to GitHub")
  ;; TODO
  ;; (http/post "http://host.com/path" options
  ;;         (fn [{:keys [status headers body error]}] ;; asynchronous response handling
  ;;           (if error
  ;;             (println "Failed, exception is " error)
  ;;             (println "Async HTTP GET: " status))))
  )

(defn- delete-from-github-org [employee]
  (println "Deleting from GitHub")
  ;; TODO
  )

(defn- add-to-slack [employee]
  (println "Adding to Slack")
  ;; TODO
  )

(defn- delete-from-slack [employee]
  (println "Deleting from Slack")
  ;; TODO
  )

(defn send-notification [notification employee]
  (case notification
    :new-employee (do (add-to-github-org employee)
                      (add-to-slack employee))
    :deleted-employee (do (delete-from-github-org employee)
                          (delete-from-slack employee))))
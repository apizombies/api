(ns api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [api.data :refer :all]
            [api.integrations :refer [send-notification]]
            [schema.core :as s]
            [ring.swagger.schema :as rs]))

(s/defschema Employee {:id Long
                       :name String
                       :username String
                       :email String
                       :team String
                       :phone String
                       :skype String
                       :twitter String
                       :github String
                       :office String
                       :description String
                       :googleacc String
                       :googleaccpass String})

(s/defschema NewEmployee (dissoc Employee :id :googleacc :googleaccpass))

(defapi app
  (swagger-ui)
  (swagger-docs
    {:info {:title "ApiZombies"
            :description "API management company managed with an awesome API"}
     :tags [{:name "hello", :description "says hello in Finnish"}]})
  (context* "/webhooks" []
            (POST* "/typeform" []
           :return Boolean
           :body [body s/Any]
           (ok true)))
  (context* "/employees" []
    :tags ["employees"]
    (GET* "/" []
      :return [Employee]
      :summary "Gets all company employees"
      (ok (get-employees)))
    (GET* "/:id" []
          :path-params [id :- Long]
          :return (s/maybe Employee)
          :summary "Get an Employee"
          (if-let [employee (get-employee id)]
            (ok employee)
            (not-found {:reason "Employee not found"})))
    (POST* "/" []
           :return Employee
           :body [employee (describe NewEmployee "new employee")]
           :summary "Adds an Employee"
           (let [db-result (add-employee (rs/coerce NewEmployee employee))]
             (send-notification :new-employee db-result)
             (ok db-result)))
    (PUT* "/:id" []
          :path-params [id :- Long]
          :body [employee (describe NewEmployee "updated employee")]
          :summary "Updates an Employee"
          (if (zero? (update-employee id (rs/coerce NewEmployee employee)))
            (not-found {:reason "Employee not found"})
            (ok (get-employee id))))
    (DELETE* "/:id" []
             :path-params [id :- Long]
             :summary "Deletes an Employee"
             (let [original-employee (get-employee id)
                   db-result (delete-employee id)]
               (if (zero? (first db-result))
                 (not-found {:reason "Employee not found"})
                 (do
                   (send-notification :deleted-employee original-employee)
                   (ok {})))))))

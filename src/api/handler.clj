(ns api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [api.data :refer :all]
            [schema.core :as s]
            [ring.swagger.schema :as rs])
  (:import [org.joda.time DateTime]))

(s/defschema Employee {:id Long
                       :name String
                       :email String
                       :team String
                       :phone String
                       :skype String
                       :twitter String
                       :office String
                       :description String})

(s/defschema NewEmployee (dissoc Employee :id))

(defapi app
  (swagger-ui)
  (swagger-docs
    {:info {:title "ApiZombies"
            :description "API management company managed with an awesome API"}
     :tags [{:name "hello", :description "says hello in Finnish"}]})
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
           (ok (add-employee (rs/coerce NewEmployee employee))))
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
             (if (zero? (delete-employee id))
               (not-found {:reason "Employee not found"})
               (ok {})))))

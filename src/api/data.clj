(ns api.data
  (:require [clojure.java.jdbc  :as sql]
            [environ.core       :refer [env]]))

(def spec (or (env :database-url) "postgresql://localhost:5432/apizombies"))

(defn drop-employees-table []
  (sql/db-do-commands spec
                      (sql/drop-table-ddl :employees)))

(defn create-employees-table []
  (sql/db-do-commands spec
                      (sql/create-table-ddl
                       :employees
                       [:id :serial "PRIMARY KEY"]
                       [:name :varchar "NOT NULL"]
                       [:email :varchar "NOT NULL"]
                       [:team :varchar "NOT NULL"]
                       [:phone :varchar "NOT NULL"]
                       [:skype :varchar "NOT NULL"]
                       [:twitter :varchar "NOT NULL"]
                       [:office :varchar "NOT NULL"]
                       [:description :varchar "NOT NULL"])))

;; TODO remove this
(def toni-employee-map
  {:name "toni"
   :email "areina0@gmail.com"
   :team "engineering"
   :phone "626"
   :skype "toni.reina"
   :twitter "notengo"
   :office "BCN"
   :description "Redis babysitter"})


(defn add-employee [new-employee]
  (first (sql/insert! spec :employees new-employee)))

(defn update-employee [id updated-employee]
  (first (sql/update! spec :employees updated-employee ["id = ?" id])))

(defn get-employee [id]
  (println id)
  (first (sql/query spec ["select * from employees where id = ?" id])))

(defn get-employees []
  (sql/query spec ["select * from employees"]))

(defn delete-employee [id]
  (sql/delete! spec :employees ["id = ?" id]))
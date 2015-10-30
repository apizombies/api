(ns api.data
  (:require [clojure.java.jdbc  :as sql]
            [environ.core       :refer [env]]))

(def spec (or (env :database-url) "postgresql://localhost:5432/apizombies"))

(defn drop-employees-table []
  (sql/db-do-commands spec
                      (sql/drop-table-ddl :employees)))

(defn create-table [sql-statement]
  (clojure.string/replace-first sql-statement "CREATE TABLE" "CREATE TABLE IF NOT EXISTS"))

(defn create-employees-table []
  (sql/db-do-commands spec
                      (create-table (sql/create-table-ddl
                                     :employees
                                      [:id :serial "PRIMARY KEY"]
                                      [:name :varchar "NOT NULL"]
                                      [:username :varchar "NOT NULL"]
                                      [:email :varchar "NOT NULL"]
                                      [:team :varchar "NOT NULL"]
                                      [:phone :varchar "NOT NULL"]
                                      [:skype :varchar "NOT NULL"]
                                      [:twitter :varchar "NOT NULL"]
                                      [:github :varchar "NOT NULL"]
                                      [:office :varchar "NOT NULL"]
                                      [:description :varchar "NOT NULL"]
                                      [:googleacc :varchar "DEFAULT 'NOT_YET'"]
                                      [:googleaccpass :varchar "DEFAULT 'NOT_YET'"]))))

(defn create-db-schema []
  (create-employees-table))

(defn add-employee [new-employee]
  (first (sql/insert! spec :employees new-employee)))

(defn update-employee [id updated-employee]
  (first (sql/update! spec :employees updated-employee ["id = ?" id])))

(defn get-employee [id]
  (first (sql/query spec ["select * from employees where id = ?" id])))

(defn get-employees []
  (sql/query spec ["select * from employees"]))

(defn delete-employee [id]
  (sql/delete! spec :employees ["id = ?" id]))

(defn add-google-acc [username google-acc google-acc-pass]
  (let [current-employee (first (sql/query spec ["select * from employees where username = ?" username]))]
    (update-employee (:id current-employee)
                     (assoc current-employee :googleacc google-acc
                                             :googleaccpass google-acc-pass))))

(ns edge.database
  "Data schema definition and database access functions."
  (:require
    [clojure.tools.logging :refer :all]
    [com.stuartsierra.component :as component]
    [schema.core :as s]))

(s/defschema TodoInput
  {:text s/Str})

(s/defschema TodoInternal
  (-> TodoInput
      (assoc :id s/Uuid)
      (assoc :created-at s/Any)
      (assoc :completed-at s/Any)))

(s/defschema TodoOutput
  TodoInternal)

(s/defschema DB
  {:todos [TodoInternal]})

(s/def db-seed :- DB
  {:todos [{:text "one"
            :id (java.util.UUID/randomUUID)
            :created-at (java.util.Date.)
            :completed-at nil}
           
           {:text "two"
            :id (java.util.UUID/randomUUID)
            :created-at (java.util.Date.)
            :completed-at (java.util.Date.)}]})

(defonce root
  (atom db-seed))

(defrecord Database [connection]
  component/Lifecycle
  (start [this]
    (if connection
      this
      (do
        (infof "Starting database")
        (assoc this :connection root))))
  
  (stop [this]
    (if connection
      (do
        (infof "Stopping database")
        (dissoc this :connection))
      this)))

(defn new-database
  []
  (component/using
    (map->Database {})
    []))

;; # Data access functions

(s/defn get-todos :- [TodoOutput]
  [database]
  (:todos @(:connection database)))

(s/defn insert-todo! :- TodoOutput
  [database
   todo :- TodoInput]
  (let [final-todo (merge todo
                          {:id (java.util.UUID/randomUUID)
                           :created-at (java.util.Date.)
                           :completed-at nil})]
    (swap! (:connection database)
           update-in [:todos] conj final-todo)
    final-todo))


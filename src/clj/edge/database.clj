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
  {:todos {s/Uuid TodoInternal}})

(s/def db-seed :- DB
  (let [id1 (java.util.UUID/randomUUID)
        id2 (java.util.UUID/randomUUID)]
    {:todos {id1 {:text "one"
                  :id id1
                  :created-at (java.util.Date.)
                  :completed-at nil}

             id2 {:text "two"
                  :id id2
                  :created-at (java.util.Date.)
                  :completed-at nil}}}))

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
  (let [id (java.util.UUID/randomUUID)
        final-todo (merge todo
                          {:id id
                           :created-at (java.util.Date.)
                           :completed-at nil})]
    (swap! (:connection database)
           update-in [:todos] assoc id final-todo)
    final-todo))

(s/defn complete-todo! :- TodoOutput
  [database
   todo-id :- s/Uuid]
  (swap! (:connection database)
         update-in [:todos todo-id :completed-at] (constantly (clj-time.core/now))))


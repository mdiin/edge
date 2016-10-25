(ns edge.event-handlers
  (:require
    [edge.database :as db]
    [edge.database.schema :as db-s]

    [re-frame.core :refer [reg-event-db]]
    [schema.core :as s]))

(reg-event-db
  :app/initialize
  (s/fn app-initialize-event :- db-s/DB
    [db :- db-s/DB
     _]
    (merge db db/initial-state)))


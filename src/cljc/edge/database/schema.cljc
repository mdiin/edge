(ns edge.database.schema
  (:require
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


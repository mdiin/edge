(ns edge.database
  (:require
    [edge.database.schema :as db-s]
    
    [schema.core :as s]))

(s/def initial-state :- db-s/DB
  {:todos {}})


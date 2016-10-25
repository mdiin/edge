(ns edge.subscription-handlers
  (:require
    [edge.database.schema :as db-s]

    [re-frame.core :refer [reg-sub]]
    [schema.core :as s]))

(reg-sub
  :todos/all
  (s/fn todos-all-sub :- [db-s/TodoOutput]
    [db :- db-s/DB
     _]
    (:todos db)))


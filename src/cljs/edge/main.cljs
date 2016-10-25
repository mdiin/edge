(ns edge.main
  (:require
    [edge.event-handlers]
    [edge.subscription-handlers]
    [edge.views :as v]

    [reagent.core :as reagent]
    [re-frame.core :refer [dispatch-sync]]
    [schema.core :as s]))

(s/set-fn-validation! true)

(defn ^:export init
  []
  (.log js/console "INIT 2")
  (dispatch-sync [:app/initialize])
  (reagent/render [v/Root]
                  (.getElementById js/document "app")))


(ns edge.resources
  (:require
    [clojure.tools.logging :refer :all]
    [edge.database :as db]
    [hiccup.core :as hiccup]
    [yada.yada :as yada]))

(defn root
  []
  (yada/resource
    {:methods {:get {:produces "text/html"
                     :response "<html><head></head><body><h1>The root</h1></body></html>"}}}))

(defn todos-get-response
  [database ctx]
  (case (yada/content-type ctx)
    "text/html" (hiccup/html
                  [:html
                   [:head]
                   [:body
                    [:main
                     [:h1 "Todos"]
                     [:ol
                      (for [{:keys [text]} (db/get-todos database)]
                        [:li [:span text]])]]]])
    "application/json" (db/get-todos database)))

(defn todos-post-response
  [database ctx]
  (let [todo (:body ctx)]
    (db/insert-todo! database todo)))

(defn todos
  [database]
  (yada/resource
    {:methods {:get {:produces #{"text/html" "application/json"}
                     :response (partial todos-get-response database)}
               :post {:consumes "application/json"
                      :produces "application/json"
                      :response (partial todos-post-response database)}}}))


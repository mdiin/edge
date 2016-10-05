(ns edge.resources
  (:require
    [clojure.tools.logging :refer :all]
    [edge.database :as db]
    [edge.views :as views]
    [schema.core :as s]
    [yada.yada :as yada]))

(defn request-content-type
  [ctx]
  (get-in ctx [:request :headers "content-type"]))

(defn root
  []
  (yada/resource
    {:methods {:get {:produces "text/html"
                     :response "<html><head></head><body><h1>The root</h1></body></html>"}}}))

(defn todos-get-response
  [database ctx]
  (case (yada/content-type ctx)
    "text/html" (views/as-html
                  (views/Todos (db/get-todos database)))
    "application/json" (db/get-todos database)))

(defn todos-post-response
  [database ctx]
  (case (request-content-type ctx)
    "application/json"
    (let [todo (:body ctx)]
      (db/insert-todo! database todo))

    "application/x-www-form-urlencoded"
    (let [todo {:text (get-in ctx [:parameters :form :text])}]
      (db/insert-todo! database todo)
      (-> (:response ctx)
          (assoc :status 303)
          (update-in [:headers] assoc "location" "/todos")))))

(defn todos
  [database]
  (yada/resource
    {:methods {:get {:produces #{"text/html" "application/json"}
                     :response (partial todos-get-response database)}
               :post {:consumes #{"application/json" "application/x-www-form-urlencoded"}
                      :produces "application/json"
                      :parameters {:form
                                   {:type (s/eq "create-new")
                                    :text s/Str}}
                      :response (partial todos-post-response database)}}}))

(defn todos-update-response
  [database ctx]
  (case (request-content-type ctx)
    "application/json"
    (let [{:keys [ids]} (:body ctx)]
      (doseq [id ids]
        (db/complete-todo! database id))
      (-> (:response ctx)
          (assoc :status 303)
          (update-in [:headers] assoc "location" "/todos")))
    
    "application/x-www-form-urlencoded"
    (let [{:keys [ids]} (get-in ctx [:parameters :form])]
      (doseq [id ids]
        (db/complete-todo! database id))
      (-> (:response ctx)
          (assoc :status 303)
          (update-in [:headers] assoc "location" "/todos")))))

(defn todos-update
  [database]
  (yada/resource
    {:methods {:post {:consumes #{"application/json" "application/x-www-form-urlencoded"}
                      :produces "application/json"
                      :parameters {:form
                                   {:type (s/eq "toggle-complete")
                                    :ids [s/Uuid]}}
                      :response (partial todos-update-response database)}}}))


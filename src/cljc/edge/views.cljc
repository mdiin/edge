(ns edge.views
  (:require
    #?(:clj [hiccup.core :as html]
       :cljs [sablono.core :as html :refer-macros [html]])
    [schema.core :as s]))

(s/defn as-html
  [tree]
  #?(:clj (html/html tree)
     :cljs (html tree)))

(s/defn Todo
  [t]
  [:input {:type :checkbox
           :name "ids"
           :checked (not (nil? (:completed-at t)))
           :value (:id t)}
   [:span (:text t)]])

(s/defn Todos
  [ts]
  [:section
   [:form {:method :post
           :action "/todos/update"}
    [:ol
     (for [[_ t] ts]
       [:li (Todo t)])]
    [:input {:type :hidden
             :name "type"
             :value "toggle-complete"}]
    [:input {:type :submit
             :value "Complete"}]]

   [:form {:method :post
           :action "/todos"}
    [:input {:type "text"
             :placeholder "Your next task..."
             :name "text"}]
    [:input {:type :hidden
             :name "type"
             :value "create-new"}]
    [:input {:type :submit
             :value "New"}]]])

(s/defn Root :- s/Any
  [ts]
  [:html
   [:head
    [:title "Todos and stuff"]]
   [:body
    [:header
     [:h1 "Todos"]]
    [:main
     #?(:clj (Todos ts)
        :cljs nil)]]])


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

(s/defn ServerRoot :- s/Any
  [ts]
  [:html {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible"
            :content "IE=edge"}]
    [:meta {:name "description"
            :content "TODO"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1"}]
    [:title "Todos and stuff"]
    [:link {:rel "stylesheet"
            :href "app.css"}]
    [:link {:rel "icon"
            :type "image/png"
            :href "img/favicon-16x16.png"}]
    [:link {:rel "icon"
            :type "image/png"
            :href "img/favicon-16x16.png"
            :sizes "16x16"}]
    [:link {:rel "icon"
            :type "image/png"
            :href "img/favicon-96x96.png"
            :sizes "96x96"}]
    [:link {:rel "icon"
            :type "image/png"
            :href "img/favicon-192x192.png"
            :sizes "192x192"}]
    ;; Firefox needs 32x32 last
    [:link {:rel "icon"
            :type "image/png"
            :href "img/favicon-32x32.png"
            :sizes "32x32"}]
    [:link {:rel "apple-touch-icon"
            :href="img/favicon-60x60.png"}]
    [:link {:rel "apple-touch-icon"
            :href "img/favicon-76x76.png"
            :sizes "76x76"}]
    [:link {:rel "apple-touch-icon"
            :href "img/favicon-120x120.png"
            :sizes "120x120"}]
    [:link {:rel "apple-touch-icon"
            :href "img/favicon-152x152.png"
            :sizes "152x152"}]]
   [:body
    [:section#app
     [:header
      [:h1 "Todos"]]
     [:main (Todos ts)]]
    [:script {:type "text/javascript"
              :src "edge.js"}]]])

(defn ClientRoot
  []
  [:p "Foo"])

(defn Root
  [?data]
  #?(:clj (ServerRoot ?data)
     :cljs [ClientRoot]))

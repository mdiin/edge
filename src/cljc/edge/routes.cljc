(ns edge.routes
  (:require
    [bidi.bidi :refer [tag]]
    [bidi.schema :refer [RoutePair]]
    [edge.resources :as r]
    [schema.core :as s]
    #?@(:clj [[yada.handler]
              [yada.yada :as yada]])))

(s/defn routes :- RoutePair
  "Create the URI route structure for our application."
  [?database :- (s/maybe {s/Any s/Any})]
  [""
   [
    ["/" (r/root)]
    ["/todos" #?(:clj (r/todos ?database)
                 :cljs (r/todos))]

    ;; This is a backstop. Always produce a 404 if we ge there. This
    ;; ensures we never pass nil back to Aleph.
    [true #?(:clj (yada/handler nil))]]])


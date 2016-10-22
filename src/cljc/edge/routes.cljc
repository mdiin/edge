(ns edge.routes
  (:require
    [bidi.bidi :refer [tag]]
    [bidi.schema :refer [RoutePair]]
    [edge.resources :as r]
    [schema.core :as s]
    #?@(:clj [[yada.handler]
              [yada.yada :as yada]
              [clojure.java.io :as io]])))

(s/defn routes :- RoutePair
  "Create the URI route structure for our application."
  [?database :- (s/maybe {s/Any s/Any})]
  [""
   [
    ["/" (r/root ?database)]
    ["/todos" #?(:clj [["" (r/todos ?database)]
                       ["/update" (r/todos-update ?database)]]
                 :cljs (r/todos))]

    #?(:clj ["/" (-> (yada/as-resource (io/file "target"))
                    (assoc :id :edge.resources/static))])

    ;; This is a backstop. Always produce a 404 if we ge there. This
    ;; ensures we never pass nil back to Aleph.
    [true #?(:clj (yada/handler nil))]]])


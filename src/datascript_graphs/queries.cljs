(ns datascript-graphs.queries
  (:require [reagent.core :as reagent]
            [datascript.core :as datascript]
            [datascript-graphs.state :as state]))


(defn format-node [{:node/keys [name]}]
  {:data {:id name :label name}})


(defn format-edge [{:edge/keys [id source target]}]
  {:data
   {:id     id
    :label  id
    :source (:node/name source)
    :target (:node/name target)}})


(defn query [query-vec]
  (reagent/track
    (fn [q]
      (datascript/q q @state/data))
    query-vec))


(def nodes-query
  (query '[:find [(pull ?e [*]) ...]
           :where [?e :node/name _]]))


(def edges-query
  (query '[:find [(pull ?e [* {:edge/source [:node/name]
                               :edge/target [:node/name]}]) ...]
           :where [?e :edge/id _]]))


(def full-graph
  (reagent/track
    (fn []
      (let [nodes @nodes-query
            edges @edges-query]
        (vec (concat (map format-node nodes)
                     (map format-edge edges)))))))


(def edge-rules
  '[[(successors ?source-node ?edge)
     [?edge :edge/source ?source-node]]
    [(successors ?source-node ?edge)
     [?connected-edge :edge/source ?source-node]
     [?connected-edge :edge/target ?target-node]
     (successors ?target-node ?edge)]])


(def query-from-node
  '{:find  [[(pull ?edge [* {:edge/source [:node/name] :edge/target [:node/name]}]) ...]]
    :in    [$ % ?root-node-name]
    :where [[?root-node :node/name ?root-node-name]
            (successors ?root-node ?edge)]})


(def graph-from-node
  (reagent/track
    (fn []
      (let [edges (datascript/q query-from-node @state/data edge-rules @state/root-node)
            nodes (->> edges
                       (mapcat (fn [edge] [(:edge/source edge) (:edge/target edge)]))
                       set)]
        (if-not (empty? edges)
          (vec (concat (map format-node nodes)
                       (map format-edge edges)))
          [(format-node {:node/name @state/root-node})])))))


(def graph
  (reagent/track
    (fn []
      (if (some? @state/root-node)
        @graph-from-node
        @full-graph))))

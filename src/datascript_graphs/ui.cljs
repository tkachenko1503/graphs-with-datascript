(ns datascript-graphs.ui
  (:require [applied-science.js-interop :as jsi]
            [reagent.core :as reagent]
            ["cytoscape" :as cytoscape]
            ["cytoscape-klay" :as klay]
            ["react-cytoscapejs" :as CytoscapeComponent]
            [datascript-graphs.state :as state]
            [datascript-graphs.queries :as queries]))


(jsi/call cytoscape :use klay)


(def node-options
  (reagent/track
    (fn []
      (let [nodes @queries/nodes-query]
        (vec (map queries/format-node nodes))))))


(def layout-options
  (clj->js
    {:name                        "klay"
     :nodeDimensionsIncludeLabels true
     :fit                         true
     :padding                     40
     :animate                     true
     :animateFilter               (constantly nil)
     :animationDuration           1000
     :animationEasing             "ease-in-out-circ"
     :transform                   (fn [_ pos] pos)
     :klay                        {:addUnnecessaryBendpoints          true
                                   :aspectRatio                       1.5
                                   :borderSpacing                     10
                                   :compactComponents                 false
                                   :crossingMinimization              "LAYER_SWEEP"
                                   :cycleBreaking                     "GREEDY"
                                   :direction                         "RIGHT"
                                   :edgeRouting                       "ORTHOGONAL"
                                   :edgeSpacingFactor                 0.8
                                   :feedbackEdges                     false
                                   :fixedAlignment                    "BALANCED"
                                   :inLayerSpacingFactor              2
                                   :layoutHierarchy                   true
                                   :linearSegmentsDeflectionDampening 0.8
                                   :mergeEdges                        false
                                   :mergeHierarchyCrossingEdges       false
                                   :nodeLayering                      "NETWORK_SIMPLEX"
                                   :nodePlacement                     "SIMPLE"
                                   :randomizationSeed                 4
                                   :routeSelfLoopInside               false
                                   :separateConnectedComponents       true
                                   :spacing                           40
                                   :thoroughness                      2}
     :priority                    (constantly nil)}))


(def stylesheet
  (clj->js
    [{:selector "node"
      :style    {:shape            "octagon"
                 :label            "data(label)"
                 :background-color "#4317bd"
                 :text-valign      "top"
                 :text-halign      "center"}}
     {:selector "edge"
      :style    {:width              1
                 :line-color         "#d8d8e6"
                 :target-arrow-color "#2b2b3a"
                 :curve-style        "bezier"
                 :target-arrow-shape "triangle-backcurve"}}]))


(defn graph []
  [:> CytoscapeComponent
   {:elements   @queries/graph
    :style      {:width "100%" :height "100%"}
    :layout     layout-options
    :stylesheet stylesheet
    :cy         (fn [cy]
                  (let [nodes (jsi/call cy :$)]
                    ;; Place new nodes
                    (-> nodes
                        (jsi/call :layout layout-options)
                        (jsi/call :run))
                    ;; Fit to the new graph
                    (jsi/call cy :fit)))}])


(defn select-root-node [event]
  (let [value (jsi/get-in event [:target :value])]
    (if (not (empty? value))
      (reset! state/root-node value)
      (reset! state/root-node nil))))


(defn root-select []
  [:div {:class "graph-control"}
   (into
     [:select {:default-value ""
               :on-change     select-root-node}
      [:option {:value ""} "All nodes"]]
     (for [node @node-options
           :let [id    (get-in node [:data :id])
                 label (get-in node [:data :label])]]
       ^{:key id}
       [:option {:value (:id node)}
        label]))])


(defn app-root []
  [:div {:class "root"}
   [graph]
   [root-select]])


(defn mount []
  (reagent/render-component
    [app-root]
    (jsi/call js/document :getElementById "app")))

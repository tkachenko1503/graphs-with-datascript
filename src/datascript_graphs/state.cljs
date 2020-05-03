(ns datascript-graphs.state
  (:require [reagent.core :as reagent]
            [datascript.core :as datascript]))


(def schema
  {:node/name   {:db/unique :db.unique/identity}
   :edge/id     {:db/unique :db.unique/identity}
   :edge/target {:db/valueType :db.type/ref}
   :edge/source {:db/valueType :db.type/ref}})


(defonce connection
  (datascript/create-conn schema))


(defonce data
  (reagent/atom nil))


(def root-node
  (reagent/atom nil))


(datascript/listen!
  connection
  :change-handler
  (fn [tx-report]
    (reset! data (:db-after tx-report))))


(def initial-state
  [;; nodes
   {:node/name "1"}
   {:node/name "2"}
   {:node/name "3"}
   {:node/name "4"}
   {:node/name "5"}
   {:node/name "6"}
   {:node/name "7"}
   {:node/name "8"}
   {:node/name "9"}
   {:node/name "10"}
   {:node/name "11"}
   {:node/name "12"}
   {:node/name "13"}
   {:node/name "14"}

   ;; edges
   {:edge/id     "1->2"
    :edge/target [:node/name "2"]
    :edge/source [:node/name "1"]}
   {:edge/id     "1->3"
    :edge/target [:node/name "3"]
    :edge/source [:node/name "1"]}
   {:edge/id     "2->4"
    :edge/target [:node/name "4"]
    :edge/source [:node/name "2"]}
   {:edge/id     "3->5"
    :edge/target [:node/name "5"]
    :edge/source [:node/name "3"]}
   {:edge/id     "2->7"
    :edge/target [:node/name "7"]
    :edge/source [:node/name "2"]}
   {:edge/id     "4->8"
    :edge/target [:node/name "8"]
    :edge/source [:node/name "4"]}
   {:edge/id     "4->9"
    :edge/target [:node/name "9"]
    :edge/source [:node/name "4"]}
   {:edge/id     "3->1"
    :edge/target [:node/name "1"]
    :edge/source [:node/name "3"]}
   {:edge/id     "3->10"
    :edge/target [:node/name "10"]
    :edge/source [:node/name "3"]}
   {:edge/id     "4->11"
    :edge/target [:node/name "11"]
    :edge/source [:node/name "4"]}
   {:edge/id     "6->12"
    :edge/target [:node/name "12"]
    :edge/source [:node/name "6"]}
   {:edge/id     "8->13"
    :edge/target [:node/name "13"]
    :edge/source [:node/name "8"]}
   {:edge/id     "10->14"
    :edge/target [:node/name "14"]
    :edge/source [:node/name "10"]}
   {:edge/id     "9->12"
    :edge/target [:node/name "12"]
    :edge/source [:node/name "9"]}
   {:edge/id     "4->11"
    :edge/target [:node/name "11"]
    :edge/source [:node/name "4"]}
   {:edge/id     "3->8"
    :edge/target [:node/name "8"]
    :edge/source [:node/name "3"]}
   {:edge/id     "2->6"
    :edge/target [:node/name "6"]
    :edge/source [:node/name "2"]}
   {:edge/id     "7->10"
    :edge/target [:node/name "10"]
    :edge/source [:node/name "7"]}
   {:edge/id     "4->5"
    :edge/target [:node/name "5"]
    :edge/source [:node/name "4"]}
   {:edge/id     "1->11"
    :edge/target [:node/name "11"]
    :edge/source [:node/name "1"]}])

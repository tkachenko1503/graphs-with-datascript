(ns datascript-graphs.core
  (:require [datascript.core :as datascript]
            [datascript-graphs.state :as state]
            [datascript-graphs.ui :as ui]))


(defn initialize-state! []
  (datascript/transact! state/connection state/initial-state))


(defn on-update []
  (ui/mount))


(defn ^:export init []
  (enable-console-print!)
  (initialize-state!)
  (ui/mount))


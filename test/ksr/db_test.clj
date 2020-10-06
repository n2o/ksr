(ns ksr.db-test
  (:require [ksr.db :as db]
            [clojure.test :refer [deftest is]]
            [mount.core :as mount]))

;; Korma is weird because it does its own state management. I dont like it. These tests should have mock-dbs
(mount/start #'ksr.db/db-conn)

(def participant
  {:name "kangaroo"
   :einheit "Das asoziale Netzwerk"
   :stand "Nüscht"
   :essen-besonderheiten "Nein"
   :das-letzte-thema-staendekreis "Windows Vista installieren"
   :orden-ist-fuer-mich "..."
   :anfahrt "Untergrund"})

(deftest add-and-query-participant
  (let [res (db/add-participant! participant)]
    (is (pos? ((ffirst res) res)))
    (is (not (empty? (db/get-participants))))
    (is (pos? (db/delete-all-participants!)))))

(deftest no-duplicates-allowed
  (let [_ (db/add-participant! participant)
        status (db/add-participant! participant)]
    (is (= :duplicate status))
    (is (pos? (db/delete-all-participants!)))))

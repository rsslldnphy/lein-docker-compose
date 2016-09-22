(ns lein-docker-compose.plugin
  (:use [robert.hooke :only (add-hook)])
  (:require [clj-yaml.core :as yaml]
            [clojure.java.io :as io]
            [clojure.java.shell :as sh]
            [clojure.string :as s]
            [leiningen.core.main]))

(defn env-file
  [project]
  (io/file (:root project) ".lein-docker-env"))

(defn docker-compose-file
  [project]
  (io/file (:root project) "docker-compose.yml"))

(defn extract-container-port
  [port-config]
  (-> (str port-config)
      (s/split #":")
      (last)))

(defn get-exposed-ports
  [services]
  (for [[service config] services
        port-config      (:ports config)]
    [(name service) (extract-container-port port-config)]))

(defn config-key
  [[service container-port]]
  (keyword (str "docker-" service "-port-" container-port)))

(defn discover-port-mapping
  [[service container-port]]
  (let [output (sh/sh "docker-compose" "port" service container-port)]
    (when (zero? (:exit output))
      (s/replace (:out output) #"^.*:(.*)\n$" "$1"))))

(defn discover-docker-ports
  [project]
  (if-not (.exists (docker-compose-file project))
    (println "WARNING: Could not find docker-compose.yml")
    (->> (slurp (docker-compose-file project))
         (yaml/parse-string)
         (get-exposed-ports)
         (map (juxt config-key discover-port-mapping))
         (into {}))))

(defn start-services!
  []
  (let [output (sh/sh "docker-compose" "up" "-d")]
    (when-not (zero? (:exit output))
      (println "WARNING: Failed to bring up docker-compose services"))))

(defn write-env-to-file!
  [project]
  (spit (env-file project) (pr-str (discover-docker-ports project))))

(defn docker-compose-task
  [func task-name project args]
  (start-services!)
  (write-env-to-file! project)
  (func task-name project args))

(defn hooks []
  (add-hook #'leiningen.core.main/apply-task #'docker-compose-task))

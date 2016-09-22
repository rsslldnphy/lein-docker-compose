(defproject lein-docker-compose "0.1.0"
  :description "Leiningen plugin that provides environ with docker-compose port mappings"
  :url "https://github.com/rsslldnphy/lein-docker-compose"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[circleci/clj-yaml "0.5.5"]]
  :profiles {:dev {:resource-paths ["test/resources"]}}
  :eval-in-leiningen true)

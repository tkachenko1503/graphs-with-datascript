(defproject datascript-graphs "1.0.0"
  :description "Example project for working with graphs in Datascript"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"

  :dependencies [[org.clojure/clojure "1.10.1"]]

  :profiles {:cljs {:source-paths ["src/" "test/"]
                    :dependencies [[binaryage/devtools "0.9.10"]
                                   [thheller/shadow-cljs "2.8.94"]
                                   ;; App deps
                                   [reagent "0.9.1"]
                                   [datascript "0.18.7"]
                                   [appliedscience/js-interop "0.2.4"]]}})

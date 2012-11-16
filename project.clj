(defproject warmit "0.1.0-SNAPSHOT"
  :description "Global warming simulator"
  :url "https://github.com/solita/codecamp2012-warmit"
  :license {:name "WTFPL"
            :url "http://sam.zoy.org/wtfpl/"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [noir "1.3.0-beta10"]]
  :plugins [[lein-cljsbuild "0.2.9"]]
  :source-paths ["src/clj"]
  :cljsbuild {:builds [{:source-path "src/cljs"
                        :compiler {:output-to "resources/public/js/cljs.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :main warmit.server)

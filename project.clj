(defproject cljs-hello "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [noir "1.3.0-beta10"]]
  :plugins [[lein-cljsbuild "0.2.9"]]
  :source-paths ["src/clj"]
  :cljsbuild
  {:builds [{:source-path "src/cljs"
             :compiler {:output-to "resources/public/js/cljs_hello.js"
                        :optimizations :whitespace
                        :pretty-print true}}]})

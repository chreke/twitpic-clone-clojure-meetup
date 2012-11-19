(defproject twitpic "0.1.0-SNAPSHOT"
  :description "Snurveys-Next Gen Survey Tool"
  :url "http://www.twitpic.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/clj"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [ring "1.0.1"]
                 [enlive "1.0.0"]
                 [clj-aws-s3 "0.3.2"]]
  :plugins [[lein-cljsbuild "0.2.9"]
            [lein-ring "0.7.5"]]
  :ring {:handler twitpic.core/handler}
  :cljsbuild {:builds
              [{; clojurescript source code path
                :source-path "src/cljs"
                                        ; Google Closure Compiler options
                :compiler {; the name of the emitted JS file
                           :output-to "resources/public/js/twitpic.js"
                                        ; use minimal optimization CLS directive
                           :optimizations :whitespace
                                        ; prettyfying emitted JS
                           :pretty-print true}}]})

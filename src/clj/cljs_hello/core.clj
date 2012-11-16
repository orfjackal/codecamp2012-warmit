(ns cljs-hello.core
  (:require [noir.core :refer [defpage]]
            [noir.response :as resp]
            [noir.server :as server])
  (:gen-class ))

(defpage "/" []
  (resp/redirect "/index.html"))

(server/start 8070)

(defn -main [& args])

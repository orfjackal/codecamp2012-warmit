(ns warmit.server
  (:require [noir.core :refer [defpage]]
            [noir.response :as resp]
            [noir.server :as server])
  (:gen-class ))

(defpage "/" []
  (resp/redirect "/index.html"))

(defn -main [& args]
  (server/start 8070))

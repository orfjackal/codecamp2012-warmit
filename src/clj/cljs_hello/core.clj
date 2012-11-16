(ns cljs-hello.core
  (:require [noir.core :refer [defpage]]
            [noir.server :as server]
            #_[hiccup :refer [html ]]))

(defpage "/" []
  "trololoo jee asdfasdf")

(server/start 8080)

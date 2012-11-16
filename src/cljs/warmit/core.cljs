(ns warmit.core
  (:require [jayq.core :refer [$,bind]]))

(defn make-square []
  (let [geometry (THREE.CubeGeometry. 200 200 200)
        material (THREE.MeshBasicMaterial. (js* "{color: 0xff0000,
                                                wireframe: true}"))
        mesh (THREE.Mesh. geometry material)]
    mesh))

(defn make-world []
  (let [scene (THREE.Scene.)
        renderer (doto (THREE.CanvasRenderer.)
      (.setSize 1000 300))
        camera (doto (THREE.PerspectiveCamera. 75 (/ (.-innerWidth js/window)
                                                    (.-innerHeight js/window))
                       1 10000)
      (-> .-position .-z (set! 1000)))]
    (-> js/document .-body (.appendChild (.-domElement renderer)))
    {:state {}
     :renderer renderer
     :scene scene
     :camera camera}))

(defn animate [{:keys [state renderer scene camera]
                :as world}]

  ; TODO: extract game loop
  (.add scene (make-square))

  (js/requestAnimationFrame (partial animate world))
  (.render renderer scene camera))

(defmulti handle-keypress (fn [event] (.-which event)))

(defmethod handle-keypress 32 [event]
  (js/alert "SPACE"))

(defmethod handle-keypress 37 [event]
  (js/alert "LEFT"))

(defmethod handle-keypress 39 [event]
  (js/alert "RIGHT"))

(bind ($ js/document) "keydown" handle-keypress)

(animate (make-world))

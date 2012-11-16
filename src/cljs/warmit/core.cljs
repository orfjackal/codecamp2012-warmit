(ns warmit.core
  (:require [jayq.core :refer [$,bind]]))

(defn make-square []
  (let [geometry (THREE.CubeGeometry. (rand-int 200) (rand-int 200) 200)
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
    {:world {}
     :renderer renderer
     :scene scene
     :camera camera}))

(defn update-world [world]
  {:catapult {:x 200 :y 100}})

(defn update-scene [scene world]
  (doseq [child (.-children scene)]
    (.remove scene child))
  (.add scene (make-square)))

(defn animate [state]
  (let [state (update-in state [:world ] update-world)]
    (update-scene (:scene state) (:world state))

    (js/requestAnimationFrame (partial animate state))
    (.render (:renderer state) (:scene state) (:camera state))))

(defmulti handle-keypress (fn [event] (.-which event)))

(defmethod handle-keypress 32 [event]
  (js/alert "SPACE"))

(defmethod handle-keypress 37 [event]
  (js/alert "LEFT"))

(defmethod handle-keypress 39 [event]
  (js/alert "RIGHT"))

(defmethod handle-keypress :default [event])

(bind ($ js/document) "keydown" handle-keypress)

(animate (make-world))

(ns warmit.core
  (:require [jayq.core :refer [$,bind]]
            [warmit.world :as world]))
(declare events)
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
    {:world {:catapult {:x 200 :y 100}}
     :renderer renderer
     :scene scene
     :camera camera}))

(defn update-scene [scene world]
  (doseq [child (.-children scene)]
    (.remove scene child))
  (.add scene (make-square)))

(defn animate [state]
  (let [state (update-in state [:world ] (fn [world]
                                           (reduce world/update world @events)))]
    (reset! events [])
    (update-scene (:scene state) (:world state))
    (js/requestAnimationFrame (partial animate state))
    (.render (:renderer state) (:scene state) (:camera state))
    ))

; Input handling
(def events (atom []))

(def button-events {32 :space
                    37 :left
                    39 :right})

(defn bind-button-handler [jquery-event-name value]
  (letfn [(handler [jquery-event]
            (when-let [event (button-events (.-which jquery-event))]
              (swap! events conj [event value])))]
    (bind ($ js/document) jquery-event-name handler)))

(bind-button-handler "keydown" :pressed)
(bind-button-handler "keyup" :released)

; Animation
(animate (make-world))

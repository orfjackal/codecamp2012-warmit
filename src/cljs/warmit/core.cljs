(ns warmit.core
  (:require [jayq.core :refer [$ bind]]
            [jayq.util :refer [clj->js]]
            [warmit.world :as world]))

(declare events)

(defn make-world []
  (let [scene (THREE.Scene.)
        renderer (doto (THREE.CanvasRenderer.)
      (.setSize 1000 300))
        camera (doto (THREE.PerspectiveCamera. 75 (/ (.-innerWidth js/window)
                                                    (.-innerHeight js/window))
                       1 10000)
      (-> .-position .-z (set! 1000)))]
    (-> js/document .-body (.appendChild (.-domElement renderer)))
    {:world {:catapult {:speed-x 0, :x 2000 
                        :speed-y 0, :y 100 
                        :speed-force 0, :force 0}
             :barrel {:fire-time 0
                      :fire-x 0
                      :landing-time 0
                      :launched? false
                      }}
     :renderer renderer
     :scene scene
     :camera camera}))

(defn project-x [x] (- x 2000))
(defn project-y [y] (- y 800))

(defn make-square [x y color]
  (let [geometry (THREE.CubeGeometry. 200 200 50)
        material (THREE.MeshBasicMaterial. (clj->js {:color color, :wireframe true}))
        square (THREE.Mesh. geometry material)]
    (.translateX square (project-x x))
    (.translateY square (project-y y))
    square))

(defn update-scene [scene world]
  (doseq [child (.-children scene)]
    (.remove scene child))
  (let [catapult (:catapult world)]
    (.add scene (make-square (:x catapult) (:y catapult) 0xff0000))))


(defn get-time [] (.getTime (js/Date.)))

(defn animate [state last-time]
  (let [cur-time (get-time)
        dt (- cur-time last-time)
        state (update-in state [:world ] (fn [world]
                                           (reduce world/update world  (conj @events [:dt dt cur-time]))))]
    (reset! events [])
    (update-scene (:scene state) (:world state))
    (js/requestAnimationFrame (partial animate state cur-time))
    (.render (:renderer state) (:scene state) (:camera state))))

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

(bind-button-handler "keydown" :pressed )
(bind-button-handler "keyup" :released )

; Animation
(animate (make-world) (get-time))

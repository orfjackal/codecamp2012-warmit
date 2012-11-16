(ns warmit.core
  (:require [jayq.core :refer [$ bind]]
            [jayq.util :refer [clj->js]]
            [warmit.world :as world]))

(declare events)

(defn make-world []
  (let [scene (THREE.Scene.)
        renderer (doto (THREE.CanvasRenderer.)
                       (.setSize 1000 600))
        light (doto (THREE.SpotLight. 0xaabbcc 1,25)
                    (-> .-position (.set -500 900 600))
                    (-> .-target .-position (.set 2000 0 2000))
                    (-> .-castShadow (set! true)))
        plane (doto (THREE.Mesh. (THREE.PlaneGeometry. 20000 5000) (THREE.MeshBasicMaterial. (clj->js {:color 0xddddff})))
                    (-> .-overdraw (set! true))
                    (-> .-position .-x (set! 0))
                    (-> .-position .-y (set! 3000))
                    (-> .-position .-z (set! -1000)))
        camera (doto (THREE.PerspectiveCamera. 75 (/ (.-innerWidth js/window)
                                                    (.-innerHeight js/window))
                                                  1 10000)
                     (-> .-rotation .-x (set! (/ js/Math.PI 4)))
                     (-> .-position .-y (set! -1450))
                     (-> .-position .-z (set! 1000)))]
    (.add scene light)
    (.add scene plane)
    (-> js/document .-body (.appendChild (.-domElement renderer)))
    {:world world/initial-world
     :renderer renderer
     :scene scene
     :camera camera}))

(defn project-x [x] (- x 2000))
(defn project-y [y] (- y 800))

(defn make-square [x y z color]
  (let [geometry (THREE.CubeGeometry. 200 200 50)
        material (THREE.MeshBasicMaterial. (clj->js {:color color, :wireframe true}))
        square (THREE.Mesh. geometry material)]
    (.translateX square (project-x x))
    (.translateY square (project-y y))
    (.translateZ square z)
    square))

(defn update-scene [scene world]
  (if (nil? (.getChildByName scene "catapult" false))
    (.add scene (doto (make-square 0 0 0 0xff0000)
                  (-> .-name (set! "catapult")))))
  (if (nil? (.getChildByName scene "iceberg" false))
    (.add scene (doto (make-square 0 0 0 0x0000ff)
                  (-> .-name (set! "iceberg")))))
  (if (nil? (.getChildByName scene "barrel" false))
    (.add scene (doto (make-square 0 0 0 0x00ff00)
                  (-> .-name (set! "barrel")))))
  (let [catapult (:catapult world)
        iceberg (:iceberg world)
        barrel (:barrel world)]
    (doto (.getChildByName scene "catapult" false)
      (-> .-position .-x (set! (project-x (:x catapult))))
      (-> .-position .-y (set! (project-y (:y catapult)))))
    (doto (.getChildByName scene "iceberg" false)
      (-> .-position .-x (set! (project-x (:x iceberg))))
      (-> .-position .-y (set! (project-y (:y iceberg)))))
    (if (:launched? barrel)
      (doto (.getChildByName scene "barrel" false)
        (-> .-position .-x (set! (project-x (:x barrel))))
        (-> .-position .-y (set! (project-y (:y barrel)))))
      (doto (.getChildByName scene "barrel" false)
        (-> .-position .-y (set! (project-y 1000000)))))))

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

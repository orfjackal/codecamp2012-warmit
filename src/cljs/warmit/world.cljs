(ns warmit.world)

; World updating
(defmulti update (fn [world [event value]] event))

(defmethod update :left [world [_ value]]
  world)

(defmethod update :right [world [_ value]]
  world)

(defmethod update :space [world [_ value]]
  world)

(defmethod update :time  [world [_ value]]
  world)

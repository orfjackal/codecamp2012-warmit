(ns warmit.world)

; World updating
(defmulti update (fn [world [event value]] event))

(defmethod update :left [world [_ value]]
  (.log js/console "left")
  (assoc-in world [:catapult] {:x 100 :y 100})
  )

(defmethod update :right [world [_ value]]
  world)

(defmethod update :space [world [_ value]]
  world)

(defmethod update :time  [world [_ value]]
  world)

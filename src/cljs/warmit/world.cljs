(ns warmit.world)

; World updating
(defmulti update (fn [world [event value]] event))

(defmethod update :left [world [_ value]]
  (assoc-in world [:catapult :speed-x] (if (= value :pressed) -20 0)))

(defmethod update :right [world [_ value]]
  (assoc-in world [:catapult :speed-x] (if (= value :pressed) 20 0)))

(defmethod update :space [world [_ value]]
  (assoc-in world [:catapult :is-firing] (= value :pressed)))

(defmethod update :dt  [world [_ value]]
  (-> world
    (update-in [:catapult :x] (partial + (-> world :catapult :speed-x)))
    (update-in [:catapult :force] (partial + (-> world :catapult :speed-force)))))

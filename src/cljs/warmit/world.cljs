(ns warmit.world)

(defn update-firing [world] 
  (if (and (= (-> world :catapult :speed-force) 0)
           (not= (-> world :catapult :force) 0))
    (do 
      (.log js/console (str "Firing " (-> world :catapult :force)))
      (assoc-in world [:catapult :force] 0))  
    world))

; World updating
(defmulti update (fn [world [event value]] event))

(defmethod update :left [world [_ value]]
  (assoc-in world [:catapult :speed-x] (if (= value :pressed) -2000 0)))

(defmethod update :right [world [_ value]]
  (assoc-in world [:catapult :speed-x] (if (= value :pressed) 2000 0)))

(defmethod update :space [world [_ value]]
  (assoc-in world [:catapult :speed-force] (if (= value :pressed) 100 0)))

(defn ms-to-s [ms] (* ms 0.001))

(defmethod update :dt  [world [_ value]]
  (-> (update-firing world)
    (update-in [:catapult :x] (partial + (* (ms-to-s value) (-> world :catapult :speed-x))))
    (update-in [:catapult :force] (partial + (* (ms-to-s value) (-> world :catapult :speed-force))))))

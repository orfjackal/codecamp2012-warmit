(ns warmit.world)

(defn random-y []
  (+ (rand-int 1000) 500))

(def initial-world 
  {:catapult {:speed-x 0, :x 2000 
              :speed-y 0, :y 100 
              :speed-force 0, :force 0}
   :barrel {:fire-time 0
            :fire-x 0
            :landing-time 0
            :launched? false}
   :iceberg {:speed-x 600, :x 0
             :speed-y 0,  :y (random-y)}})

(defn fire [world x force t] 
  (-> world 
    (assoc-in [:barrel :fire-x] (-> world :catapult :x))
    (assoc-in [:barrel :fire-time] t)
    (assoc-in [:barrel :launched?] true)))

(defn update-firing [world t] 
  (if (and (= (-> world :catapult :speed-force) 0)
           (not= (-> world :catapult :force) 0))
    (do 
      (fire world (-> world :catapult :x) (-> world :catapult :force) t)
      (assoc-in world [:catapult :force] 0))  
    world))

; World updating
(defmulti update (fn [world [event value & _]] event))

(defmethod update :left [world [_ value & _]]
  (assoc-in world [:catapult :speed-x] (if (= value :pressed) -2000 0)))

(defmethod update :right [world [_ value & _]]
  (assoc-in world [:catapult :speed-x] (if (= value :pressed) 2000 0)))

(defmethod update :space [world [_ value & _]]
  (assoc-in world [:catapult :speed-force] (if (= value :pressed) 100 0)))

(defn reposition-iceberg [world]
  (if (< 4000 (-> world :iceberg :x))
    (-> world
      (assoc-in [:iceberg :x] 0)
      (assoc-in [:iceberg :y] (random-y)))
    world))

(defn ms-to-s [ms] (* ms 0.001))

(defmethod update :dt  [world [_ value t]]
  (-> (update-firing world t)
    (update-in [:catapult :x] (partial + (* (ms-to-s value) (-> world :catapult :speed-x))))
    (update-in [:catapult :force] (partial + (* (ms-to-s value) (-> world :catapult :speed-force))))
    (update-in [:iceberg :x] (partial + (* (ms-to-s value) (-> world :iceberg :speed-x))))
    reposition-iceberg))

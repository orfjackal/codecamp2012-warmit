(ns cljs-hello.main)

(defn make-world []
  (let [geometry (THREE.CubeGeometry. 200 200 200)
        material (THREE.MeshBasicMaterial. (js* "{color: 0xff0000,
                                                wireframe: true}"))
        mesh (THREE.Mesh. geometry material)
        scene (doto (THREE.Scene.)
                (.add mesh))
        renderer (doto (THREE.CanvasRenderer.)
                   (.setSize (.-innerWidth js/window) (.-innerHeight js/window)))
        camera (doto (THREE.PerspectiveCamera. 75 (/ (.-innerWidth js/window)
                                                     (.-innerHeight js/window))
                                               1 10000)
                 (-> .-position .-z (set! 1000)))]
    (-> js/document .-body (.appendChild (.-domElement renderer)))
    {:mesh mesh
     :renderer renderer
     :scene scene
     :camera camera}))

(defn animate [{:keys [mesh renderer scene camera]
                :as world}]
  (js/requestAnimationFrame (partial animate world))
  (.render renderer scene camera))

(animate (make-world))

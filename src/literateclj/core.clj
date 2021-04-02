(ns literateclj.core
  (:refer-clojure :exclude [- + *])
  (:require [sicmutils.env :refer :all :as env]
            [sicmutils.mechanics.lagrange :as l]))

;; (bootstrap-repl!)

;; (defn T-pend [m l g ys]
;;   (fn [local]
;;     (let [t        (time local)
;;           theta    (coordinate local)
;;           thetadot (velocity local)]
;;       (let [vys (D ys)]
;;         (* 1/2 m
;;            (+ (square (* l thetadot))
;;               (square (vys t))
;;               (* 2 l (vys t) thetadot (sin theta))))))))
;; ;; => #'literateclj.core/T-pend

;; (defn V-pend [m l g ys]
;;   (fn [local]
;;     (let [t     (time local)
;;           theta (coordinate local)]
;;       (* m g (- (ys t) (* l (cos theta)))))))
;; ;; => #'literateclj.core/V-pend

;; (def L-pend (- T-pend V-pend))
;; ;; => #'literateclj.core/L-pend

;; (simplify
;;   (((Lagrange-equations
;;       (L-pend 'm 'l 'g (literal-function 'y_s)))
;;     (literal-function 'theta))
;;    't))
  (defn T-pend [m l g ys]
    ;; Function taking a local tuple
    ;; Destructuring the first three components
    (fn [[t theta thetadot]] ;; returning kinetic energy
      (let [vys (D ys)]
        (* 1/2 m
           (+ (square (* l thetadot))
              (square (vys t))
              (* 2 l (vys t) thetadot (sin theta)))))))

  ;; Potential energy is V, hence the name
  (defn V-pend [m l g ys]
    ;; Function taking a local tuple
    ;; Destructuring the first three components
    (fn [[t theta]]
      (* m g (- (ys t) (* l (cos theta))))))

  ;; The lagrangian!
(def L-pend (- T-pend V-pend))

(->TeX
  (simplify
    (((Lagrange-equations
        (L-pend 'm 'l 'g (literal-function 'y_s)))
      (literal-function 'theta))
     't)))
;; => "g\\,l\\,m\\,\\sin\\left(\\theta\\left(t\\right)\\right) + {l}^{2}\\,m\\,{D}^{2}\\theta\\left(t\\right) + l\\,m\\,\\sin\\left(\\theta\\left(t\\right)\\right)\\,{D}^{2}y_s\\left(t\\right)"
;; => (+ (* g l m (sin (theta t))) (* (expt l 2) m (((expt D 2) theta) t)) (* l m (sin (theta t)) (((expt D 2) y_s) t)))

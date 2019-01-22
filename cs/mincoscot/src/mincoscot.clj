(ns mincoscot
  (:require [clojure.pprint :refer [print-table]])
  (:import java.lang.Math))

(defn parse-double [v]
  (try (Double/parseDouble v) (catch Exception e 0.0)))

(defn read-user-input []
  (as-> [] v
    (do
      (println "Enter start:")
      (conj v (parse-double (read-line))))
    (do
      (println "Enter end:")
      (conj v (parse-double (read-line))))
    (do
      (println "Enter delta:")
      (conj v (parse-double (read-line))))))

(defn f [x]
  (let [n 61.0
        v (min
            (/ (/ 1.0 (Math/tan x)) n)
            (Math/log (- 1.0 (/ n (Math/cos x)))))]
    (if (Double/isNaN v) "Error" (format "%.6f" v))))

(defn -main []
  (->> (read-user-input)
       (apply range)
       (reduce #(conj %1 {:x (format "%.6f" %2) :min (f %2)}) [])
       (print-table [:x :min])))

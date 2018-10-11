(ns mincoscot
  (:require [clojure.pprint :refer [print-table]])
  (:import java.lang.Math))

(def ^:const n 61.0)
(def ^:const result-error "Error")
(def ^:const result-format "%.6f")

(defn parse-int [v]
  (try (Integer/parseInt v)
    (catch Exception e 0)))

(defn read-user-input []
  (as-> [] v
    (do
      (println "Enter start:")
      (conj v (parse-int (read-line))))
    (do
      (println "Enter end:")
      (conj v (parse-int (read-line))))
    (do
      (println "Enter delta:")
      (conj v (parse-int (read-line))))))

(defn mincoscot [x]
  (let [v (min
            (as-> x k
              (Math/tan k)
              (/ 1.0 k)
              (/ k n))
            (->> x
                 (Math/cos)
                 (/ n)
                 (- 1.0)
                 (Math/log)))]
    (if (Double/isNaN v)
      result-error
      (format result-format v))))

(defn -main []
  (->> (read-user-input)
       (apply range)
       (reduce #(conj %1 {:x %2 :min (mincoscot %2)}) [])
       (print-table [:x :min])))

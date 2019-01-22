(ns mincoscot-test
  (:require [mincoscot :refer :all]
            [clojure.test :refer :all]))

(deftest parse-int-test
  (is (= (parse-int "1") 1))
  (is (= (parse-int "") 0)))

(deftest mincoscot-test
  (is (= (mincoscot 0) "Error"))
  (is (= (mincoscot 1) "Error"))
  (is (= (mincoscot 2) "-0.007503"))
  (is (= (mincoscot 3) "-0.115004"))
  (is (= (mincoscot 4) "0.014159"))
  (is (= (mincoscot 5) "Error")))

(run-tests)

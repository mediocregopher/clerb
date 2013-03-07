(ns clerb.core-test
    (:use clojure.test
          clerb.core))


(deftest clerb-string-test
    (testing "clerb-string"
        (is (= (clerb-string "test") "test"))
        (is (= (clerb-string "test #(+ 1 2)# test") "test  test"))
        (is (= (clerb-string "test #(print (+ 1 2))# test") "test 3 test"))
        (is (= (clerb-string "test ##(+ 1 2)## test") "test 3 test"))
        ))

(deftest clerb-file-test
    (testing "clerb-file"
        (is (= (clerb-file "test/tpl/core.clerb") "this is a test\nblah\n3\nlol\n"))
        ))

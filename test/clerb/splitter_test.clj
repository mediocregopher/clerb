(ns clerb.splitter-test
  (:use clojure.test
        clerb.splitter))

(deftest two-split-on-left-test
  (testing "two-split-on-left"
    (is (= (two-split-on-left "" "whatever")
           [ "" "" ]))
    (is (= (two-split-on-left "test" "-")
           [ "test" "" ]))
    (is (= (two-split-on-left "test-test" "-")
           [ "test" "-test" ]))
    (is (= (two-split-on-left "test-test-test" "-")
           [ "test" "-test-test" ]))
    (is (= (two-split-on-left "test--test" "--")
           [ "test" "--test" ]))
    (is (= (two-split-on-left "test--test--test" "--")
           [ "test" "--test--test" ]))
    (is (= (two-split-on-left "test--test-test" "-")
           [ "test" "--test-test" ]))
    ))

(deftest two-split-on-right-test
  (testing "two-split-on-right"
    (is (= (two-split-on-right "" "whatever")
           [ "" "" ]))
    (is (= (two-split-on-right "test" "-")
           [ "test" "" ]))
    (is (= (two-split-on-right "test-test" "-")
           [ "test-" "test" ]))
    (is (= (two-split-on-right "test-test-test" "-")
           [ "test-" "test-test" ]))
    (is (= (two-split-on-right "test--test" "--")
           [ "test--" "test" ]))
    (is (= (two-split-on-right "test--test--test" "--")
           [ "test--" "test--test" ]))
    (is (= (two-split-on-right "test--test-test" "-")
           [ "test-" "-test-test" ]))
    ))

(deftest three-split-test
  (testing "three-split"
    (is (= (three-split "" "whatever" "whatever")
           [ "" "" "" ]))
    (is (= (three-split "test" "#(" ")#")
           [ "test" "" "" ]))
    (is (= (three-split "test #(test)# test" "#(" ")#")
           [ "test " "#(test)#" " test" ]))
    (is (= (three-split "test #( #(test) )# test" "#(" ")#")
           [ "test " "#( #(test) )#" " test" ]))
    (is (= (three-split "test #(test test" "#(" ")#")
           [ "test " "#(test test" "" ]))
    ))

(ns clerb.parser-test
    (:use clojure.test
          clerb.parser))

(deftest in-place-del-split-test
    (testing "in-place-del-split"
        (is (= (in-place-del-split "")
               '()))
        (is (= (in-place-del-split "##(test)## ")
               '("##(test)##" " ")))
        (is (= (in-place-del-split "ohai##(test##(ok)##wut")
               '("ohai" "##(test##(ok)##" "wut")))
        ))

(deftest del-split-test
    (testing "del-split"
        (is (= (del-split "")
               '()))
        (is (= (del-split "#(test)# ")
               '("#(test)#" " ")))
        (is (= (del-split "ohai#(test#(ok)#wut")
               '("ohai" "#(test#(ok)#" "wut")))
        ))

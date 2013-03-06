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

(deftest both-del-split-test
    (testing "both-del-split"
        (is (= (both-del-split "")
               '()))
        (is (= (both-del-split "test #(hi)# ##(test)## ##(#(wut)#)## ok")
               '("test " "#(hi)#" " " "##(test)##" " " "##(#(wut)#)##" " ok")))
        ))

(deftest -easy-subs-test
    (testing "-easy-subs-test"
        (is (= (-easy-subs "01234" 0 5)  "01234"))
        (is (= (-easy-subs "01234" 0 1)  "0"))
        (is (= (-easy-subs "01234" 0 4)  "0123"))
        (is (= (-easy-subs "01234" 0 6)  "01234"))
        (is (= (-easy-subs "01234" -1 1) "4"))
        (is (= (-easy-subs "01234" -4 2) "12"))
        (is (= (-easy-subs "01234" -1 4) "4"))
        ))

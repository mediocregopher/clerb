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

(deftest string-type-test
    (testing "string-type"
        (is (= (string-type "##(blah)##") :in-place))
        (is (= (string-type "##(blah")    :raw))
        (is (= (string-type "blah)##")    :raw))
        (is (= (string-type "#(blah)#")   :normal))
        (is (= (string-type "#(blah")     :raw))
        (is (= (string-type "blah)#")     :raw))
        (is (= (string-type "blah")       :raw))
        ))

(deftest remove-escape-test
    (testing "remove-escape"
        (is (= (remove-escape (wrap-string "test\\")) (wrap-string "test")))
        ))

(deftest try-escape-test
    (testing "try-escape"
        (is (= (try-escape (wrap-string "test\\") (wrap-string "test\\"))
               [ { :type :raw :text "test\\" } { :type :raw :text "test\\" } ]))
        (is (= (try-escape (wrap-string "test\\") (wrap-string "#(test)#"))
               [ { :type :raw :text "test" } { :type :raw :text "#(test)#" }]))
        (is (= (try-escape (wrap-string "test") (wrap-string "#(test)#"))
               [ { :type :raw :text "test" } { :type :normal :text "#(test)#" } ]))
        ))

(deftest code-format-test
    (testing "code-format"
        (is (= (code-format (wrap-string "test")) (wrap-string "test")))
        (is (= (code-format (wrap-string "#(test)#")) {:type :normal :text "(test)"}))
        (is (= (code-format (wrap-string "##(test)##")) {:type :in-place :text "(test)"}))
        ))

(deftest to-string-wrappers-test
    (testing "to-string-wrappers"
        (is (= (to-string-wrappers "test") (list (wrap-string "test"))))
        (is (= (to-string-wrappers "test #(test)# test")
                                   (list (wrap-string "test ")
                                         {:type :normal :text "(test)"}
                                         (wrap-string " test"))))
        (is (= (to-string-wrappers "test\\#(test)# test")
                                   (list (wrap-string "test")
                                         {:type :raw :text "#(test)#"}
                                         (wrap-string " test"))))
        (is (= (to-string-wrappers "test\\#(test)#\\#(test)# test")
                                   (list (wrap-string "test")
                                         {:type :raw :text "#(test)#"}
                                         (wrap-string "")
                                         {:type :raw :text "#(test)#"}
                                         (wrap-string " test"))))))

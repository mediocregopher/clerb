(ns clerb.executor-test
    (:use clojure.test
          clerb.executor
          clerb.parser))

(deftest execute-normal-test
    (testing "execute-normal"
        (is (= (execute-normal ":wut") ""))
        (is (= (execute-normal "(print (+ 1 2))") "3"))
        (is (= (execute-normal "(println (+ 1 2))") "3\n"))
        ))

(deftest execute-in-place-test
    (testing "execute-in-place"
        (is (= (execute-in-place ":wut") ":wut"))
        (is (= (execute-in-place "(+ 1 2)") "3"))
        (is (= (execute-in-place "(print (+ 1 2))") "3nil"))
        ))

(deftest execute-test
    (testing "execute-test"
        (is (= (execute (code-format (wrap-string "test"))) "test"))
        (is (= (execute (code-format (wrap-string "#(print (+ 1 2))#"))) "3"))
        (is (= (execute (code-format (wrap-string "##(+ 1 2)##"))) "3"))
        ))

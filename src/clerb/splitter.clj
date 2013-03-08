(ns clerb.splitter
    (:import java.util.StringTokenizer)
    (:require [clojure.string :as s]))

(defn split-to-vec
    "Given a string and a substring splits the string into an array using the
    substring as a delimiter. Returns occurences of the substring as well"
    [string splitter]
    (s/split string (re-pattern (str "(?=" splitter ")|(?<=" splitter ")"))))

(defn two-split-on-left
    "Given a string and a substring, tries to split the string at the
    first occurance of the given substring, leaving that substring to the right
    of the split point
    
    Ex. (two-split-on-left \"test-test\" \"-\") => [\"test\" \"-test\"]
        (two-split-on-left \"test-test-test\" \"-\") => [\"test\" \"-test-test\"]
        (two-split-on-left \"testtest\" \"-\") => [\"testtest\" \"\"]
    "
    [string splitter]
    (let [ regex (-> splitter
                     (s/replace "(" "\\(")
                     (s/replace ")" "\\)"))
           split-vec (split-to-vec string regex) ]
        (case (count split-vec)
            1 [(first split-vec) ""]
            [ (first split-vec)
              (apply str (rest split-vec)) ])))

(defn two-split-on-right
    "Given a string and a substring, tries to split the string at the
    first occurance of the given substring, leaving that substring to the left
    of the split point
    
    Ex. (two-split-on-right \"test-test\" \"-\") => [\"test-\" \"test\"]
        (two-split-on-right \"test-test-test\" \"-\") => [\"test-\" \"test-test\"]
        (two-split-on-right \"testtest\" \"-\") => [\"testtest\" \"\"]
    "
    [string splitter]
    (let [ regex (-> splitter
                     (s/replace "(" "\\(")
                     (s/replace ")" "\\)"))
           split-vec (split-to-vec string regex) ]
        (case (count split-vec)
            1 [(first split-vec) ""]
            [ (str (first split-vec) (second split-vec))
              (apply str (drop 2 split-vec)) ])))

(defn three-split
    "Given a string and two substrings, tries to split the string at the first
    occurence of the first substring, leaving that substring to the right of the
    split point. It then takes the right string and tries to split it on the second
    substring, this time leaving that second substring on the left of the split
    point.

    Ex. (three-split \"test [test] test\" \"[\" \"]\") => [\"test \" \"[test]\" \" test\"]
    "
    [string starter ender]
    (let [[ pre mid-full ] (two-split-on-left string starter)
          [ mid post ]     (two-split-on-right mid-full ender) ]
        [pre mid post]))

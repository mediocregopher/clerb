(ns clerb.splitter
    (:require [clojure.string :as s]))

(defn two-split-on-left
    "Given a string and a substring, tries to split the string at the
    first occurance of the given substring, leaving that substring to the right
    of the split point
    
    Ex. (two-split-on-left \"test-test\" \"-\") => [\"test\" \"-test\"]
        (two-split-on-left \"test-test-test\" \"-\") => [\"test\" \"-test-test\"]
        (two-split-on-left \"testtest\" \"-\") => [\"testtest\" \"\"]
        
    The behavior is a bit weird if the string ends in a splitter, but I'm lazy and
    that's dumb"
    [string splitter]
    (let [ regex (-> splitter
                     (s/replace "(" "\\(")
                     (s/replace ")" "\\)")
                     (re-pattern))
           split-vec (s/split string regex) ]
        (case (count split-vec)
            1 [(first split-vec) ""]
            2 [(first split-vec) (str splitter (second split-vec))]
            [ (first split-vec)
              (str splitter (reduce #(str %1 splitter %2) (rest split-vec))) ])))

(defn two-split-on-right
    "Given a string and a substring, tries to split the string at the
    first occurance of the given substring, leaving that substring to the left
    of the split point
    
    Ex. (two-split-on-right \"test-test\" \"-\") => [\"test-\" \"test\"]
        (two-split-on-right \"test-test-test\" \"-\") => [\"test-\" \"test-test\"]
        (two-split-on-right \"testtest\" \"-\") => [\"testtest\" \"\"]
        
    The behavior is a bit weird if the string ends in a splitter, but I'm lazy and
    that's dumb"
    [string splitter]
    (let [ regex (-> splitter
                     (s/replace "(" "\\(")
                     (s/replace ")" "\\)")
                     (re-pattern))
           split-vec (s/split string regex) ]
        (case (count split-vec)
            1 [(first split-vec) ""]
            2 [(str (first split-vec) splitter) (second split-vec)]
            [ (str (first split-vec) splitter)
              (reduce #(str %1 splitter %2) (rest split-vec)) ])))

(defn three-split
    "Given a string and two substrings, tries to split the string at the first
    occurence of the first substring, leaving that substring to the right of the
    split point. It then takes the right string and tries to split it on the second
    substring, this time leaving that second substring on the left of the split
    point.

    Ex. (three-split \"test [test] test\" \"[\" \"]\") => [\"test \" \"[test]\" \" test\"]

    The behavior is a bit weird if the string ends in a splitter, but I'm lazy and
    that's dumb"
    [string starter ender]
    (let [[ pre mid-full ] (two-split-on-left string starter)
          [ mid post ]     (two-split-on-right mid-full ender) ]
        [pre mid post]))
    

;(defn clerb-string
;    [to-process]
;    (let [charseq (seq to-process)]
;        (reduce (fn [ acc ch ]
;            (let [ [ prev 

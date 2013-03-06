(ns clerb.parser
    (:require [clojure.string :as s])
    (:use clerb.splitter))

(defn -del-split
    [string a b]
    (let [[ pre mid post ] (three-split string a b)]
        (if (empty? post)
            [pre mid]
            [pre mid (-del-split post a b)])))

(defn in-place-del-split
    "Goes through a string and splits it into a sequence of chunks
    based on opening and closing ##( )## sections

    Ex. (in-place-del-split \"test ##(test)## test\") => (\"test\" \"##(test)##\" \"test\")"
    [string]
    (remove empty?
        (flatten (-del-split string "##(" ")##"))))

(defn del-split
    "Goes through a string and splits it into a sequence of chunks
    based on opening and closing #( )# sections

    Ex. (del-split \"test #(test)# test\") => (\"test\" \"#(test)#\" \"test\")"
    [string]
    (remove empty?
        (flatten (-del-split string "#(" ")#"))))

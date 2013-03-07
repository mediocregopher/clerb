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

(defn -easy-subs
    "Because subs freaks out if you give it a range larger than the size of the string, and doesn't
    work the way every other substring function in the rest of the freakin world. Accepts negative
    indices"
    [string beg len]
    (let [strcnt (count string)
          start (if (neg? beg) (+ strcnt beg) beg)
          endtmp (+ start len)
          end (if (> endtmp strcnt) strcnt endtmp)]
        (subs string start end)))

(defn both-del-split
    "Splits a string on both ##( and #( using in-place-del-split and del-split"
    [string]
    (->> string
         (in-place-del-split)
         (map #(if (= "##(" (-easy-subs % 0 3)) %
                   (del-split %)))
         (flatten)))

(defn string-type
    "Returns :in-place, :normal, or :raw depending on what the given string looks like"
    [string]
    (cond (and (= "##(" (-easy-subs string  0 3))
               (= ")##" (-easy-subs string -3 3))) :in-place
          (and (= "#("  (-easy-subs string  0 2))
               (= ")#"  (-easy-subs string -2 2))) :normal
          :else :raw))

;(defn escapes-next?
;    "Given a string wrapper, returns if it escapes the next string-wrapper in the seq"
;    [string-wrapper]
;    (

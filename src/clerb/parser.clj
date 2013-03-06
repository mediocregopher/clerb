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
    [string]
    (remove empty?
        (flatten (-del-split string "##(" ")##"))))

(defn del-split
    [string]
    (remove empty?
        (flatten (-del-split string "#(" ")#"))))

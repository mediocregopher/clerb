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
          start (if (neg? beg) (max (+ strcnt beg) 0) beg)
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

(defn wrap-string
    "Given a string, wraps it into a string wrapper"
    [string]
    { :type (string-type string)
      :text string })

(defn escapes-next?
    "Given a string wrapper, returns if it escapes the next string-wrapper in the seq"
    [string-wrapper]
    (and (= (string-wrapper :type) :raw)
         (= (-easy-subs (string-wrapper :text) -1 1) "\\")))

(defn remove-escape
    "Given a string wrapper removes the backslash from the end. Assumes there is one"
    [string-wrapper]
    (let [oldtext (string-wrapper :text)]
        (assoc string-wrapper :text
            (-easy-subs oldtext 0 (dec (count oldtext))))))

(defn try-escape
    "Given two string-wrappers, returns a vector of the same two, except if the first
    escapes the second it has that backslash removed and the second one becomes a raw
    type. If the second is already a raw immediately return"
    [string-wrapper-a string-wrapper-b]
    (if (= :raw (string-wrapper-b :type)) [string-wrapper-a string-wrapper-b]
        (if (escapes-next? string-wrapper-a)
            [ (remove-escape string-wrapper-a)
              (assoc string-wrapper-b :type :raw) ]
            [ string-wrapper-a string-wrapper-b ])))

(defn escape-all
    "Given a seq of string-wrappers returns another seq with all escapes properly handled"
    [string-wrappers]
    (->> (concat string-wrappers (list (wrap-string "")))
         (reductions
            (fn [prev-pair curr]
                (let [[ _ prev ] prev-pair]
                    (try-escape prev curr)))
            [ :whatever (wrap-string "") ])
         (map #(let [[curr _] %] curr))
         (drop 2)))

(defn code-format
    "Removes #'s from code wrappers in string-wrapper"
    [string-wrapper]
    (let [strtext (string-wrapper :text)
          strtype (string-wrapper :type)]
        (case strtype
            :raw string-wrapper
            :normal   (assoc string-wrapper :text (-> strtext
                                                    (-easy-subs 0 (- (count strtext) 1))
                                                    (-easy-subs 1 (count strtext))))
            :in-place (assoc string-wrapper :text (-> strtext
                                                    (-easy-subs 0 (- (count strtext) 2))
                                                    (-easy-subs 2 (count strtext)))))))


(defn to-string-wrappers
    "Given a full string, splits it, wraps the splitted strings, and handles all escaping,
    returning a seq of string-wrappers"
    [string]
    (->> string
        (both-del-split)
        (map wrap-string)
        (escape-all)
        (map code-format)))

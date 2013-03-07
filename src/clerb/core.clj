(ns clerb.core
    (:require [clerb.executor :as e]
              [clerb.parser   :as p]))

(defn clerb-string
    "Given a string, parses it as a clerb template and returns the parsed result. See README.md for
    details on how parsing works"
    [string]
    (->> string
        (p/to-string-wrappers)
        (map e/execute)
        (apply str)))

(defn clerb-file
    "Given a file name, reads that file and parses it as a clerb template, returning the parsed
    result. See README.md for details on how parsing works"
    [filename]
    (clerb-string (slurp filename)))

(ns clerb.executor)

(defn execute-normal
    "Given a string to execute, returns a string of all the output captured on stdout during
    execution"
    [ex-string]
    (with-out-str (load-string ex-string)))

(defn execute-in-place
    "Given a string to execute, returns a string of al the output captured on stdout during
    execution. Differs from execute-normal in that it wraps the whole thing in a (print), so
    the return from the execution is always printed"
    [ex-string]
    (execute-normal (str "(print " ex-string ")")))

(defn execute
    "Given a string wrapper which has been run through code-format, executes it
    if necessary and returns the appropriate text"
    [string-wrapper-formatted]
    (let [ strtype (string-wrapper-formatted :type)
           strtext (string-wrapper-formatted :text) ]
        (case strtype
            :raw strtext
            :normal (execute-normal strtext)
            :in-place (execute-in-place strtext))))

clerb
=====

It's like ERB, but in clojure!

Usage
-----

Clerb can be used both as a standalone and as a library. The standalone takes in the name of a file whose
filename ends in ```.clerb```, and outputs to a file with the same name sans the ```.clerb``` extension.

The library can be included in your ```project.clj``` like usual, and included using ```(import 'clerb.core)```.
There are two public methods: ```clerb-string/1```, which takes in a string, and ```clerb-file/1```, which takes
in the name of a file as a string. Both output the string representation of the parsed clerb template that was passed
in.

Format
------

Clerb templates are similar to ERB templates in usage. Given a normal text file, clerb will go through the text
file and find the pattern ```#( ... )#```. If found it will process whatever's enclosed using ```eval```, with any
stdout output being put in-place. For example

```
This is a normal text file

1 + 2 = #(print (+ 1 2))#

Basic math!
```

The ```#( ... )#``` pattern doesn't output the return value from the ```eval```, so you can use it for side-effects:

```
#(require 'some.library)#

This library does: #(print (some-library-method :foo :bar :baz))#

```

You can use ```##( ... )##``` to implicitely wrap the contents in a ```print``` statement.

```
1 + 2 = ##(+ 1 2)##
```

If you want to not have clerb process ```#( ... )#``` or ```##( ... )##``` patterns as clojure statements, prefix them
with a backslash: ```\#( ... )#``` or ```\##( ... )##```

(ns jepordy.server.main)

(range 10)

(->> (range 10)
     (filter odd?))

(def m {"a", "1", "b", 2})

(assoc m "c" 42)

(type "a")

(.getClass "a")

(def a (atom {"a" 1}))

(def time1 (deref a))


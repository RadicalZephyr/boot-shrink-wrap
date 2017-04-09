(ns radicalzephyr.boot-shrink-wrap
  {:boot/export-tasks true}
  (:require [boot.core :as core]
            [boot.util  :as util]
            [clojure.string :as str]))

(defonce bin-template
  "#!/bin/sh
   exec boot %s\n")

(defn- remove-boot-prefix [cmd]
  (let [idx (.indexOf cmd (int \space))]
    (.substring cmd (inc idx))))

(defonce shrink-wrap-pattern
  #"shrink-wrap( +--?[np]a?[^- ]* +[^- ]+)* *")

(defn- remove-shrink-wrap [cmd]
  (str/replace cmd shrink-wrap-pattern))

(defn- clean-up [cmd]
  (-> cmd
      remove-boot-prefix
      remove-shrink-wrap))

(core/deftask shrink-wrap
  "Packages a boot invocation into a wrapper shell-script.

  For the best experience, set the path shrink-wrap outputs scripts to
  in the task-options in your ~/.boot/profile.boot, like so:

  (task-options!
    shrink-wrap {:path \"/home/me/.local/bin\"}"
  [n name    NAME str "The name of the script file"
   p path    PATH str "Path that the script file will be written to"]
  (fn [_next]
    (fn [_fs]
      (let [command-line (System/getProperty "sun.java.command")
            script (format bin-template (clean-up command-line))
            ]

        )
      )))

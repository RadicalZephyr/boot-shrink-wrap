(ns radicalzephyr.boot-shrink-wrap
  {:boot/export-tasks true}
  (:require [boot.core :as core]
            [boot.util  :as util]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defonce bin-template
  (str "#!/bin/sh\n"
       "exec boot %s\n"))

(defn- remove-boot-prefix [cmd]
  (let [idx (.indexOf cmd (int \space))]
    (.substring cmd (inc idx))))

(def base-pattern
  "shrink-wrap( +--?[np]a?[^- ]* +[^- ]+)* *")

(def shrink-wrap-pattern
  (re-pattern base-pattern))

(def next-task-name-pattern
  (re-pattern (str base-pattern "([^ ]+)")))

(defn- default-name [cmd]
  (if-let [match (re-find next-task-name-pattern cmd)]
    (nth match 2)
    "boot.out"))

(defn- remove-shrink-wrap [cmd]
  (str/replace cmd shrink-wrap-pattern ""))

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
  (let [command-line (System/getProperty "sun.java.command")
        name (or name (default-name command-line))
        outfile (io/file path name)
        script (format bin-template (clean-up command-line))]
    (spit outfile script)
    (constantly (constantly nil))))

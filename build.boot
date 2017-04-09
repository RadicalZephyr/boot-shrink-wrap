(def project 'radicalzephyr/boot-shrink-wrap)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.8.0"]
                            [adzerk/boot-test "RELEASE" :scope "test"]])

(task-options!
 pom {:project     project
      :version     version
      :description "Create executable wrappers from a boot pipeline."
      :url         "https://github.com/RadicalZephyr/boot-shrink-wrap"
      :scm         {:url "https://github.com/RadicalZephyr/boot-shrink-wrap"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build
  "Build and install the project locally."
  []
  (comp (pom) (jar) (install)))

(require '[adzerk.boot-test :refer [test]])

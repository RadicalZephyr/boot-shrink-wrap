(ns radicalzephyr.boot-shrink-wrap-test
  (:require [clojure.test :refer :all]
            [radicalzephyr.boot-shrink-wrap :refer :all]))

(deftest shrink-wrap-pattern-test
  (testing "non-matching invocations"
    (is (= nil
           (re-find shrink-wrap-pattern
                    "")))
    (is (= nil
           (re-find shrink-wrap-pattern
                    "abc")))
    (is (= nil
           (re-find shrink-wrap-pattern
                    "boot -d things --name stuff"))))

  (testing "matching invocations"
    (testing "no arguments"
      (is (= ["shrink-wrap" nil]
             (re-find shrink-wrap-pattern
                      "shrink-wrap")))

      (testing "before shenanigans"
        (is (= ["shrink-wrap" nil]
               (re-find shrink-wrap-pattern
                        "boot -d thingy/stuff shrink-wrap")))
        (is (= ["shrink-wrap" nil]
               (re-find shrink-wrap-pattern
                        "boot -d thingy/stuff --name stuff shrink-wrap")))
        (is (= ["shrink-wrap" nil]
               (re-find shrink-wrap-pattern
                        "boot --name stuff -d thingy/stuff shrink-wrap"))))

      (testing "before and after weirdness"
        (is (= ["shrink-wrap " nil]
               (re-find shrink-wrap-pattern
                        "boot -d thingy/stuff shrink-wrap new --name stuff -t things")))
        (is (= ["shrink-wrap " nil]
               (re-find shrink-wrap-pattern
                        "boot --damn thingy/stuff shrink-wrap new -t things --name stuff")))))

    (testing "one short argument"
      (is (= ["shrink-wrap -n crabs " " -n crabs"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap -n crabs new")))
      (is (= ["shrink-wrap -p for/you " " -p for/you"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap -p for/you new"))))

    (testing "two short arguments"
      (is (= ["shrink-wrap -n crabs -p for/you " " -p for/you"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap -n crabs -p for/you new")))
      (is (= ["shrink-wrap -p for/you -n crabs " " -n crabs"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap -p for/you -n crabs new"))))

    (testing "one long argument"
      (is (= ["shrink-wrap --path for/you " " --path for/you"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap --path for/you new")))
      (is (= ["shrink-wrap --name crabs " " --name crabs"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap --name crabs new"))))

    (testing " two long arguments"
      (is (= ["shrink-wrap --name crabs --path for/you" " --path for/you"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap --name crabs --path for/you")))
      (is (= ["shrink-wrap --path for/you --name crabs" " --name crabs"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap --path for/you --name crabs"))))

    (testing "one short, one long"
      (is (= ["shrink-wrap --path things -n bird" " -n bird"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap --path things -n bird")))
      (is (= ["shrink-wrap -n bird --path things" " --path things"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap -n bird --path things")))
      (is (= ["shrink-wrap --name things -p bird" " -p bird"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap --name things -p bird")))
      (is (= ["shrink-wrap -p bird --name things" " --name things"]
             (re-find shrink-wrap-pattern
                      "shrink-wrap -p bird --name things"))))))

(deftest next-task-name-pattern-test
  (testing "gets just the name of the task directly following shrink-wrap"
    (is (= ["shrink-wrap -p bird --name things target" " --name things" "target"]
           (re-find next-task-name-pattern
                    "shrink-wrap -p bird --name things target")))
    (is (= ["shrink-wrap -p bird --name things foobar" " --name things" "foobar"]
           (re-find next-task-name-pattern
                    "shrink-wrap -p bird --name things foobar")))))

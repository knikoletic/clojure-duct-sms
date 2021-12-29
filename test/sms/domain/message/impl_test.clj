(ns sms.domain.message.impl-test
  (:require [clojure.test :as t]
            [sms.domain.message.impl :refer
             [map->MessageServiceImpl]]
            [sms.domain.message.sender :as sender]
            [sms.domain.message.service :as service])
  (:import [java.util UUID]))

(def ^:private id (UUID/randomUUID))

(def ^:private expected-message
  {:id id
   :receiver "+420700000000"
   :text "Hej Clojure!"})

(t/deftest send!-test
  (t/testing "should successfully send a message"
    (let [conf {:sender
                (reify sender/Sender
                  (send! [_ message]
                    (t/is (= (dissoc expected-message :id)
                             message))
                    (assoc message :id id)))}
          request (select-keys expected-message [:receiver :text])
          result (service/send! (map->MessageServiceImpl conf)
                                request)]
      (t/is (= expected-message result))))

  (t/testing "should return an error"
    (let [conf {:sender
                (reify sender/Sender
                  (send! [_ message]
                    {:error :unexpected-error}))}
          request (select-keys expected-message [:receiver :text])
          result (service/send! (map->MessageServiceImpl conf)
                                request)]
      (t/is (= {:error :unexpected-error} result)))))

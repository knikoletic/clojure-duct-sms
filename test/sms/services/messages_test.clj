(ns sms.services.messages-test
  (:require [clojure.test :as t]
            [integrant.core :as ig]
            [ring.mock.request :as mock]
            [sms.domain.message.service :refer [MessageService]]
            [sms.handler.api])
  (:import [java.util UUID]))

(defn- send-message-api
  [conf params]
  (let [handler (ig/init-key :sms.handler/api conf)]
    (-> :post
        (mock/request "/messages")
        (assoc :body-params params)
        handler)))

(def ^:private expected-message
  {:id (UUID/randomUUID)
   :receiver "+420700000000"
   :text "Hej Clojure!"})

(t/deftest send-message-test
  (t/testing "should successfully send a message"
    (let [conf {:message-service
                (reify MessageService
                  (send! [_ message]
                    (t/is (= (select-keys expected-message
                                          [:receiver :text])
                             message))
                    expected-message))}
          params (select-keys expected-message [:receiver :text])
          {:keys [body status]} (send-message-api conf params)]
      (t/is (= expected-message body))
      (t/is (= 201 status))))))


  (t/testing "should return 503, Sender failed"
    (let [conf {:message-service
                (reify MessageService
                  (send! [_ message]
                    {:error :unexpected-error}))}
          params (select-keys expected-message [:receiver :text])
          {:keys [status]} (send-message-api conf params)]
      (t/is (= 503 status)))))


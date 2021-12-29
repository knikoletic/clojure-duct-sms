(ns sms.handler.api.message
  (:require [ring.util.response :as response]
            [sms.domain.message.service :as service]))

(defn send! [message-service req]
  (let [result (service/send! message-service
                              (select-keys req
                                           [:receiver :text]))]
    (if (= {:error :unexpected-error} result)
      (response/status {} 503)
      (response/created (format "/messages/%s" (:id result))
                        result))))

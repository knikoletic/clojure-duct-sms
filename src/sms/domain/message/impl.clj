(ns sms.domain.message.impl
  (:require [sms.domain.message.sender :as sender]
            [sms.domain.message.service :refer [MessageService]]))

(defrecord MessageServiceImpl [sender]
  MessageService
  (send! [_ request]
    (sender/send! sender request)))

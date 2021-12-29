(ns sms.domain.message.service)

(defprotocol MessageService
  (send! [this message]))

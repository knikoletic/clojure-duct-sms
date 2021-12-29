(ns sms.domain.message.sender)

(defprotocol Sender
  (send! [this message]))

(ns sms-dev.boundaries.sms-gateway-mock
  (:require [integrant.core :as ig]
            [sms.domain.message.sender :refer [Sender]])
  (:import [java.util UUID]))

(defrecord SmsGatewayMock []
  Sender
  (send! [_ message]
    (assoc message :id (UUID/randomUUID))))

(defmethod ig/init-key :sms-dev.boundaries/sms-gateway-mock
  [_ opts]
  (map->SmsGatewayMock opts))

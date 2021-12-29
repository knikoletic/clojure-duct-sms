(ns sms.services.message
  (:require [integrant.core :as ig]
            [sms.domain.message.impl :refer
             [map->MessageServiceImpl]]))

(defmethod ig/init-key ::service
  [_ opts]
  (map->MessageServiceImpl opts))

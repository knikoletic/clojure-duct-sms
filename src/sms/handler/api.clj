(ns sms.handler.api
  (:require [compojure.core :as compojure]
            [integrant.core :as ig]
            [sms.handler.api.message :as message]))

(defmethod ig/init-key :sms.handler/api
  [_ {:keys [message-service]}]
  (compojure/context "/messages" []
    (compojure/POST "/" [:as req]
      (message/send! message-service (:body-params req)))))

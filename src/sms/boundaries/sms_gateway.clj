(ns sms.boundaries.sms-gateway
  (:require [duct.logger :as logger]
            [integrant.core :as ig]
            [jsonista.core :as jsonista]
            [org.httpkit.client :as http]
            [sms.domain.message.sender :refer [Sender]]))

(def mime-type "application/json")

(def default-headers
  {"Accept" mime-type
   "Content-Type" mime-type})

(defrecord SmsGateway [logger url]
  Sender
  (send! [_ message]
    (let [{:keys [body status] :as response}
          (http/post url
                     {:as :text
                      :body (jsonista/write-value-as-string message)
                      :headers default-headers})]
      (case status
        (200 201) (jsonista/read-value body)
        (do
          (logger/log logger :error response)
          {:error :unexpected-error})))))

(defmethod ig/init-key :sms.boundaries/sms-gateway
  [_ opts]
  (map->SmsGateway opts))

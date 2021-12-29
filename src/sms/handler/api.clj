(ns sms.handler.api
  (:require [compojure.core :refer compojure]
            [integrant.core :as ig]))

(defmethod ig/init-key :sms.handler/api [_ options]
  (compojure/context "/messages" []
    (compojure/GET "/" []
      {:body {:example "data"}})))

(ns server
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring-range-middleware.core :refer [wrap-range-header]]))

(defn handler [request]
  (let [uri (:uri request)]
    (case uri
      "/cog" (-> (response/file-response "./TCI.tif")
                 (response/content-type "image/tiff"))
      (ring.util.response/not-found "Not Found"))))

(defn -main [& args]
  (jetty/run-jetty
   (-> handler
       (wrap-cors :access-control-allow-origin #".*"
                   :access-control-allow-methods [:get :put :post :delete]
                   :access-control-allow-headers ["Range"])
       wrap-range-header)
   {:port 3000 :join? false}))

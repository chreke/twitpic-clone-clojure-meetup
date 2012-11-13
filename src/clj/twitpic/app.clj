(ns twitpic.app
  (:use (compojure handler
         [core :only (GET POST defroutes)]))
  (:require compojure.route
            [ring.adapter.jetty :as jetty]
            [net.cgrand.enlive-html :as en]
            [ring.util.response :as response]
            [clojure.pprint :as pp]))

(defonce pics (atom {}))

(en/deftemplate homepage
  (en/html-resource "public/index.html")
  [request]
  [:#listing :li] (en/clone-for [[id pic] @pics]
                                [:a] (comp
                                      (en/content (format "%s : %s" id pic))
                                      (en/set-attr :href (str \/ id)))))

(defn redirect
  [id]
  (response/redirect (@pics id)))

(defroutes app*
  (compojure.route/resources "/")
  (GET "/" request (homepage request))
  ;;(POST "/save" request)
  (GET "/:id" [id] (redirect id)))

(def app (compojure.handler/site app*))
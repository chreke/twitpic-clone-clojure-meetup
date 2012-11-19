(ns twitpic.app
  (:use (compojure handler
                   [core :only (GET POST defroutes)]
                   )
        )
  (:require compojure.route
            [aws.sdk.s3 :as s3]
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

(defn random-char []
  (rand-nth "qwertyuioopasdfghjklzxcvbnmm1234567890"))

;;

(defn random-string [strl]
  (apply str (take strl (repeatedly random-char)) ))

(defn save-pic
  "store the key/val in the pics atom"
  [r]

  )

(def credentials
  {:access-key "AKIAIKYCC6GRTOE5IQNA"
   :secret-key "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"})

(def bucket "https://s3-eu-west-1.amazonaws.com/swarm/%s")

(defn upload-file* [path name]
  (s3/put-object credentials
                 "swarm"
                 name
                 path
                 {:content-type "image/jpeg"}))

(defn upload-file [request]
  (let [file (:tempfile ((:multipart-params request) "url"))
        target (random-string 10)]
    (upload-file* file target)
    target))

(defn random-insult []
   (let [insults ["dude" "bro" "broseph" "man" "hen"]]
     (rand-nth insults)))

(defroutes app*
  (compojure.route/resources "/")
  (GET "/" request (homepage request))
  (POST "/save" request  (let [file (format bucket (upload-file request))] (str "<img src='" file
                                           "'/><p>Uploaded to: <a href='" file "'>" file "</a> " (random-insult) "</p>")))
  (GET "/:id" [id] (redirect id)))

; Cool trick: (use :reload 'twitpic.app)

(def app (compojure.handler/site app*))
#!/usr/bin/env bb

(defn get-config [& ks]
  (-> "config.edn" (slurp) (edn/read-string) (get-in ks)))

(defn send-to-mm! [body]
  (curl/post (get-config :mattermost :hook-url)
             {:headers {"Content-Type" "application/json"}
              :body (json/generate-string body)}))

(defn send-message! [text]
  (let [config (get-config :mattermost)]
    (send-to-mm! {:username (:username config)
                  :icon_url (:icon config)
                  :text     text})))

(defn send-message-attachment! [{:keys [text] :as attachment}]
  (let [config (get-config :mattermost)]
    (send-to-mm! {:username   (:username config)
                  :icon_url   (:icon config)
                  :attachments [(assoc attachment :fallback text)]})))

(defn main [title body]
  (send-message-attachment! {:color "#00ff00" :title title :text body}))

(main "THIS IS A TEST" "Hello World!")

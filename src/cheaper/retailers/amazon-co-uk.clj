(ns cheaper.retailers.amazon-co-uk
   (:require [net.cgrand.enlive-html :as html]
             [clojure.string :as str])
  (:use cheaper.core))

(deftype parser [url]
  URLScraper
  (product-title [this] "title")
  (product-price [this]
                 (read-string
                 (re-find
                  (re-pattern "[0-9]+.[0-9]{2}")
                  (first (map html/text
                              (html/select
                               (fetch-url (.url this))
                               [:b.priceLarge html/text]))))))
  (product-token [this] "token")
  (product-url   [this] (.url this)))

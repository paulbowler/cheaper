(ns cheaper.retailers.amazon-co-uk
   (:require [net.cgrand.enlive-html :as html]
             [clojure.string :as str])
   (:use cheaper.core))

(def ^:dynamic *domain-uri* "http://www.amazon.co.uk/product/dp/")

(deftype parser [url]
  URLScraper
  (product-title [this]
                 (str/trim
                   (first
                    (html/select (fetch-url (.url this)) [:h1.parseasinTitle :span :span html/text]))))
  (product-price [this]
                 (read-string
                   (re-find
                     (re-pattern "[0-9]+.[0-9]{2}")
                       (first
                         (map html/text (html/select (fetch-url (.url this)) [:b.priceLarge html/text]))))))
  (product-token [this] (-> "B[A-Z0-9]+" re-pattern (re-find (.url this))))
  (product-url   [this] (str *domain-uri* (.product-token this) "/")))

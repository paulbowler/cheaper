(ns cheaper.core
   (:require [net.cgrand.enlive-html :as html]
             [clojure.string :as str]))

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn domain-name [url]
  (-> "^(?:http|https|ftp)://(?:www.)([a-zA-Z0-9.-]+)"
    re-pattern
   (re-find url)
    second
   (str/replace #"\." "-")
 ))

(defprotocol URLScraper
  "A protocol parsing product information from a web page."
  (product-title [url] "Parses the product title from the web page.")
  (product-price [url] "Parses the product price from the web page.")
  (product-token [url] "Parses the product token or unique ID from the web page.")
  (product-url   [url] "Creates an affiliate URL for accessing the product page in the future."))

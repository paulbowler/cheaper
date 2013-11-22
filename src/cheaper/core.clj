(ns cheaper.core
   (:require [net.cgrand.enlive-html :as html]
             [clojure.string :as str]))

(def ^:dynamic *base-url* "http://www.amazon.co.uk/gp/product/B00EORACPI/")

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn price []
  (first (map html/text (html/select (fetch-url *base-url*) [:b.priceLarge html/text]))))

(re-find (re-pattern "[0-9]+.[0-9]{2}") (price))

(resolve (symbol "cheaper.core/price"))

(re-find
   (re-pattern "^(?:http|https|ftp)://(?:www.)([a-zA-Z0-9.-]+)/([a-zA-Z0-9-]+)/dp/([A-Z0-9]+)/") *base-url*)

(-> "^(?:http|https|ftp)://(?:www.)([a-zA-Z0-9.-]+)"
    re-pattern
   (re-find *base-url*)
    second
   (str/replace #"\." "-")
    keyword
 )

(defprotocol URLScraper
  "A protocol parsing product information from a web page."
  (product-title [this] "Parses the product title from the web page.")
  (product-price [this] "Parses the product price from the web page.")
  (product-token [this] "Parses the product token or unique ID from the web page.")
  (product-url   [this] "Creates an affiliate URL for accessing the product page in the future."))

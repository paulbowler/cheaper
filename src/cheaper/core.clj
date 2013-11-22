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
  (product-title [url] "Parses the product title from the web page.")
  (product-price [url] "Parses the product price from the web page.")
  (product-token [url] "Parses the product token or unique ID from the web page.")
  (product-url   [url] "Creates an affiliate URL for accessing the product page in the future."))

(deftype amazon-co-uk [url]
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

(def amazon-product (->amazon-co-uk "http://www.amazon.co.uk/gp/product/B00EORACPI/"))

(.product-price amazon-product)

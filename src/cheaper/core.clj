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

(defn scrape [url]
  ((resolve (symbol (str "cheaper.retailers." (domain-name url) "/->parser"))) url))

(def url "http://www.amazon.co.uk/gp/product/B0083PWAWU/ref=amb_link_178443707_1?ie=UTF8&nav_sdd=aps&pf_rd_m=A3P5ROKL5A1OLE&pf_rd_s=center-1&pf_rd_r=14J0KMBE395JZ9X0PV27&pf_rd_t=101&pf_rd_p=448228307&pf_rd_i=468294")

(.product-title (scrape url))
(.product-price (scrape url))
(.product-token (scrape url))
(.product-url   (scrape url))

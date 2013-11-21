(ns cheaper.core
   (:require [net.cgrand.enlive-html :as html]
             [clojure.string :as str]))

(def ^:dynamic *base-url* "http://www.amazon.co.uk/Doctor-Character-Building-Construction-Playset/dp/B004W1T8YW/ref=pd_ys_sf_s_468292_b6_3_p?ie=UTF8&refRID=1W16R8191GXJJSX6KGF5")

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn price []
  (map html/text (html/select (fetch-url *base-url*) [:b.priceLarge html/text])))

(price)

(resolve (symbol "cheaper.core/price"))

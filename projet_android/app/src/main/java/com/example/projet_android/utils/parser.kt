package com.example.projet_android.utils

import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory


class Parser {
    companion object {
        fun xmlToDocument(uri : String) : Document? {
            return try {
                val documentFactory = DocumentBuilderFactory.newInstance()
                val documentBuilder = documentFactory.newDocumentBuilder()
                documentBuilder.parse(uri)

            }catch(e : Exception){
                e.printStackTrace()
                null
            }
        }

        // Read elements
        // rss > title - atom:link - description - pubDate
        // item > title > link > description - pubDate - media-content(url)
        fun analyseRssXml(document: Document?){
            if(document == null) return
            // rss feed

            // items
            val items = document.getElementsByTagName("item")
            for(i in 0 until items.length){
                val item = items.item(i) as Element
                // title
                val title = item.getElementsByTagName("title").item(0)
                //println(title?.textContent)

                // link
                val link = item.getElementsByTagName("link").item(0)
                //println(link?.textContent)

                // description
                val description = item.getElementsByTagName("description").item(0)
                //println(description?.textContent)

                // publication date
                val pubDate = item.getElementsByTagName("pubDate").item(0)
                //println(pubDate?.textContent)
            }
        }
    }
}

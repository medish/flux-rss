package com.example.projet_android.utils

import com.example.projet_android.entities.Info
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.IllegalArgumentException
import javax.xml.parsers.DocumentBuilderFactory


class RssXmlParser{
    companion object {
        fun xmlToDocument(istream : InputStream?) : Document? {
            return try {
                val documentFactory = DocumentBuilderFactory.newInstance()
                val documentBuilder = documentFactory.newDocumentBuilder()
                documentBuilder.parse(istream)

            }catch(e : Exception){
                e.printStackTrace()
                null
            }
        }

        // Read elements
        // rss > title - atom:link - description - pubDate
        // item > title > link > description - pubDate - media-content(url)
        fun analyseRssXml(document: Document?): Array<Info> {
            // TODO("check null for item's elements")
            // TODO("Add pubDate to info's table")
            if(document == null) throw FileNotFoundException("Document not found")

            val infoList = mutableListOf<Info>()
            // rss feed
            //val atomLink = document.getElementsByTagName("atom:link").item(0)
            //val rssLink = atomLink.attributes.getNamedItem("href")

            // items
            val items = document.getElementsByTagName("item")
            for(i in 0 until items.length){
                val item = items.item(i) as Element
                val title = item.getElementsByTagName("title").item(0)

                val link = item.getElementsByTagName("link").item(0)

                val description = item.getElementsByTagName("description").item(0)

                val pubDate = item.getElementsByTagName("pubDate").item(0)

                val info = Info(title.textContent, description.textContent, link.textContent, true, 1)
                infoList.add(info)
            }

            return infoList.toTypedArray()
        }
    }
}

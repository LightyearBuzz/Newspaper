package com.pelican.newspaper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * обрабатывает данные полученные из входящего потока с помощью DOM.
 * */
public class Processing {

    /*с помощью DOM обрабатывает XML, возвращает обработанный список новостей*/
    public static ArrayList<RssItem> getRssItems(InputStream is) {

        ArrayList<RssItem> rssItems = new ArrayList<RssItem>();//хранит список статей

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();//фабрика для создания строителя
            DocumentBuilder builder = factory.newDocumentBuilder();//строитель для создания документа
            Document document = builder.parse(is);//создает документ из XML входящего потока
            Element element = document.getDocumentElement();//получает корневой элемент
            NodeList nodeList = element.getElementsByTagName("item");//получает элементы item внутри
                                                                     // коренного элемента

            if (nodeList.getLength() > 0) {//если список не пустой
                for (int i = 0; i < nodeList.getLength(); i++) {

                    Element entry = (Element) nodeList.item(i);//текущий элемент списка(статья новостей)

                    Element titleE = (Element) entry.getElementsByTagName(
                            "title").item(0);//получает элемент title внутри текущего элемента

                    Element pubDateE = (Element) entry.getElementsByTagName(
                            "pubDate").item(0);//получает элемент pubDate внутри текущего элемента

                    Element descriptionE = (Element) entry.getElementsByTagName(
                            "description").item(0);//получает элемент description внутри текущего элемента

                    Element linkE = (Element) entry.getElementsByTagName(
                            "link").item(0);//получает элемент link внутри текущего элемента

                    String titleS = titleE.getFirstChild().getNodeValue();//содержит значение узла(title)
                    Date pubDateS = new Date(pubDateE.getFirstChild().getNodeValue());//содержит значение узла(pubDate)
                    String descriptionS = descriptionE.getFirstChild().getNodeValue();//содержит значение узла(description)
                    String linkS = linkE.getFirstChild().getNodeValue();//содержит значение узла(link)

                    //создает объект статьи новостей
                    RssItem rssItem = new RssItem(titleS, pubDateS, descriptionS, linkS);

                    rssItems.add(rssItem);//добавляет объект новостей в ArrayList
                }
            }
        } catch (IOException e) {//ловит исключения ввода-вывода, например если не удалось создать входящий поток
            e.printStackTrace();
        } catch (ParserConfigurationException e) {//ловит исключения парсинга
            e.printStackTrace();
        } catch (SAXException e) {//ловит исключения обработки XML c помощью SAX
            e.printStackTrace();
        }

        return rssItems;
    }
}

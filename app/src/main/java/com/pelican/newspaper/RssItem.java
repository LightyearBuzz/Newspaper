package com.pelican.newspaper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * описывает объект статьи новостей. Cодержит поле заголовка, даты, и описания.
 * Содержит метод возврата строки для отображения в ListView второй Активности.
 */
public class RssItem {

    private String title;
    private Date pubDate;
    private String description;
    private String link;

    //конструктор принимает название, описание, дату и ссылку
    public RssItem(String title, Date pubDate, String description, String link) {
        this.title = title;
        this.pubDate = pubDate;
        this.description = description;
        this.link = link;
    }

    public String getTitle()
    {
        return this.title;
    }

    public Date getPubDate()
    {
        return this.pubDate;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getLink()
    {
        return this.link;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd - hh:mm:ss");//содержит форамат даты
        String dateNews = sdf.format(this.getPubDate());//получает дату из поля pubDate
        String result = getTitle() + "  ( " + dateNews + " )";//содержит заголовок и дату
        return result;
    }
}
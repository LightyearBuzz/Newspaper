package com.pelican.newspaper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import java.text.SimpleDateFormat;

/**
 * описывает работу третьей Activity. В данной активности из объекта текущей статьи
 * извлекаются дата, заголовок статьи  и описание статьи. Затем эти данные размещаются
 * в текстовых представлениях
 */
public class ShowRssActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_rss);

        setTitle("News");

        RssItem currentRssItem = CurrentChannelActivity.currentRssItem;//хранит текущий пункт списка

        TextView titleView = findViewById(R.id.titleTextView);
        TextView contentView = findViewById(R.id.contentTextView);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd - hh:mm:ss");//содержит форамат даты
        String dateNews = sdf.format(currentRssItem.getPubDate());//содержит дату из текущей статьи

        String title = "";
        title = "\n" + currentRssItem.getTitle() + "  ( " + dateNews + " )\n\n";//содержит заголовок

        String content = "";
        content = currentRssItem.getDescription() + "\n" + currentRssItem.getLink();//содержит описание

        titleView.setText(title);
        contentView.setText(content);
    }
}
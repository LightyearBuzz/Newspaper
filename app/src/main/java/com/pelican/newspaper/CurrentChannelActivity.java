package com.pelican.newspaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * описывает работу второй Activity. В первой части расположены поля класса,
 * затем метод onCreate, в нем мы получаем URL из намерения и запускаем метод updateRssList().
 * А также устанавливаем слушателя на ListView.
 *
 * Метод updateRssList() через объект класса Сonnecting(паралельный поток) создает соединение с сервером. И заполняет
 * ArrayList полученными новостями
 */
public class CurrentChannelActivity extends Activity {
    static RssItem currentRssItem = null;//хранит текущий пункт списка
    String channelUrl = "";
    ArrayList<RssItem> rssItems = new ArrayList<RssItem>();//хранит объекты новостей
    ArrayAdapter<RssItem> adapter =  null;//адаптер между ArrayList и ListView
    ListView rssListView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_channel);

        setTitle("List");
        rssListView = findViewById(R.id.rssListView);

        Intent intent = getIntent();//принимает намерение от TopActivity
        channelUrl = intent.getStringExtra("url");//извлекает из намерения URL
        updateRssList();//заполняет ListView новостями из сети.


         /*описывает и устанавливает слушателя ListView,
         при нажатии на элемент, сохраняет выбраную статью в объект RssItem
         для временного хранения, и запускает активность ShowRssActivity*/
        rssListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                currentRssItem = rssItems.get(position);//сохраняет выбраную статью в объект RssItem

                //запускает активность ShowRssActivity
                Intent intentTwo = new Intent(CurrentChannelActivity.this, ShowRssActivity.class);
                startActivity(intentTwo);
            }
        });
    }

    /*получает соединение с сервером через объект класса Сonnecting(паралельный поток) и заполняет
     ArrayList полученными новостями*/
    private void updateRssList(){
        try {
            Connecting connector = new Connecting(channelUrl);//создает объект паралельного потока
            connector.start();
            connector.join();

            ArrayList<RssItem> newItems = connector.getList();//получает ArrayList с новыми статьями из объекта потока
            rssItems.clear();//очищает ArrayList
            rssItems.addAll(newItems);//заполняет ArrayList

            adapter = new ArrayAdapter<RssItem>(CurrentChannelActivity.this,
                    android.R.layout.simple_list_item_1, rssItems);//связывает адаптер с ArrayList
            rssListView.setAdapter(adapter);//устанавливает адаптер на ListView

        //ловит исключения прерывания
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package com.pelican.newspaper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

/**
 * реализует паралельный поток. Конструктор класса принимает строку которая содержит URL.
 * Затем создает соединение и привязывает его ко входящему потоку.
 * При помощи объекта класса Processing обрабатывает данные из входящего потока и
 * помещает их в ArrayList на хранение.
 */
public class Connecting extends Thread{
    static String channelUrl="";
    static ArrayList<RssItem> newItems = null;//для хранения списка новостей

    //конструктор класса принимает строку которая содержит URL
    Connecting(String channel){
        channelUrl = channel;
    }

    /* создает соединение и привязывает его ко входящему потоку.
     * При помощи объекта класса Processing обрабатывает данные из входящего потока и
     * помещает их в ArrayList на хранение*/
    @Override
    public void run(){
        try {
            URL url = new URL(channelUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {//проверяет есть ли соединение
                InputStream is = conn.getInputStream();
                Processing processor = new Processing();//обработчик данных
                newItems = processor.getRssItems(is);//ArrayList, хранит обработанные данные
            }

        //ловит исключения ввода-вывода, например если не удалось создать входящий поток
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //возвращает ArrayList, который хранит обработанные данные
    public  ArrayList<RssItem> getList (){
        return newItems;
    }
}
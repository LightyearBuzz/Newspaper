/**
 * Приложение Newspaper является мобильным RSS агрегатором, написано 05.2019
 *В нем используется
 *1.язык Java
 *2.Android SDK
 *3.объектно-ориентированный подход программирования
 *4.приемы многопоточного програмирования
 *5.работа с интернет соединением
 *6.парсинг XML
 *
 * Пользователь вводит адрес ресурса новостей.
 * При нажатии на кнопку ADD URL, адрес попадает в список адресов
 * При нажатии на кнопку DEL URL, адрес удаляется из списка адресов
 * При нажатии на кнопку GET NEWS, пользователь получает новости с ресурса
 * При нажатии на кнопку SAVE FILE, адреса из списка помещаются в файл
 * При нажатии на кнопку OPEN FILE, адреса из файла помещаются в список
 *
 */

package com.pelican.newspaper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * описывает работу верхней Activity, 5 кнопок и список каналов.
 * первая часть содержит поля класса (в том числе View).
 * Далее следует метод onCreate(), в первой части которого
 * происходит инициализация полей класса. Далее описываются
 * слушатели 5 кнопок данной активности. И в конце мы видим описание
 * слушателя списка.
 */
public class TopActivity extends Activity {

    int currentIndex = 0;//хранит текущий индекс списка
    ArrayList<String> channels = new ArrayList<>();//хранит адреса URL
    ArrayAdapter<String> adapter = null;//адаптер между ArrayList и ListView
    File fileName = new File("channels_file.txt");//имя файла для хранения адресов
    File folder = new File(Environment.getExternalStorageDirectory()
            + "/Android/data/com.pelican.newspaper");//имя дирректории для хранения файла
    File absolutePath = new File(folder + "/" + fileName);//полное имя-путь файла

    EditText editText = null;
    Button addBtn = null;
    Button deleteBtn = null;
    Button getNewsBtn = null;
    Button openBtn = null;
    Button saveBtn = null;
    ListView channelListView = null;
    Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_rss);

        setTitle("Newspaper");
        folder.mkdirs();//создает папку для хранения файла

        adapter = new ArrayAdapter<String>(TopActivity.this,
                android.R.layout.simple_list_item_1, channels);//привязывает адаптер к ArrayList

        editText = findViewById(R.id.rssURL);
        addBtn = findViewById(R.id.addBtn);
        deleteBtn = findViewById(R.id.delBtn);
        getNewsBtn = findViewById(R.id.getBtn);
        saveBtn = findViewById(R.id.saveBtn);
        openBtn = findViewById(R.id.openBtn);
        channelListView = findViewById(R.id.channelListView);

        channelListView.setAdapter(adapter);//установает адаптер на ListView

        /*описывает и устанавливает слушателя кнопки addURL
        при нажатии на нее, URL из editText попадает в ArrayList */
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channels.add(0, editText.getText().toString());//
                adapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        /*описывает и устанавливает слушателя кнопки delete URL, при нажатии на нее
        из ArrayList удаляется текущий элемент*/
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (channels.size() > 0) {
                    channels.remove(currentIndex);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
            }
        });

        /*описывает и устанавливает слушателя кнопки getNews.
        при нажатии на нее создается намерение открыть новую активность
        в которой отображается список новостей выбранного канала*/
        getNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channelUrl = editText.getText().toString();//получает URL из editText

                if(channelUrl.equals("")){
                    toast = Toast.makeText(getApplicationContext(),
                        "Enter URL", Toast.LENGTH_SHORT);
                    toast.show();//сообщает что нужно ввести URL
                }else {
                    Intent intent = new Intent(TopActivity.this,
                            CurrentChannelActivity.class);//создает намерение открыть активность
                    intent.putExtra("url", channelUrl);//передает URL в намерение
                    currentIndex = 0;
                    startActivity(intent);

                }
            }
        });

       /*описывает и устанавливает слушателя кнопки open file.
        при нажатии на нее создается входящий поток из которого происходит
        буферизованное чтение во временную строку, а из строки в ArrayList*/
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                try {
                    FileInputStream inputStream = new FileInputStream(absolutePath);
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(isr);

                    channels.clear();//очищает ArrayList
                    str = reader.readLine();//записывает строку из потока во временную

                    //в цикле из временной строки копируем в ArrayList пока файл не закончиться
                    while (str != null) {
                        channels.add(0, str);
                        str = reader.readLine();
                    }
                    adapter.notifyDataSetChanged();
                    inputStream.close();

                //ловит исключения ввода-вывода, например если не удалось создать входящий поток
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /*описывает и устанавливает слушателя кнопки save file.
        при нажатии на нее из ArrayList копируется строка во временную строку,
        затем создается фаил к которому привязывается исходящий поток,
        затем временная строка копируется в исходящий поток(файл)*/
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                try {
                    absolutePath.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(absolutePath);

                    //в циле копирует строку из ArrayList во временную строку,
                    //из временной строки в исходящий поток пока ArrayList не закончится
                    for (int i = 0; i < channels.size(); i++) {
                        str = channels.get(i);
                        outputStream.write(str.getBytes("UTF-8"));
                        outputStream.write('\n');
                    }

                    toast = Toast.makeText(getApplicationContext(),
                            "File saved", Toast.LENGTH_SHORT);
                    toast.show();//сообщает что файл успешно сохранен

                    outputStream.close();
                //ловит исключения ввода-вывода, например если не удалось создать исходящий поток
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        /*описывает и устанавливает слушателя ListView,
        при нажатии на элемент, его индекс сохраняется в поле, URL отображается в editText*/
        channelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                currentIndex = position;
                editText.setText(channels.get(position));
            }
        });
    }
}
package com.example.popularnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private ListView lv;
    ArrayList<String > rssLinks1 = new ArrayList<String>();
    String urlAdress;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.list_view_1);
      rssLinks1 = getArrayPrefs("urlRSS", getApplicationContext());
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, rssLinks1);
        lv.setAdapter(arrayAdapter1);
        Button btnRediff = findViewById(R.id.btnPerehod);
        Button btnCinemaBlend = findViewById(R.id.btnDelete);
        Button button = findViewById(R.id.button);

        final EditText editText = findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlRSS = "";
               urlRSS = editText.getText().toString();
                rssLinks1.add(urlRSS);
                editText.setText("");
                setArrayPrefs("urlRSS", rssLinks1,getApplicationContext());

               rssLinks1 = getArrayPrefs("urlRSS", getApplicationContext());
               lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
               ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, rssLinks1);
                lv.setAdapter(arrayAdapter2);
            }

        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {

                urlAdress = ((TextView)itemClicked).getText().toString();
                pos = lv.getCheckedItemPosition();

            }
        });


        btnRediff.setOnClickListener(this);
        btnCinemaBlend.setOnClickListener(this);
        //https://24tv.ua/rss/all.xml?lang=ru
      //  rssLinks1.add("http://www.lenta.ru/rss");

      //  rssLinks1.add("https://rss.publika.md/stiri.xml");
        //https://vz.ru/rss.xml
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RSSFeedActivity.class);
        switch (view.getId()) {
            case R.id.btnPerehod:
                intent.putExtra("rssLink", urlAdress);
                startActivity(intent);

                break;
            case R.id.btnDelete:
            delete("urlRSS", rssLinks1, pos, getApplicationContext());
                rssLinks1 = getArrayPrefs("urlRSS", getApplicationContext());
                lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, rssLinks1);
                lv.setAdapter(arrayAdapter3);
                break;
        }
    }

    private void setArrayPrefs(String arrayName, ArrayList<String> array, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("myPrefs", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putString(arrayName + "_" + i, array.get(i));
        editor.apply();
    }

    private ArrayList<String> getArrayPrefs(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("myPrefs", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        ArrayList<String> array = new ArrayList<>(size);
        for(int i=0;i<size;i++)
            array.add(prefs.getString(arrayName + "_" + i, null));
       //prefs.edit().clear().commit();
        return array;
    }
private void delete(String arrayName, ArrayList<String> array,int pos, Context mContext){
    SharedPreferences prefs = mContext.getSharedPreferences("myPrefs", 0);
    prefs.edit().remove(arrayName + "_" + pos).apply();
    prefs.edit().clear().commit();
    SharedPreferences.Editor editor = prefs.edit();
    editor.putInt(arrayName+"_size", array.size()-1);
    editor.apply();

    for(int i = 0; i<pos; i++){
            editor.putString(arrayName + "_" + i, array.get(i));
    editor.apply();}
    for(int j=pos+1; j<array.size();j++) {
        editor.putString(arrayName + "_" + (j - 1), array.get(j));
        editor.apply();
    }
}
}

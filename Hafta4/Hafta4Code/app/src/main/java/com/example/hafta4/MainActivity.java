package com.example.hafta4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewNumbers, listViewCities;
    private Button buttonRandomize;

    private String[] cities = {
            "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa",
            "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan",
            "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta",
            "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir",
            "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla",
            "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt",
            "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak",
            "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman",
            "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"
    };

    private List<Integer> numbersList;
    private ArrayAdapter<Integer> numbersAdapter;
    private ArrayAdapter<String> citiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewNumbers = findViewById(R.id.listViewNumbers);
        listViewCities = findViewById(R.id.listViewCities);
        buttonRandomize = findViewById(R.id.buttonRandomize);

        numbersList = new ArrayList<>();
        for (int i = 1; i <= 81; i++) {
            numbersList.add(i);
        }

        // Şehirler listesini doldur
        citiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        listViewCities.setAdapter(citiesAdapter);

        // Sayıları karıştır ve listeyi doldur
        randomizeNumbers();

        // Butona basınca sayıları tekrar karıştır
        buttonRandomize.setOnClickListener(v -> randomizeNumbers());

        // Senkronize Scroll Ayarı: Bir liste kaydırılınca diğeri de kayar
        listViewNumbers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getChildAt(0) != null) {
                    int top = view.getChildAt(0).getTop();
                    listViewCities.setSelectionFromTop(firstVisibleItem, top);
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        listViewCities.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getChildAt(0) != null) {
                    int top = view.getChildAt(0).getTop();
                    listViewNumbers.setSelectionFromTop(firstVisibleItem, top);
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        // Şehre tıklanınca ResultActivity'ye git
        listViewCities.setOnItemClickListener((parent, view, position, id) -> {
            int selectedNumber = numbersList.get(position);
            int correctPlate = position + 1;
            String cityName = cities[position];
            String result;

            if (selectedNumber == correctPlate) {
                result = cityName + " ile eşleşme bulundu!";
            } else {
                result = cityName + " ile eşleşmedi (" + selectedNumber + " != " + correctPlate + ")";
            }

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);
        });
    }

    private void randomizeNumbers() {
        Collections.shuffle(numbersList);
        if (numbersAdapter == null) {
            numbersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numbersList);
            listViewNumbers.setAdapter(numbersAdapter);
        } else {
            numbersAdapter.notifyDataSetChanged();
        }
    }
}
package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.slice.Slice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm, totalActive, totalRecovered, totalDeath, totalTests;
    private TextView todayconfirm, todayRecovered, todaydDeath, date;
    private PieChart pieChart;


    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        init();

        findViewById(R.id.countryName).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CountryActivity.class)));



        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>>call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i=0; i<list.size(); i++){
                            if (list.get(i).getCountry().equals("India")){
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeath.setText(NumberFormat.getInstance().format(death));

                                todaydDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                todayconfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm",confirm, getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Recovered",recovered, getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("Active",active, getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Death",death, getResources().getColor(R.color.red)));


                            }
                        }
                    }

                    private void setText(String updated) {
//                        DateFormat format = new SimpleDateFormat( patter: "MMM dd,YYYY");
//
//                        long milliseconnd = Long.parseLong(updated);
//
//                        Calendar
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable y) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void init() {

        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        totalTests = findViewById(R.id.totalTests);
        todayconfirm = findViewById(R.id.todayConfirm);
        todayRecovered = findViewById(R.id.todayRecovered);
        todaydDeath = findViewById(R.id.todayDeath);
        pieChart = findViewById(R.id.pieChart);
        date = findViewById(R.id.date);



    }

}
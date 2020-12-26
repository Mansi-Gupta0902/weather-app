package com.mansi.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText et;

    private TextView tv;

    String url="api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apikeys="454f2e213f8f348a9c809edad6eea99f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=(EditText)findViewById(R.id.et);
        tv=(TextView)findViewById(R.id.tv);
    }

    public void getWeather(View view) {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherapi myapi=retrofit.create(weatherapi.class);
        Call<Example>example=myapi.getweather(et.getText().toString().trim(),apikeys);
        example.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if(response.code()==404)
                {
                    Toast.makeText(MainActivity.this,"Please enter a valid city name",Toast.LENGTH_SHORT);
                }
                else if(!(response.isSuccessful()))
                {
                    Toast.makeText(MainActivity.this,response.code(),Toast.LENGTH_SHORT);
                }
                Example mydata=response.body();
                Main main=mydata.getMain();
                Double temp=main.getTemp();
                Integer temperature=(int)(temp-273.15);
                tv.setText(String.valueOf(temperature+"C"));
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT);
            }
        });
    }
}
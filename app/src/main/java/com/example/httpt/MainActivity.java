package com.example.httpt;

import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.json.*;


import java.io.IOException;


import okhttp3.HttpUrl;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * коммент для меня самого
 * в этой библиотеке все делается через внутренний класс Builder (строятся и ссылки, и запросы и )
 */
public class MainActivity extends AppCompatActivity {
    OkHttpClient client;
    Request request;
    TextView jsonTv/*,pressureTv,windTv,tempTv*/ ;
    Button requestButton;
    EditText cityNameEt;
    private static final String ADDRESS = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "219614d6ad572af84068364f6f08c3d2";
    private static final String SECOND_ADDRESS = "http://192.168.0.79:8080/api/v1/student";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* pressureTv = findViewById(R.id.pressure);
        tempTv = findViewById(R.id.temperature);
        windTv = findViewById(R.id.wind);
        */
        jsonTv = findViewById(R.id.json_tv);
        cityNameEt = findViewById(R.id.city_name_et);
        requestButton = findViewById(R.id.request_button);


        requestButton.setOnClickListener(listener);

    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            //String cityName = cityNameEt.getText().toString();
            String cityName = "default, не использую пока что";

            AsyncWeatherRequest weatherRequest = new AsyncWeatherRequest();
            weatherRequest.execute(cityName);
        }
    };

    class AsyncWeatherRequest extends AsyncTask<String,Void,Response>{

        @Override
        protected Response doInBackground(String... cityNames) {
//тут всё понятно, инициализация клиента
            client = new OkHttpClient();

//новый строитель ссылки с параметрами (закомментил параметры, мне не нужно)
            HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(SECOND_ADDRESS).newBuilder();

//добавление парамеров
           /* httpUrlBuilder.addQueryParameter("q",cityNames[0]);
            httpUrlBuilder.addQueryParameter("appid",API_KEY);
            httpUrlBuilder.addQueryParameter("lang","ru");
            httpUrlBuilder.addQueryParameter("units","metric");
           */
//постройка ссылки с параметрами
            HttpUrl httpUrl = httpUrlBuilder.build();



//постройка реквеста
            request = new Request.Builder().url(httpUrl).build();





//отправка реквеста с помощью клиента и получение ответа как возвращаемого значения execute()
            try {
                Response response = client.newCall(request).execute();

                return response;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }



        @Override
        protected void onPostExecute(Response response) {
                try {
                    super.onPostExecute(response);

                   JSONArray rootArray = new JSONArray(response.body().string());
                    /*
                    JSONObject mainObject = rootObject.getJSONObject("main");
                    JSONObject windObject = rootObject.getJSONObject("wind");

                    Double temperature = mainObject.getDouble("temp");
                    tempTv.setText(String.format("Температура %.1f градусов цельсия", temperature));

                    Double pressure = mainObject.getDouble("pressure");
                    pressureTv.setText(String.format("Атмосферное давление %.3f мм рт ст",pressure));

                    Double windSpeed = windObject.getDouble("speed");
                    windTv.setText(String.format("Скорость ветра %.2f метров в секунду",windSpeed));
                    */

                    jsonTv.setText(String.valueOf(rootArray));
                }

                catch (IOException e) {e.printStackTrace();}
                catch (JSONException e) {e.printStackTrace();}


        }


    }
}
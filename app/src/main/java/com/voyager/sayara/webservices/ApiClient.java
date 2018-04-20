package com.voyager.sayara.webservices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.voyager.sayara.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static Retrofit pathRetrofit = null;
    private static OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(300, TimeUnit.SECONDS).
            readTimeout(300, TimeUnit.SECONDS).
            build();


    /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
       logging.setlevel(HttpLoggingInterceptor.Level.BODY)

         OkHttpClient.Builder httpClient = new OkHttpClient.Builder();*/
        // add your other interceptors …

        // add logging as last interceptor
        //httpClient.addInterceptor(logging);

    public static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            if(BuildConfig.DEBUG){
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                logging.getLevel().toString();
                System.out.println("logging : "+logging.getLevel().toString());
            }
            OkHttpClient client = new OkHttpClient.Builder().
                    addInterceptor(logging).
                    connectTimeout(300, TimeUnit.SECONDS).
                    readTimeout(300, TimeUnit.SECONDS).
                    build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            /*http://10.1.1.21/sayara/*/
            retrofit = new Retrofit.Builder().baseUrl("http://10.1.1.18/sayara/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }

    public static Retrofit getNearRetrofitClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            /*http://10.1.1.21/sayara/*/
            retrofit = new Retrofit.Builder().baseUrl("http://ablecode.com/nearbystores/1.0/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }


    public static Retrofit getRetrofitClientPath() {
        if (pathRetrofit == null) {
            pathRetrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return pathRetrofit;
    }
}

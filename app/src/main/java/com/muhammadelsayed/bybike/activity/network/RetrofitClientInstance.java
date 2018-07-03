package com.muhammadelsayed.bybike.activity.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    public static final String BASE_URL = "http://52.224.66.22/abdullah/bel3agal/bel3agal/public/";
    public static OkHttpClient.Builder httpClient;

    public static  Retrofit getRetrofitInstance() {

        //http logging interceptor will give us the information about web service call reqponse.
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient.Builder();

        httpClient.readTimeout(60, TimeUnit.SECONDS);

        httpClient.connectTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(logging);

        //We should add headers for the request.
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });


        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }


}

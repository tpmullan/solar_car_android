package com.scf.michael.solarcarbasic.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.TimeZone;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tom on 4/26/16.
 */
public class ServiceGenerator {

    private static final String BASE_URL = "https://solar.tpmullan.com/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson =
            new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .excludeFieldsWithoutExposeAnnotation()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static CookieManager cookieManager = new CookieManager();

    public static <S> S createService(Class<S> serviceClass) {

        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        HttpCookie httpTimezoneCookie = new HttpCookie("timezone", TimeZone.getDefault().getID());
        cookieManager.getCookieStore().add(URI.create(BASE_URL), httpTimezoneCookie);

        OkHttpClient client = httpClient
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
        Retrofit retrofit = builder.client(client).build();

        return retrofit.create(serviceClass);
    }

    public static Gson getGson() {
        return gson;
    }

}

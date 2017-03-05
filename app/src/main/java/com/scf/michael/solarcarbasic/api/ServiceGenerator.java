package com.scf.michael.solarcarbasic.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tom on 4/26/16.
 */
public class ServiceGenerator {

    private static final String BASE_URL = "https://solar.tpmullan.com/api/";
    //private static final String BASE_URL = "http://desktop:8000/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Gson gson =
            new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static CookieManager cookieManager = new CookieManager();

    public static <S> S createService(Class<S> serviceClass) {

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                //Log.d("RETROFIT request", bodyToString(original.body()));
                //Log.d("RETROFIT params", original.url().toString());


                Auth authToken = Auth.getInstance();
                Request.Builder requestBuilder = original.newBuilder()
                        .method(original.method(), original.body());
                requestBuilder.header("Content-Type", "application/json");
                requestBuilder.header("Accept","application/json");
                Request requestWithHeaders = authToken.setHeaders(requestBuilder).build();

                //Log.d("RETROFIT headers", requestWithHeaders.headers().toString());

                Response response = chain.proceed(requestWithHeaders);

                String body = response.body().string();
                //Log.d("RETROFIT response", body);
                if (response.code() == 422 && body.equals("Invalid authenticity token")) {
                    response = chain.proceed(authToken.setHeaders(requestBuilder).build());
                    //authToken.update(response.headers());
                } else {
                    response = response.newBuilder()
                            .body(ResponseBody.create(response.body().contentType(), body))
                            .build();
                }

                return response;
            }
        });

        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        HttpCookie httpTimezoneCookie = new HttpCookie("timezone", TimeZone.getDefault().getID());
        cookieManager.getCookieStore().add(URI.create(BASE_URL), httpTimezoneCookie);

        OkHttpClient client = httpClient
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
        Retrofit retrofit = builder.client(client).build();

        return retrofit.create(serviceClass);
    }

    @NonNull
    public static String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if(copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }

    public static Gson getGson() {
        return gson;
    }

}

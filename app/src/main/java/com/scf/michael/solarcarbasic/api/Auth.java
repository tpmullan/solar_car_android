package com.scf.michael.solarcarbasic.api;

import android.util.Log;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmObject;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends RealmObject {

  private static final String TAG = "Auth Class";
  @SerializedName("token")
  @Expose
  private String token;

  @SerializedName("username")
  @Expose
  private String username;

  @SerializedName("password")
  @Expose
  private String password;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void login() {
    ClosedTrackSolarApiEndpoint endpoint = ServiceGenerator.createService(ClosedTrackSolarApiEndpoint.class);
    Call<Auth> call =  endpoint.login(this);
      call.enqueue(new Callback<Auth>() {
        @Override
        public void onResponse(Call<Auth> call, final Response<Auth> response) {
          if (response.isSuccessful()) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(
                    new Realm.Transaction() {
                      @Override
                      public void execute(Realm realm) {
                        setToken(response.body().getToken());
                      }
                    }
            );
          } else {
            Log.e(TAG,"Error logging in");
          }
        }

        @Override
        public void onFailure(Call<Auth> call, Throwable t) {
          Log.e(TAG,"Error logging in");
        }
      });
  }

  public static Auth getInstance() {
    Realm realm = Realm.getDefaultInstance();
    Auth first = realm.where(Auth.class).findFirst();
    if (first == null) {
      realm.beginTransaction();
      first = realm.createObject(Auth.class);
      realm.commitTransaction();
    }
    return first;
  }

  public Request setHeaders(Request original) {
    // Request customization: add request headers
    Request.Builder requestBuilder = original.newBuilder()
            .method(original.method(), original.body());
    if (token!=null && !token.equals("")) {
      requestBuilder.header("token", getToken());
    }
    return requestBuilder.build();
  }
}

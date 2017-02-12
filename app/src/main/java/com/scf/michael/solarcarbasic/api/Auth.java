package com.scf.michael.solarcarbasic.api;

import android.util.Log;
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
  private String token;

  @SerializedName("username")
  private String username;

  @SerializedName("password")
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
    final Realm realm = Realm.getDefaultInstance();

    final Auth self = this;
    Call<Auth> call =  endpoint.login(realm.copyFromRealm(self));
      call.enqueue(new Callback<Auth>() {
        @Override
        public void onResponse(Call<Auth> call, final Response<Auth> response) {
          if (response.isSuccessful()) {
            realm.executeTransaction(
                    new Realm.Transaction() {
                      @Override
                      public void execute(Realm realm) {
                        self.setToken(response.body().getToken());
                      }
                    }
            );
          } else {
            Log.e(TAG,"Not successfull logging " + response.message()+"\n"+call.request().method());
          }
        }

        @Override
        public void onFailure(Call<Auth> call, Throwable t) {
          Log.e(TAG,"Error with network logging in");
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

  public Request.Builder setHeaders(Request.Builder requestBuilder) {
    // Request customization: add request headers
    if (getToken()!=null && !getToken().equals("")) {
      requestBuilder.header("Authorization", "Token " + getToken());
    }
    return requestBuilder;
  }
}

package com.scf.michael.solarcarbasic.api;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

import java.io.IOException;
import com.orm.SugarRecord;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends SugarRecord {

  private static final String TAG = "Auth Class";
  @Expose
  @SerializedName("token")
  private String token;

  @Expose
  @SerializedName("username")
  private String username;

  @Expose
  @SerializedName("password")
  private String password;

  public Auth() {

  }

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

    final Auth self = this;
    self.setToken(null);

    Call<Auth> call =  endpoint.login(self);
      call.enqueue(new Callback<Auth>() {
        @Override
        public void onResponse(Call<Auth> call, final Response<Auth> response) {
          if (response.isSuccessful()) {

            self.setToken(response.body().getToken());
            self.save();

          } else {
            //Log.e(TAG,"Not successfull logging " + response.message()+"\n"+call.request().method());
          }
        }

        @Override
        public void onFailure(Call<Auth> call, Throwable t) {
          //Log.e(TAG,"Error with network logging in");
        }
      });
  }

  public static Auth getInstance() {
    Auth first =  Auth.findById(Auth.class, (long) 1);
    if (first == null) {
      first = new Auth();
      first.save();
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

package com.scf.michael.solarcarbasic.api;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Streams;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;

@Table(name = "Auth")
public class Auth extends Model {

  private static final String TAG = "Auth Class";
  @Column(name = "token")
  @SerializedName("token")
  @Expose
  private String token;

  @Column(name = "username")
  @SerializedName("username")
  @Expose
  private String username;

  @Column(name = "password")
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
    try {
      Response<Auth> response = call.execute();
      if (response.isSuccessful()) {
        this.token = response.body().getToken();
        this.save();
      } else {
        Log.e(TAG,"Error logging in");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Auth getInstance() {
    Auth first = new Select()
            .from(Auth.class)
            .limit(1)
            .orderBy("id asc")
            .executeSingle();
    if (first == null) {
      first = new Auth();
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

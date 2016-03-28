package com.example.fragments;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class LoginActivity extends Activity {

	private EditText username, password;
	private Button login, registration;

	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		username = (EditText)findViewById(R.id.la_usernameEdit);
		password = (EditText)findViewById(R.id.la_passwordEdit);
		login = (Button)findViewById(R.id.la_loginButton);
		registration = (Button)findViewById(R.id.la_registrationButton);
		
		login.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				class Task extends AsyncTask<String, Integer, String> {
					@Override
					protected void onPreExecute() {
						login.setEnabled(false);
						pd = new ProgressDialog(LoginActivity.this);
                        pd.setTitle("Wait");
                        pd.setMessage("Sign in is in the process now");
                        pd.setCancelable(false);
                        pd.setIndeterminate(true);
                        pd.show();
	                }
					@Override
					protected String doInBackground(String... params) {
						HttpURLConnection urlConnection = null;
						String jsonResponse = null;
						try {
							urlConnection = loginRequest();
							InputStream in = new BufferedInputStream(
									urlConnection.getInputStream());
							BufferedReader reader = new BufferedReader(new InputStreamReader(in));
							StringBuilder builder = new StringBuilder();
							for (String line = null; (line = reader.readLine()) != null;) {
								builder.append(line).append("\n");
							}
							jsonResponse = builder.toString();
					        reader.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							urlConnection.disconnect();
						}
						return jsonResponse;
					}
					@Override
					protected void onPostExecute(String jsonResponse) {
						if (pd != null) {
                            pd.dismiss();
                        }
						JSONTokener tokener = new JSONTokener(jsonResponse);
						JSONObject finalResult = null;
						try {
							finalResult = new JSONObject(tokener);
							if (finalResult != null) {
								String status = finalResult.getString("status");
								if (status.equals("ok")) {
									Connection.isLogged = true;
									JSONObject data = finalResult.getJSONObject("data");
									SharedPreferencesManager.setAccessToken(data.getString("token"));
									LayoutInflater inflater = getLayoutInflater();
									View layout = inflater.inflate(R.layout.green_toast,
									                               (ViewGroup) findViewById(R.id.toast_layout_root));
									TextView text = (TextView) layout.findViewById(R.id.toast_text);
									text.setText("You are successfully logged in");
									Toast toast = new Toast(getApplicationContext());
									toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
									toast.setDuration(Toast.LENGTH_LONG);
									toast.setView(layout);
									toast.show();
									finish();
								} else {
									Connection.isLogged = false;
									LayoutInflater inflater = getLayoutInflater();
									View layout = inflater.inflate(R.layout.red_toast,
									                               (ViewGroup) findViewById(R.id.toast_layout_root));
									TextView text = (TextView) layout.findViewById(R.id.toast_text);
									text.setText("Incorrect login or password");
									Toast toast = new Toast(getApplicationContext());
									toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
									toast.setDuration(Toast.LENGTH_LONG);
									toast.setView(layout);
									toast.show();
									}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
				}
				String status = clientValidation();
				if (status == null) {
					Task task = new Task();
					task.execute();
				} else {				
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.red_toast,
										(ViewGroup) findViewById(R.id.toast_layout_root));
					TextView text = (TextView) layout.findViewById(R.id.toast_text);
					text.setText(status);
					Toast toast = new Toast(getApplicationContext());
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.setDuration(Toast.LENGTH_LONG);
					toast.setView(layout);
					toast.show();
				}
			}

			private String clientValidation() {
				if (username.getText().toString().isEmpty()) {
					return "Field \"Username\" should not be empty";
				}
				if (password.getText().toString().isEmpty()) {
					return "Field \"Password\" should not be empty";
				}
				
				return null;
			}
		});
		
		registration.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegistrationActivity.class);
				startActivity(intent);
			}
		});
	}

	protected HttpURLConnection loginRequest() throws Exception {
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("login", username.getText().toString());
		hashmap.put("password", password.getText().toString());
		HttpURLConnection conn = Post.performPostCall(Connection.mainUrl + "/?r=authentication/login", hashmap);
		return conn;
	}
}

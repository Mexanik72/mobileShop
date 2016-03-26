package com.example.fragments;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends Activity
{
	private CheckBox agreement;
	private TextView terms;
	private Button registration;
	private EditText name, surname, phone, login, email, password, confirm;
	
	private boolean agreementCheckBoxState = false;
	private ProgressDialog pd;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		name = (EditText)findViewById(R.id.ra_nameEdit);
		surname = (EditText)findViewById(R.id.ra_surnameEdit);
		phone = (EditText)findViewById(R.id.ra_phoneNumberEdit);
		login = (EditText)findViewById(R.id.ra_loginEdit);
		email = (EditText)findViewById(R.id.ra_emailEdit);
		password = (EditText)findViewById(R.id.ra_passwordEdit);
		confirm = (EditText)findViewById(R.id.ra_confirmPasswordEdit);
		agreement = (CheckBox)findViewById(R.id.ra_agreementCheckbox);
		terms = (TextView)findViewById(R.id.ra_terms);
		registration = (Button)findViewById(R.id.ra_registrationButton);
		
		if (savedInstanceState != null) {
			agreementCheckBoxState = savedInstanceState.getBoolean("agreementCheckBoxState");
		}
		registration.setEnabled(agreementCheckBoxState);
		
		agreement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				agreementCheckBoxState = agreement.isChecked();
				registration.setEnabled(agreementCheckBoxState);
			}
		});
		
		terms.setText(Html.fromHtml("<a href=\'http://www.google.com\'>terms</a>"));
		terms.setMovementMethod(LinkMovementMethod.getInstance());
		
		registration.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				class Task extends AsyncTask<String, Integer, String> {
					@Override
					protected void onPreExecute() {
						registration.setEnabled(false);
						pd = new ProgressDialog(RegistrationActivity.this);
                        pd.setTitle("Wait");
                        pd.setMessage("Registration is in the process now");
                        pd.setCancelable(false);
                        pd.setIndeterminate(true);
                        pd.show();
	                }
					@Override
					protected String doInBackground(String... params) {
						HttpURLConnection urlConnection = registrationRequest();
						String jsonResponse = null;
						try {
							InputStream in = new BufferedInputStream(
									urlConnection.getInputStream());
							BufferedReader reader = new BufferedReader(new InputStreamReader(in));
							StringBuilder builder = new StringBuilder();
							for (String line = null; (line = reader.readLine()) != null;) {
								builder.append(line).append("\n");
							}
							jsonResponse = builder.toString();
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return jsonResponse;
					}
					private HttpURLConnection registrationRequest() {
						HashMap<String, String> hashmap = new HashMap<String, String>();
						hashmap.put("name", name.getText().toString());
						hashmap.put("surname", surname.getText().toString());
						hashmap.put("login", login.getText().toString());
						hashmap.put("phone", phone.getText().toString());
						hashmap.put("password", password.getText().toString());
						hashmap.put("email", email.getText().toString());
						return Post.performPostCall(Connection.mainUrl + "/?r=registration", hashmap);
					}
					@Override
					protected void onPostExecute(String jsonResponse) {
						if (pd != null) {
                            pd.dismiss();
                            registration.setEnabled(true);
                        }
						JSONTokener tokener = new JSONTokener(jsonResponse);
						JSONObject finalResult = null;
						try {
							finalResult = new JSONObject(tokener);
							if (finalResult != null) {
								String status = finalResult.getString("status");
								if (status.equals("ok")) {
									Toast.makeText(getApplicationContext(), "Registration successfully completed", Toast.LENGTH_LONG).show();
									finish();
								} else {
									JSONObject message = finalResult.getJSONObject("message");
									Iterator<String> keys = message.keys();
									JSONArray firstError = message.getJSONArray(keys.next());
									Toast.makeText(getApplicationContext(), firstError.get(0).toString(), Toast.LENGTH_LONG).show();
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
					Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
				}
			}

			private String clientValidation() {
				if (name.getText().toString().isEmpty()) {
					return "Field \"Name\" should not be empty";
				}
				if (surname.getText().toString().isEmpty()) {
					return "Field \"Surname\" should not be empty";
				}
				if (login.getText().toString().isEmpty()) {
					return "Field \"Login\" should not be empty";
				}
				if (phone.getText().toString().isEmpty()) {
					return "Field \"Phone number\" should not be empty";
				}
				if (password.getText().toString().isEmpty()) {
					return "Field \"Password\" should not be empty";
				}
				Pattern pattern = Pattern.compile("^.+@.+\\..+$", Pattern.MULTILINE);
	            Matcher m = pattern.matcher(email.getText().toString());
	            if (!m.matches()) {
	            	return "Not valid e-mail";
	            }
				if (!password.getText().toString().equals(confirm.getText().toString())) {
					return "Passwords do not match";
				}
				
				return null;
			}
		});
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("agreementCheckBoxState", agreementCheckBoxState);
		super.onSaveInstanceState(savedInstanceState);
	}
}

package com.v.mypersonaltrainer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import com.database.sqlite.DataBaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettinActivity extends Activity {
	TextView name, height,weight,Dob,weightGoal,Bmi,Waist,GoalWeightDuration,Loc,Gen;
	String[] details; 
	String Name,CurrentWeight,GoalWeight,GoalDuration,BMI,Height,WaistText,Gender,DOB,Location;
	ImageButton imageButton1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		imageButton1=(ImageButton) findViewById(R.id.imageButtonOCR);
		name=(TextView)findViewById(R.id.Uname);
		height=(TextView)findViewById(R.id.UHeight);
		weight=(TextView)findViewById(R.id.CW);
		Dob=(TextView)findViewById(R.id.Dob);
		weightGoal=(TextView)findViewById(R.id.GW);
		Bmi=(TextView)findViewById(R.id.UBMI);
		Waist=(TextView)findViewById(R.id.UWaist);
		GoalWeightDuration=(TextView)findViewById(R.id.GD);
		Loc=(TextView)findViewById(R.id.Loc);
		Gen=(TextView)findViewById(R.id.UGen);
		dbGetUserDetails();
		imageButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		});
	}

	public void dbGetUserDetails(){
			  Runnable runnable = new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub

						try {
							URL url = new URL("http://58.27.132.54:443/health/getUserJSON?");
							HttpURLConnection urlConnection = (HttpURLConnection) url
									.openConnection();
							int resCode = urlConnection.getResponseCode();
							if (resCode == 200) {
								InputStream is = new BufferedInputStream(
										urlConnection.getInputStream());
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(is, "iso-8859-1"), 8);
								StringBuilder sb = new StringBuilder();
								String line = null;
								String result = "";
								while ((line = reader.readLine()) != null) {
									sb.append(line + "\n");
								}
								is.close();
								result = sb.toString();

								JSONArray jArray = new JSONArray(result);
								for (int i = 0; i < jArray.length(); i++) {
									JSONObject c = jArray.getJSONObject(i);
									// Storing each json item in variable

									Name= c.getString("Name");
									CurrentWeight = c.getString("Weight");
									GoalWeight = c.getString("WeightGoal");
									GoalDuration= c
											.getString("GoalWeightDuration");
									BMI = c.getString("BMI");
									Height = c.getString("Height");
									WaistText= c.getString("Waist");
									Gender= c.getString("Gender");
									DOB= c.getString("DateOfBirth");
									Location=c.getString("Location");
									
								}
							} else
								resCode = -1;

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				runnable.run();
				
				name.setText(Name);
				double heightDou= Double.valueOf(Height);
				int heightInt=(int)Math.round(heightDou);
				height.setText(String.format("%.2f", heightDou));
				//height.setText(String.valueOf(heightInt));
				weight.setText(CurrentWeight);
				Dob.setText(DOB);
				weightGoal.setText(GoalWeight);
				double BMIDou= Double.valueOf(BMI);
				int BMIInt=(int)Math.round(BMIDou);
				Bmi.setText(String.format("%.2f", BMIDou));
				Waist.setText(WaistText);
				GoalWeightDuration.setText(GoalDuration);
				Loc.setText(Location);
				Gen.setText(Gender);

		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
		return true;
	}

}

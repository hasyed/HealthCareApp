package com.v.mypersonaltrainer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.database.sqlite.DataBaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ExerciseActivity extends Activity {
	EditText exerName,exerDuration,exerNotes;
	Button btnAddExer;
	ImageButton imageButton1;
	public static String ExerciseName,CaloriesBurnt;
	int exerciseId;
	double caloriesBurnt;
	public static String Weight,calBurnString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exercise);
		imageButton1=(ImageButton) findViewById(R.id.imageButtonOCR);
		exerciseId=getIntent().getExtras().getInt("exerciseId");
		exerciseId++;
		addEditText();
		dbGetExerciseDetails();
		imageButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(getApplicationContext(), GoalActivity.class);
				startActivity(intent);
			}
		});
		btnAddExer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(exerDuration.getText().toString().contentEquals("")){
					Toast.makeText(getApplicationContext(), "Enter duration for exercise", Toast.LENGTH_LONG).show();
				}
				else{
					dbInsertUserExerciseDetails();
					//dbCountUserExerciseDetails();
				}
			}
		});
	}
	
	private void addEditText() {
		// TODO Auto-generated method stub
		exerName=(EditText) findViewById(R.id.exerName);
		exerDuration=(EditText)findViewById(R.id.exerDuration);
		exerNotes=(EditText)findViewById(R.id.exerNotes);
		btnAddExer=(Button)findViewById(R.id.btnAddExer);
	}

	public void dbGetExerciseDetails(){
		Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						URL url = new URL(
								"http://58.27.132.54:443/health/getIndividualExerciseListJSON?_id="
										+ String.valueOf(exerciseId) + "");
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
								ExerciseName=c.getString("NAME");
								CaloriesBurnt=c.getString("CaloriesBurnt");
								}
						} else
							resCode = -1;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			runnable.run();
			Toast.makeText(getApplicationContext(), ExerciseName+" "+CaloriesBurnt, Toast.LENGTH_LONG).show();
			exerName.setText(ExerciseName);
			caloriesBurnt=Double.parseDouble(CaloriesBurnt);
			

	}

	public void dbInsertUserExerciseDetails(){
		Runnable runnableUser = new Runnable() {
				
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
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,
									"iso-8859-1"), 8);
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
								  
								  Weight=c.getString("Weight");
							}
						} else
							resCode = -1;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			runnableUser.run();
			
			int duration=Integer.parseInt(exerDuration.getText().toString());
			if(exerNotes.getText().toString().contentEquals("")){
				exerNotes.setText("No notes");
			}
			
			double calBurn=duration*caloriesBurnt*Double.parseDouble(Weight)*2.20462;
			calBurnString=String.valueOf(calBurn);
			Runnable runnable= new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						URL url = new URL("http://58.27.132.54:443/health/insertUserExercise?Duration="+exerDuration.getText().toString()+"&CaloriesBurnt="+calBurnString+"&ExerciseId="+exerciseId+"");
						HttpURLConnection urlConnection = (HttpURLConnection) url
								.openConnection();
						int resCode = urlConnection.getResponseCode();
						Toast.makeText(getApplicationContext(), String.valueOf(resCode), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			};
			runnable.run();
		}

	public void dbCountUserExerciseDetails(){
		  DataBaseHelper db = new DataBaseHelper(getApplicationContext());
		  try {
				db.createDataBase();
			} catch (IOException ioe) {
				// throw new Error("Unable to create database");
				Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
			}
			try {
				db.openDataBase();
			} catch (SQLException sqle) {
				Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
				// throw sqle;
			}
			
			int count=db.getAllUserExerciseCount();
			Toast.makeText(getApplicationContext(), "Count is $$"+count, Toast.LENGTH_LONG).show();
			db.close();

	}
}

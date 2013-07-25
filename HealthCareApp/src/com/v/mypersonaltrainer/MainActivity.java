package com.v.mypersonaltrainer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.database.sqlite.DataBaseHelper;

import edu.sfsu.cs.orange.ocr.CaptureActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends Activity {
	ImageButton cambutton;
	ImageButton chartbutton;
	ImageButton goalbutton;
	ImageButton settingbutton;
	TextView tv,textView6, textview7, textView3, textView4, textView5,textView03, textView04, textView05;
	Button bmibutton, btnUpdBMI, btnChart;
	EditText heightText, weightText, duraText, GWedit;
	String Weight, Height, WeightGoal, BMI, GoalWeightDuration, Age;
	TextView BMIText;
	double sumE, sumM,h, sumG = 0;
	int bmiValue;
	int caloriesInt,exe;

	protected void showDialog() {
		AlertDialog.Builder alertd = new AlertDialog.Builder(this);
		alertd.setMessage("Are you sure you want to exit MY PERSONAL TRAINER ?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Action for 'Yes' Button

								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for 'NO' Button
						dialog.cancel();
					}
				});
		AlertDialog alert = alertd.create();
		// Title for AlertDialog
		alert.setTitle("Confirm Exit");
		// Icon for AlertDialog
		alert.setIcon(R.drawable.logo);
		alert.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main2);
		initialize();
		getAllDetails();
		getAllDetailsOnScreen();
		getCaloriesPerDay();
		getSumUserExercise();
		getSumUserProduct();
		double net = sumG - sumE - sumM;
		// textView5.setText(String.valueOf(net));
		cambutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						CaptureActivity.class);
				startActivity(i);

			}
		});

		chartbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						getApplicationContext(),
						com.v.mypersonaltrainer.chart.MainActivity.class);
				startActivity(i);
			}
		});
		goalbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						GoalActivity.class);
				startActivity(i);
			}
		});

		settingbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						SettinActivity.class);
				startActivity(i);
			}
		});
		bmibutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Bmi.class);
				startActivity(i);
			}
		});
		btnUpdBMI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				updateUserDetails();
				getCaloriesPerDay();

			}
		});

		btnChart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						getApplicationContext(),
						com.v.mypersonaltrainer.chart.MainActivity.class);
				startActivity(intent);
			}
		});
	}

	public void initialize() {
		cambutton = (ImageButton) findViewById(R.id.cam);
		goalbutton = (ImageButton) findViewById(R.id.goal);
		chartbutton = (ImageButton) findViewById(R.id.chart);
		heightText = (EditText) findViewById(R.id.heightText);
		weightText = (EditText) findViewById(R.id.weightText);
		GWedit = (EditText) findViewById(R.id.GWedit);
		duraText = (EditText) findViewById(R.id.DurationEdit);
		BMIText = (TextView) findViewById(R.id.BMIText);
		settingbutton = (ImageButton) findViewById(R.id.setting);
		bmibutton = (Button) findViewById(R.id.btnBMI);
		btnUpdBMI = (Button) findViewById(R.id.btnUpdBMI);
		btnChart = (Button) findViewById(R.id.btnChart);
		textview7 = (TextView) findViewById(R.id.textView7);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		textView5 = (TextView) findViewById(R.id.textView5);
		textView03 = (TextView) findViewById(R.id.TextView03);
		textView04 = (TextView) findViewById(R.id.TextView04);
		textView05 = (TextView) findViewById(R.id.TextView05);
		textView6= (TextView) findViewById(R.id.textView6);
	}

	public void getAllDetailsOnScreen() {
		float height = Float.parseFloat(Height);
		int h = (int) (height * 39.3701);
		int feet = (int) (h / 12);
		int inch = (int) (h % 12);
		// Toast.makeText(getApplicationContext(), feet +" "+inch,
		// Toast.LENGTH_LONG).show();
		heightText.setText(feet + "\"" + inch + "'");
		weightText.setText(Weight);
		GWedit.setText(WeightGoal);
		duraText.setText(GoalWeightDuration);
		Double bmiDouble = Double.valueOf(BMI);
		int bmiInt = (int) Math.round(bmiDouble);
		BMIText.setText(String.valueOf(bmiInt));
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());
		textView6.setText("It's "+formattedDate);

	}

	public double getCaloriesPerDay() {
		double calories = 0;
		String feets = heightText.getText().toString().substring(0, 1);
		String inches = heightText.getText().toString()
				.substring(2, heightText.getText().toString().length() - 1);
		double h = Double.parseDouble(feets) * 0.3048
				+ (Double.parseDouble(inches) * 0.0254);
		h = h * 100;
		int age = Integer.parseInt(Age);
		calories = ((10 * (Double.parseDouble(Weight))) + (6.5 * h) - (5 * age) * 1.55);
		double sum = Double.parseDouble(GWedit.getText().toString())
				- Double.parseDouble(weightText.getText().toString());
		double cal = Math.abs(sum)
				/ (Integer.parseInt(duraText.getText().toString()) / 7) * 2;
		calories = calories - cal;
		sumG = calories;
		Toast.makeText(getApplicationContext(), String.valueOf(calories),
				Toast.LENGTH_LONG).show();
		// Toast.makeText(getApplicationContext(), String.valueOf(calories),
		// Toast.LENGTH_LONG).show();
		int goalCal=(int)Math.round(sumG);
		textview7.setText(String.valueOf(goalCal));
		textView03.setText(String.valueOf(caloriesInt));
		textView04.setText(String.valueOf(exe));
		int remaining=goalCal-caloriesInt+exe;
		textView05.setText(String.valueOf(remaining));
		return calories;
	}

	public void getAllDetails() {
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

							Height = c.getString("Height");
							Weight = c.getString("Weight");
							WeightGoal = c.getString("WeightGoal");
							BMI = c.getString("BMI");
							GoalWeightDuration = c
									.getString("GoalWeightDuration");
							Age = c.getString("Age");
						}
					} else
						resCode = -1;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		runnable.run();

		Runnable runnableCal = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					URL url = new URL("http://58.27.132.54:443/health/getTodayCaloriesJSON?");
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

							caloriesInt= c.getInt("SUM(Calories)");
						}
					} else
						resCode = -1;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		runnableCal.run();

		
		Runnable runnableExe = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					URL url = new URL("http://58.27.132.54:443/health/getTodayCaloriesBurntJSON?");
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

							exe= c.getInt("SUM(CaloriesBurnt)");
						}
					} else
						resCode = -1;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		runnableExe.run();

		
		/*
		 * Toast.makeText(getApplicationContext(), details[0] + " " +
		 * details[7], Toast.LENGTH_LONG).show();
		 */
	}

	public void updateUserDetails() {

		heightText.getText().toString().length();
		String feets = heightText.getText().toString().substring(0, 1);
		String inches = heightText.getText().toString()
				.substring(2, heightText.getText().toString().length() - 1);
		h = Double.parseDouble(feets) * 0.3048
				+ (Double.parseDouble(inches) * 0.0254);
		double w = Double.parseDouble(weightText.getText().toString());
		bmiValue = (int) calculateBMI(w, h);
		/*
		 * DecimalFormat twoDForm = new DecimalFormat("#.##");
		 * Double.valueOf(twoDForm.format(bmiValue));
		 */
		final String WeightNew = String.valueOf(w);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					URL url = new URL(
							"http://58.27.132.54:443/health/updateMeasurement?Height="+String.valueOf(h)+"&Weight="
									+ WeightNew + "&WeightGoal="
									+ GWedit.getText().toString()
									+ "&GoalWeightDuration="
									+ duraText.getText().toString() + "&BMI="
									+ String.valueOf(bmiValue) + "");
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					int resCode = urlConnection.getResponseCode();
					Toast.makeText(getApplicationContext(),
							String.valueOf(resCode), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		};
		runnable.run();
		BMIText.setText(String.valueOf(bmiValue));
		/*
		 * Toast.makeText( getApplicationContext(),
		 * heightText.getEditableText().toString() + " --> " + h + " " +
		 * details[2] + " " + weightText.getText().toString() + "--> " + w + " "
		 * + details[3] + " " + bmiValue + " " + details[8],
		 * Toast.LENGTH_LONG).show();
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void performExit() {
		showDialog();
	}

	private double calculateBMI(double weight, double height) {

		return (float) (weight / (height * height));
	}

	public double getSumUserExercise() {
		double sum = 0;
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

		// Spinner Drop down elements

		sumE = db.getAllUserExerciseSum();
		// textView4.setText(String.valueOf(sum));
		// Toast.makeText(getApplicationContext(), details[0]+" "+details[1],
		// Toast.LENGTH_LONG).show();
		db.close();

		return sum;
	}

	public double getSumUserProduct() {
		double sum = 0;
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

		// Spinner Drop down elements
		sumM = 0;
		sumM = db.getAllUserProductSum();

		// textView3.setText(String.valueOf(sum));
		// Toast.makeText(getApplicationContext(), details[0]+" "+details[1],
		// Toast.LENGTH_LONG).show();
		db.close();

		return sum;
	}
}

package com.v.mypersonaltrainer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class GoalActivity extends Activity {
	private Spinner spinner1, spinner2;
	private Button btnSubmitExercise, btnSubmitMeal, btnAddMeal;
	ImageButton imageButtonGoal;
	public static ArrayList<String> lables = new ArrayList<String>();
	public static ArrayList<Integer> PID = new ArrayList<Integer>();
	public static ArrayList<String> lables2 = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_goal);
		addListenerOnButton();
		addListenerOnSpinnerItemSelection();
		addItemsOnSpinner1();
		addItemsOnSpinner2();
	}

	// add items into spinner dynamically
	public void addItemsOnSpinner2() {
		if (lables2.size() == 0) {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						URL url = new URL(
								"http://58.27.132.54:443/health/getExerciseListJSON?");
						// Proxy p=new Proxy(Type.HTTP, new
						// InetSocketAddress("10.1.20.17", 8080));
						// url.openConnection(proxy);
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
								lables2.add(c.getString("NAME"));
							}
							// Toast.makeText(getApplicationContext(),
							// "webservice",
							// Toast.LENGTH_SHORT).show();
						} else
							resCode = -1;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			runnable.run();
		}
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lables2);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner2.setAdapter(dataAdapter);
	}

	// add items into spinner dynamically
	public void addItemsOnSpinner1() {

		if (lables.size() == 0) {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						URL url = new URL(
								"http://58.27.132.54:443/health/getProductListJSON?");
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
								lables.add(c.getString("Product_Name"));
								PID.add(c.getInt("_id"));
							}
							// Toast.makeText(getApplicationContext(),
							// "webservice",
							// Toast.LENGTH_SHORT).show();
						} else
							resCode = -1;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			runnable.run();
		}
		/*
		 * // Spinner Drop down elements List<String> lables =
		 * db.getAllProductName(); db.close();
		 */

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		if (dataAdapter.getCount() == 0) {

			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, lables);

			// Drop down layout style - list view with radio button
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// attaching data adapter to spinner
			spinner1.setAdapter(dataAdapter);
		}
	}

	public void addListenerOnSpinnerItemSelection() {
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		// spinner1.setOnItemSelectedListener(new
		// CustomOnItemSelectedListener());
	}

	// get the selected dropdown list value
	public void addListenerOnButton() {

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		btnSubmitExercise = (Button) findViewById(R.id.btnSubmitExercise);
		btnSubmitMeal = (Button) findViewById(R.id.btnSubmitMeal);
		btnAddMeal = (Button) findViewById(R.id.btnAddMeal);
		imageButtonGoal = (ImageButton) findViewById(R.id.imageButtonGoal);
		imageButtonGoal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
			}
		});
		btnSubmitExercise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(
						GoalActivity.this,
						"OnClickListener : " + "\nSpinner 1 : "
								+ String.valueOf(spinner1.getSelectedItem())
								+ "\nSpinner 2 : "
								+ String.valueOf(spinner2.getSelectedItem()),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(),
						ExerciseActivity.class);
				intent.putExtra("exerciseId",
						(int) spinner2.getSelectedItemId());
				startActivity(intent);
			}

		});
		btnSubmitMeal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(
						GoalActivity.this,
						"OnClickListener : " + "\nSpinner 1 : "
								+ String.valueOf(spinner1.getSelectedItemId())
								+ "\nSpinner 2 : "
								+ String.valueOf(spinner2.getSelectedItem()),
						Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(getApplicationContext(),
						MealActivity.class);
				// String
				// strIntent=String.valueOf(spinner1.getSelectedItemId());
				intent.putExtra("productID",
						PID.get(spinner1.getSelectedItemPosition()));
				startActivity(intent);
			}
		});

		btnAddMeal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MealAddActivity.class);
				// String
				// strIntent=String.valueOf(spinner1.getSelectedItemId());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_goal, menu);
		return true;
	}

}

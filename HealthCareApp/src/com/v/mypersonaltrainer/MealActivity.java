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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.SQLException;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.database.sqlite.DataBaseHelper;

public class MealActivity extends Activity {
	TextView ProductName, ServingSize, ServingSizeWeight, Calories, Pro, TF,
			CHo, Sodium, TC, P, VA, VC, Cal, Iron;
	Button btnAddMeal;
	EditText amountMeal;
	ImageButton btnBackMealAdd;
	int productId;
	public static String caloriesTotal,ProductName1, Calories1, ServingSize1, ServingSizeWeight1, TF1, CHo1, Sodium1, TC1, Pro1, VA1, VC1, Cal1, Iron1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_meal);
		productId = getIntent().getExtras().getInt("productID");
		// Toast.makeText(getApplicationContext(), "PID -->"+productId,
		// Toast.LENGTH_LONG).show();
		addTextView();
		dbGetProductDetails();

		btnAddMeal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (amountMeal.getText().toString().contentEquals("")) {
					Toast.makeText(getApplicationContext(), "Enter the amount",
							Toast.LENGTH_LONG).show();

				} else {
					dbInsertUserProductDetails();
					//dbCountUserProductDetails();
				}
			}
		});

	}

	private void dbInsertUserProductDetails() {
		// TODO Auto-generated method stub
		Date d = new Date();
		String date = DateFormat.format("dd/MM/yyyy ", d.getTime()).toString();
		if (amountMeal.getText() == null) {
			amountMeal.setText(0);
		}
		
		/*DataBaseHelper db = new DataBaseHelper(getApplicationContext());
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
		db.insertUserProductDetails(
				date,
				Integer.parseInt(amountMeal.getText().toString()),
				Double.parseDouble(Calories.getText().toString())
						* Integer.parseInt(amountMeal.getText().toString()), 1,
				(productId + 1),
				Integer.parseInt(ServingSize.getText().toString()));
		db.getAllUserProductDetails();
		
		db.close();*/
		
		double calories=Integer.parseInt(amountMeal.getText().toString())*Double.parseDouble(Calories.getText().toString());
		caloriesTotal=String.valueOf(calories);
		Runnable runnable= new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL("http://58.27.132.54:443/health/insertUserProduct?Amount="+amountMeal.getText().toString()+"&Calories="+caloriesTotal+"&ServingSize="+ServingSize.getText().toString()+"&ProductId="+productId+"");
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

	private void dbCountUserProductDetails() {
		// TODO Auto-generated method stub

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
		db.getAllUserProductDetails();
		int count = db.getAllUserProductCount();
		Toast.makeText(getApplicationContext(), "Count is  $$$ " + count,
				Toast.LENGTH_LONG).show();
		db.close();
	}

	public void addTextView() {

		ProductName = (TextView) findViewById(R.id.ProName);
		Calories = (TextView) findViewById(R.id.Calories);
		ServingSize = (TextView) findViewById(R.id.SS);
		ServingSizeWeight = (TextView) findViewById(R.id.SSW);
		TF = (TextView) findViewById(R.id.TF);
		CHo = (TextView) findViewById(R.id.Cho);
		Sodium = (TextView) findViewById(R.id.Sod);
		TC = (TextView) findViewById(R.id.TC);
		Pro = (TextView) findViewById(R.id.Pro);
		VA = (TextView) findViewById(R.id.VA);
		VC = (TextView) findViewById(R.id.VC);
		Cal = (TextView) findViewById(R.id.Cal);
		Iron = (TextView) findViewById(R.id.Iron);
		btnAddMeal = (Button) findViewById(R.id.btnAddMeal);
		amountMeal = (EditText) findViewById(R.id.amountMeal);
		btnBackMealAdd = (ImageButton) findViewById(R.id.btnBackMealAdd2);
		btnBackMealAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						GoalActivity.class);
				startActivity(intent);
			}
		});
	}

	public void dbGetProductDetails() {
		//productName = getIntent().getExtras().getString("productName");
		//productId=productId+1;
		
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(
							"http://58.27.132.54:443/health/getIndividualProductListJSON?_id="
									+ String.valueOf(productId) + "");
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
							ProductName1=c.getString("Product_Name");
							ServingSize1=c.getString("ServingSize");
							ServingSizeWeight1=c.getString("ServingSizeWeight");
							Calories1=c.getString("Calories");
							TF1=c.getString("TotalFats");
							CHo1=c.getString("Cholesterol");
							Sodium1=c.getString("Sodium");
							TC1=c.getString("TotalCarbs");
							Pro1=c.getString("Protein");
							VA1=c.getString("VitaminA");
							VC1=c.getString("VitaminC");
							Cal1=c.getString("Calcium");
							Iron1=c.getString("Iron");
							}
					} else
						resCode = -1;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		runnable.run();
		ProductName.setText(ProductName1);
		Calories.setText(Calories1);
		ServingSize.setText(ServingSize1);
		ServingSizeWeight.setText(ServingSizeWeight1);
		TF.setText(TF1);
		CHo.setText(CHo1);
		Sodium.setText(Sodium1);
		TC.setText(TC1);
		Pro.setText(Pro1);
		VA.setText(VA1);
		VC.setText(VC1);
		Cal.setText(Cal1);
		Iron.setText(Iron1);
		
	}
}

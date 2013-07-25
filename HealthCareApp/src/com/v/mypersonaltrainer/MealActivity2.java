package com.v.mypersonaltrainer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;

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

public class MealActivity2 extends Activity {
	TextView ProductName, ServingSize, ServingSizeWeight, Calories, Pro, TF,
			CHo, Sodium, TC, P, VA, VC, Cal, Iron;
	EditText ProductNameText, ServingSizeText, ServingSizeWeightText,
			CaloriesText, ProText, TFText, CHoText, SodiumText, TCText, PText,
			VAText, VCText, CalText, IronText;
	Button btnAddMeal;
	EditText amountMeal;
	ImageButton btnBackMealAdd;
	int productId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_meal2);
		// Toast.makeText(getApplicationContext(), "PID -->"+productId,
		// Toast.LENGTH_LONG).show();
		addTextView();
		String ocrString = getIntent().getExtras().getString("str");
		Toast.makeText(getApplicationContext(), ocrString, Toast.LENGTH_LONG)
				.show();
		StringTokenizer st = new StringTokenizer(ocrString);

		// System.out.println("---- Split by space ------");
		while (st.hasMoreElements()) {
			if ((st.nextElement().equals("Calories"))) {
				CaloriesText.setText((CharSequence) st.nextElement());
			}
			/*
			 * else
			 * if((st.nextElement().equals("Total Fat"))||(st.nextElement().
			 * equals("Total F"))){ TFText.setText((CharSequence)
			 * st.nextElement()); } else
			 * if((st.nextElement().equals("Sodium"))||
			 * (st.nextElement().equals("Sod"))){
			 * SodiumText.setText((CharSequence) st.nextElement()); } else
			 * if((st
			 * .nextElement().equals("Cholesterol"))||(st.nextElement().equals
			 * ("Cho"))){ CHoText.setText((CharSequence) st.nextElement()); }
			 * else
			 * if((st.nextElement().equals("Protein"))||(st.nextElement().equals
			 * ("Pro"))){ ProText.setText((CharSequence) st.nextElement()); }
			 * else
			 * if((st.nextElement().equals("Vit"))||(st.nextElement().equals
			 * ("Vitnamin"))){ VAText.setText((CharSequence) st.nextElement());
			 * VCText.setText((CharSequence) st.nextElement()); }
			 */// System.out.println(st.nextElement());
		}
		TFText.setText("0");
		CHoText.setText("0");
		TCText.setText("0");
		SodiumText.setText("0");
		ProText.setText("0");
		VAText.setText("0");
		VCText.setText("0");
		CalText.setText("0");
		IronText.setText("0");

		// dbGetProductDetails();
		btnAddMeal = (Button) findViewById(R.id.btnAddOCRMeal);
		btnAddMeal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							URL url = new URL(
									"http://58.27.132.54:443/health/insertProduct?Product_Name="
											+ ProductNameText.getText()
													.toString() + "&Calories="
											+ CaloriesText.getText().toString()
											+ "&TotalFats="
											+ TFText.getText().toString()
											+ "&Cholesterol="
											+ CHoText.getText().toString()
											+ "&Sodium="
											+ SodiumText.getText().toString()
											+ "&TotalCarbs="
											+ TCText.getText().toString()
											+ "&Protein="
											+ ProText.getText().toString()
											+ "&VitaminA="
											+ VAText.getText().toString()
											+ "&VitaminC="
											+ VCText.getText().toString()
											+ "&Calcium="
											+ CalText.getText().toString()
											+ "&Iron="
											+ IronText.getText().toString()
											+ "");
							HttpURLConnection urlConnection = (HttpURLConnection) url
									.openConnection();
							int resCode = urlConnection.getResponseCode();
							Toast.makeText(getApplicationContext(),
									String.valueOf(resCode), Toast.LENGTH_LONG)
									.show();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				};
				runnable.run();

				Toast.makeText(getApplicationContext(),
						"Product added into DB", Toast.LENGTH_LONG).show();
			}
		});

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
		amountMeal = (EditText) findViewById(R.id.amountMeal);

		ProductNameText = (EditText) findViewById(R.id.editText2);
		CaloriesText = (EditText) findViewById(R.id.editText3);
		TFText = (EditText) findViewById(R.id.editText1);
		CHoText = (EditText) findViewById(R.id.EditText01);
		SodiumText = (EditText) findViewById(R.id.EditText03);
		TCText = (EditText) findViewById(R.id.EditText02);
		ProText = (EditText) findViewById(R.id.EditText08);
		VAText = (EditText) findViewById(R.id.EditText05);
		VCText = (EditText) findViewById(R.id.EditText06);
		CalText = (EditText) findViewById(R.id.EditText07);
		IronText = (EditText) findViewById(R.id.EditText04);

		btnBackMealAdd = (ImageButton) findViewById(R.id.btnBackMealAdd2);
		btnBackMealAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
			}
		});
	}

}

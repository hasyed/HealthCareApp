package com.v.mypersonaltrainer;

import java.io.IOException;
import android.content.Intent;
import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.database.sqlite.DataBaseHelper;

import edu.sfsu.cs.orange.ocr.CaptureActivity;

public class MealAddActivity extends Activity {
	EditText mealName;
	Button btnNewMealOCR,button2;
	ImageButton imageButtonOCR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mealadd);
		addTextView();
		
		//dbGetProductDetails();
	}
	
	public void addTextView(){
		mealName=(EditText) findViewById(R.id.mealName);
		imageButtonOCR=(ImageButton)findViewById(R.id.imageButtonOCR);
		button2=(Button)findViewById(R.id.btnBackMealAdd2);
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(getApplicationContext(), GoalActivity.class);
				startActivity(intent);
			}
		});
		imageButtonOCR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(getApplicationContext(), GoalActivity.class);
				startActivity(intent);
			}
		});
		
		btnNewMealOCR= (Button) findViewById(R.id.btnAddMealOCR);
		btnNewMealOCR.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(getApplicationContext(), CaptureActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/*public void dbGetProductDetails(){
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
			int productId=getIntent().getExtras().getInt("productID");
			// Spinner Drop down elements
			String[] details =new String[13];
			details = db.getDetailsOfProduct(productId);
		
			Toast.makeText(getApplicationContext(), details[1]+" "+details[2]+" "+details[3]+" ", Toast.LENGTH_LONG).show();
			db.close();

	}
*/}

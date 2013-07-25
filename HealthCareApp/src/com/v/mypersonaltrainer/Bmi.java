package com.v.mypersonaltrainer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.database.sqlite.DataBaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Bmi extends Activity {
	 /** Called when the activity is first created. */
	 
	 // declare adaptors to bind with spinners
	 ArrayAdapter<String> heightFeetsAdapter;
	 ArrayAdapter<String> heightMetersAdapter;
	 ArrayAdapter<String> weightLibsAdapter;
	 ArrayAdapter<String> weightKgsAdapter;
	 
	 // declare the references for the UI elements
	 ImageButton backButton;
	 Spinner weightSpinner;
	 Spinner heightSpinner;
	 Spinner weightUnitSpinner;
	 Spinner heightUnitSpinner;
	 TextView bmiValueText;
	 TextView bmiDescriptionText;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bmi);
		

		  // load the references to the widgets

		  weightSpinner = (Spinner) findViewById(R.id.spinner1);
		  weightUnitSpinner = (Spinner) findViewById(R.id.spinner2);
		  heightSpinner = (Spinner) findViewById(R.id.spinner3);
		  heightUnitSpinner = (Spinner) findViewById(R.id.spinner4);
		  bmiValueText = (TextView) findViewById(R.id.bmivalue);
		  bmiDescriptionText = (TextView) findViewById(R.id.bmidesc);
		  backButton=(ImageButton)findViewById(R.id.btnBackBMI);
		  // initialize the value range for the spinners
		  backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		});

		  initializeSpinnerAdapters();
		 
		  // load the default values for the spinners
		  loadLibsValueRange();
		  loadFeetsValueRange();
		 
		  // add listeners to the unit changes

		  addListernsToUnitChanges();
		 }
		 
		 // handler that we defined in "onClick" attribute of the button
		 // get called when the button is clicked
		 public void calculateClickHandler(View view) {

		  // make sure we handle the click of the calculator button
		  if (view.getId() == R.id.calculatebmi) {

		 
		   // get the users values from the spinners (converted to floats and
		   // metrics units)
		   float weight = getSelectedWeight();
		   float height = getSelectedHeight();
		 
		   // calculate the bmi value and set it in the text

		   float bmiValue = calculateBMI(weight, height);
		   bmiValueText.setText(bmiValue + "");
		 
		   // interpret the meaning of the bmi value and set it in the text
		   int bmiInterpretation = interpretBMI(bmiValue);
		   bmiDescriptionText.setText(getResources().getString(

		     bmiInterpretation));
		 
		   // color for the bmi text fields
		   int bmiColor = colorBMI(bmiValue);
		   bmiValueText.setTextColor(getResources().getColor(bmiColor));
		   bmiDescriptionText.setTextColor(getResources().getColor(bmiColor));
		   updateUserDetails();
		   
		  }

		 }
		 
		 // retrieve the weight from the spinner control converted to kg
		 public float getSelectedWeight() {

		  String selectedWeightValue = (String) weightSpinner.getSelectedItem();
		  if (weightUnitSpinner.getSelectedItemPosition() == 0) {

		   // the position is libs, so convert to kg and return
		   return (float) (Float.parseFloat(selectedWeightValue) * 0.45359237);
		  } else {

		   // already kg is selected, so no need to covert (just cast to float)
		   return Float.parseFloat(selectedWeightValue);
		  }
		 }

		 
		 // retrieve the hight from the spinner control convented to me
		 public float getSelectedHeight() {
		  String selectedHeightValue = (String) heightSpinner.getSelectedItem();
		  if (heightUnitSpinner.getSelectedItemPosition() == 0) {

		   // the position is feets and inches, so convert to meters and return
		   String feets = selectedHeightValue.substring(0, 1);
		   String inches = selectedHeightValue.substring(2, 4);
		   return (float) (Float.parseFloat(feets) * 0.3048)

		     + (float) (Float.parseFloat(inches) * 0.0254);
		  } else {

		   // already meters is selected, so no need to covert (just cast to
		   // float)
		   return Float.parseFloat(selectedHeightValue);
		  }

		 }
		 
		 // the formula to calculate the BMI index
		 // check for http://en.wikipedia.org/wiki/Body_mass_index
		 private float calculateBMI(float weight, float height) {

		  return (float) (weight / (height * height));
		 }
		 
		 // returns the string name defined in strings.xml

		 // that interpret the BMI
		 private int interpretBMI(float bmiValue) {

		  if (bmiValue < 16) {
		   return R.string.bmiSUnder;
		  } else if (bmiValue < 18.5) {

		   return R.string.bmiUnder;
		  } else if (bmiValue < 25) {

		   return R.string.bmiNormal;
		  } else if (bmiValue < 30) {

		   return R.string.bmiOver;
		  } else {
		   return R.string.bmiObese;
		  }

		 }
		 
		 // returns the color name defined in strings.xml
		 // that represent the BMI
		 private int colorBMI(float bmiValue) {

		  if (bmiValue < 16) {
		   return R.color.colorRed;
		  } else if (bmiValue < 18.5) {

		   return R.color.colorYellow;
		  } else if (bmiValue < 25) {

		   return R.color.colorGreen;
		  } else if (bmiValue < 30) {

		   return R.color.colorYellow;
		  } else {
		   return R.color.colorRed;
		  }

		 }
		 
		 // adding listers to unit changing spinners, as we need to change the
		 // value range accordingly
		 public void addListernsToUnitChanges() {

		  // listener to the weight unit
		  //weightUnitSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
			 
			 weightUnitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		     public void onItemSelected(AdapterView<?> parent,
		       View view, int row, long id) {

		      // load the relevent units and the values
		      if (row == 0) {
		       // libs is selected
		       loadLibsValueRange();
		      } else {

		       // kg is selected
		       loadKgsValueRange();
		      }
		     }
		 
		     public void onNothingSelected(AdapterView<?> arg0) {

		      // Nothing to do here
		     }
		    });
		 
		  // listener to the height unit
		  heightUnitSpinner
		    .setOnItemSelectedListener(new OnItemSelectedListener() {

		     public void onItemSelected(AdapterView<?> parent,
		       View view, int row, long id) {

		      // load the relevent units and the values
		      if (row == 0) {
		       // feets is selected
		       loadFeetsValueRange();
		      } else {

		       // meters is selected
		       loadMetersValueRange();
		      }
		     }
		 
		     public void onNothingSelected(AdapterView<?> arg0) {

		      // Nothing to do here
		     }
		    });
		 }
		 
		 // load the libs value range to the weight spinner
		 public void loadLibsValueRange() {

		  weightSpinner.setAdapter(weightLibsAdapter);
		  // set the default lib value
		  weightSpinner.setSelection(weightLibsAdapter.getPosition("170"));
		 }

		 
		 // load the kg value range to the weight spinner
		 public void loadKgsValueRange() {
		  weightSpinner.setAdapter(weightKgsAdapter);
		  // set the default vaule for kg

		  weightSpinner.setSelection(weightKgsAdapter.getPosition(" 77"));
		 }
		 
		 // load the feets value range to the height spinner
		 public void loadFeetsValueRange() {

		  heightSpinner.setAdapter(heightFeetsAdapter);
		  // set the default value to feets
		  heightSpinner.setSelection(heightFeetsAdapter.getPosition("5\"05'"));
		 }

		 
		 // load the meters value range to the height spinner
		 public void loadMetersValueRange() {
		  heightSpinner.setAdapter(heightMetersAdapter);
		  // set the default value to meters

		  heightSpinner.setSelection(heightMetersAdapter.getPosition("1.65"));
		 }
		 
		 // load the value range of all the units to adapters
		 // we would assign adapters to the spinners based on the users selection

		 public void initializeSpinnerAdapters() {
		 
		  String[] weightLibs = new String[300];
		  // loading 1.0 to 300 to the weight in libs

		  int k = 299;
		  for (int i = 1; i <= 300; i++) {

		   weightLibs[k--] = String.format("%3d", i);
		  }

		  // initialize the weightLibsAdapter with the weightLibs values
		  weightLibsAdapter = new ArrayAdapter<String>(this,
		    android.R.layout.simple_spinner_item, weightLibs);
		 
		  String[] weightKgs = new String[200];
		  // loading 1.0 to 200 to the weight in kgs

		  k = 199;
		  for (int i = 1; i <= 200; i++) {

		   weightKgs[k--] = String.format("%3d", i);
		  }

		  // initialize the weightKgsAdapter with the weightKgs values
		  weightKgsAdapter = new ArrayAdapter<String>(this,
		    android.R.layout.simple_spinner_item, weightKgs);
		 
		  String[] heightFeets = new String[60];
		  // loading 3"0' to 7"11' to the height in feet/inch

		  k = 59;
		  for (int i = 3; i < 8; i++) {

		   for (int j = 0; j < 12; j++) {
		    heightFeets[k--] = i + "\"" + String.format("%02d", j) + "'";
		   }

		  }
		  // initialize the heightFeetAdapter with the heightFeets values
		  heightFeetsAdapter = new ArrayAdapter<String>(this,
		    android.R.layout.simple_spinner_item, heightFeets);
		 
		  String[] heightMeters = new String[300];
		  // loading 0.0 to 2.9 to the height in m/cm

		  k = 299;
		  for (int i = 0; i < 3; i++) {

		   for (int j = 0; j < 100; j++) {
		    heightMeters[k--] = i + "." + j;
		   }

		  }
		  // initialize the heightMetersAdapter with the heightMeters values
		  heightMetersAdapter = new ArrayAdapter<String>(this,
		    android.R.layout.simple_spinner_item, heightMeters);
		 
		 }
	




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_bmi, menu);
		return true;
	}

	public void updateUserDetails() {

		double h = getSelectedHeight();
		double w = getSelectedWeight();
		double bmiValue = calculateBMI(w, h);
		
		final String HeightNew=String.valueOf(h);
		final String WeightNew=String.valueOf(w);
		final String BMIValue=String.valueOf(bmiValue);
		Runnable runnable= new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
					try {
						URL url = new URL("http://58.27.132.54:443/health/updateWeightHeight?Weight="+WeightNew+"&Height="+HeightNew+"&BMI="+BMIValue+"");
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
		Toast.makeText(
				getApplicationContext(),
				" --> " + h + " "+ "--> " +w+" "+ bmiValue+ " " , Toast.LENGTH_LONG).show();

	}

	private double calculateBMI(double w, double h) {
		// TODO Auto-generated method stub
		return (double) (w / (h* h));
	}


}

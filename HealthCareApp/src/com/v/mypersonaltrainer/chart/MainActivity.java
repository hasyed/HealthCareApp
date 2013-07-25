package com.v.mypersonaltrainer.chart;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.database.sqlite.DataBaseHelper;
import com.v.mypersonaltrainer.R;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String[] mMonth = new String[] { "10", "11", "12", "13", "14",
			"15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };
	ImageButton imageButtonChart;
	ArrayList<Integer> calBurntArrayList = new ArrayList<Integer>();
	int[] daily = new int[15];
	double h;
	int age;
	double W;
	double GW;
	int duration;
	int calG;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dbGetSumCalories();
		// setContentView(R.layout.activity_main);
		// imageButtonChart=(ImageButton)findViewById(R.id.imageButtonChart);
		openChart();
		// Getting reference to the button btn_chart
	}

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	private void openChart() {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL("http://58.27.132.54:443/health/getGraphJSON?");
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
							Integer ExerciseName = c.getInt("CaloriesDiff");
							calBurntArrayList.add(ExerciseName);
							// Toast.makeText(getApplicationContext(),
							// ExerciseName+" ", Toast.LENGTH_LONG).show();
						}
					} else
						resCode = -1;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		runnable.run();
		double sumCal=0;
		ArrayList<Integer> calRight= new ArrayList<Integer>();
		for (int i = calBurntArrayList.size()-1; i >=0 ; i--) {
			
		calRight.add(calBurntArrayList.get(i).intValue());
		}
		for (int j = 0; j < calRight.size(); j++) {
			daily[j] = calRight.get(j).intValue();
			sumCal=daily[j]+sumCal;	
		}
		
	
		sumCal=sumCal/15;
		int[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

		/*
		 * int[] daily = { 1714, 1331, 1902, 1424, 1815, 1857, 1508, 1804, 1818,
		 * 1550, 1464, 1983, 1497, 1479, 1869, 1978 };
		 */
		
		Runnable runnable2 = new Runnable() {

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
							h = c.getDouble("Height");
							W = c.getDouble("Weight");
							GW = c.getDouble("WeightGoal");
							duration = c.getInt("GoalWeightDuration");
							age = c.getInt("Age");
						}
					} else
						resCode = -1;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		runnable2.run();
		
		h = h * 100;
		double calories = ((10 * (W)) + (6.5 * h) - (5 * age) * 1.55);
		double sum = GW- W;
		double cal = Math.abs(sum)
				/ (duration / 7) * 2;
		calories = calories - cal;
		int[] goal= new int[15];
		int[] goal1= new int[15];
		for (int i = 0; i < 15; i++) {
			goal[i]=(int)calories;
			goal1[i]=(int) sumCal;
		}
		 /*= { 1672, 1672, 1672, 1672, 1672, 1672, 1672, 1672, 1672,
				1672, 1672, 1672, 1672, 1672, 1672, 1672 };
		
		int[] goal1 = { 1687, 1687, 1687, 1687, 1687, 1687, 1687, 1687, 1687,
				1687, 1687, 1687, 1687, 1687, 1687, 1687 };
		*/// Creating an XYSeries for Income
		// CategorySeries incomeSeries = new CategorySeries("Income");
		XYSeries dailySeries = new XYSeries("Daily");
		// Creating an XYSeries for Income
		XYSeries goalSeries = new XYSeries("Goal");
		// Adding data to Income and Expense Series

		XYSeries avgSeries = new XYSeries("Avg");
		// Adding data to Income and Expense Series
		for (int i = 0; i < x.length; i++) {
			dailySeries.add(i, daily[i]);
			goalSeries.add(i, goal[i]);
			avgSeries.add(i, goal1[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(dailySeries);
		// Adding Expense Series to dataset
		dataset.addSeries(goalSeries);

		dataset.addSeries(avgSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		incomeRenderer.setColor(Color.rgb(130, 130, 230));
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setLineWidth(2);
		incomeRenderer.setDisplayChartValues(true);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.rgb(220, 80, 80));
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2);
		expenseRenderer.setDisplayChartValues(true);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer goalRenderer = new XYSeriesRenderer();
		goalRenderer.setColor(Color.rgb(200, 100, 150));
		goalRenderer.setFillPoints(true);
		goalRenderer.setLineWidth(2);
		goalRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Daily Calories vs Goal Calories");
		multiRenderer.setXTitle("June 2013");
		multiRenderer.setYTitle("Calories");
		multiRenderer.setZoomButtonsVisible(true);
		for (int i = 0; i < x.length; i++) {
			multiRenderer.addXTextLabel(i, mMonth[i]);
		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);
		multiRenderer.addSeriesRenderer(goalRenderer);

		// Creating an intent to plot bar chart using dataset and
		// multipleRenderer
		Intent intent = ChartFactory.getLineChartIntent(getBaseContext(),
				dataset, multiRenderer);

		// Start Activity
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
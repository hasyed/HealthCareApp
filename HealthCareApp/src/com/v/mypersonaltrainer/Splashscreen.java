package com.v.mypersonaltrainer;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Splashscreen extends Activity {

	ProgressBar myProgressBar;
	int myProgress = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_splashscreen);

		myProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

		new Thread(myThread).start();
	}

	private Runnable myThread = new Runnable()
	{
		@Override
		public void run()
		{
			while (myProgress < 100)
			{
				try
				{
					myHandle.sendMessage(myHandle.obtainMessage());
					Thread.sleep(250);
				}
				catch (Throwable t)
				{
				}
			}

			Intent mainIntent = new Intent(Splashscreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);;

			Splashscreen.this.startActivity(mainIntent);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Splashscreen.this.finish();
		}

		Handler myHandle = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				myProgress = myProgress + 10;
				myProgressBar.setProgress(myProgress);
			}
		};
	};
	
}

package com.mesachinsingh.ai;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener
{
	private TextToSpeech tts;
	EditText et1;
	Button b1;
	String speech,greeting = null;

	SharedPreferences ShP;
	
	Calendar time = Calendar.getInstance();
	int hr,am_pm,date;

	private final int SPEECH_RECOGNITION_CODE = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
		
		tts = new TextToSpeech(this,this);
		
		Button micb = (Button)findViewById(R.id.mic1);
		micb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				speechToText();
				
			}
		});  
		
		SharedPreferences ShP = this.getSharedPreferences("MyPrefs", Context.MODE_WORLD_READABLE);
		
	    final Boolean isOpened = ShP.getBoolean("isOpened" , false);
		if(!isOpened) {
			Intent fintent = new Intent(MainActivity.this,FirstTime.class);
			MainActivity.this.startActivity(fintent);
		}
				
	}
	
	@Override
	public void onInit(int p1)
	{
		if(p1 != TextToSpeech.ERROR) {
			tts.setLanguage(Locale.US);
			Greeting();
		}
	}
		
	
	public void speakOut(String toSpeak) {
		tts.speak(toSpeak,TextToSpeech.QUEUE_ADD,null);
	}
	
	
	  public void Greeting() {
		SharedPreferences ShP = this.getSharedPreferences("MyPrefs", Context.MODE_WORLD_READABLE);
		String fname = ShP.getString("FirstName","");
		hr = time.get(Calendar.HOUR);
		date = time.get(Calendar.DATE);
		am_pm = time.get(Calendar.AM_PM);
		if(am_pm == 0) {
			if(hr >= 4 & hr <= 11)
				greeting = "Good Morning";
			else
				greeting = "Good Evening";
		}
		else {
			if(hr >= 0 & hr < 4)
				greeting = "Good Afternoon";
			else
				greeting = "Good Evening";
		}
	    String togreet = ShP.getString("Greeting", greeting);
		if(greeting != togreet) {
			speakOut(greeting);
		}
		
	  }
	
	private void speechToText() {
		Intent stt = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		stt.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.US);
		stt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		try {
			startActivityForResult(stt,SPEECH_RECOGNITION_CODE);
		} catch( ActivityNotFoundException e){
			
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case SPEECH_RECOGNITION_CODE: {
				if(resultCode == RESULT_OK && null!=data) {
					ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					speech = result.get(0);
					analyseActivity(speech);
				}
			}
		}
	}
	
	public void analyseActivity(String speech) {
		switch (speech) {
			case "exit":
			case "stop":
				hr = time.get(Calendar.HOUR);
				if(am_pm == 0) {
				    if(hr >= 0 & hr <= 3)
					    greeting = "Good Night Sir";
				}
				else 
					greeting = "Have a nice day sir";
					
				speakOut(greeting);
				while(tts.isSpeaking()) {}
				finish();
				break;
			case "control" :
				Intent blue = new Intent(MainActivity.this,Bluetooth.class);
				MainActivity.this.startActivity(blue);
				break;
			}
	}
	
    @Override
	public void onDestroy() {
		if(tts != null) {
			tts.stop();
			tts.shutdown();
		}  
		super.onPause();
	} 				

	@Override
	public void onBackPressed()
	{
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			MainActivity.this);

        // set title
        alertDialogBuilder.setTitle("Exit");
		
        // set dialog message
        alertDialogBuilder
			.setMessage("Do you really want to exit?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					finish();
					System.exit(1);
				}
			})
			.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
	       
}

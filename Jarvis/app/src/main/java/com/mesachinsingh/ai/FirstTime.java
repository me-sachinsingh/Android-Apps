package com.mesachinsingh.ai;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;

public class FirstTime extends Activity
{
	CheckBox Male,Female;
	Button submit;
	EditText fname,lname;
	String gender;
	
	SharedPreferences ShP;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.userdetail);
		
		submit = (Button)findViewById(R.id.submit);
		fname = (EditText)findViewById(R.id.fname);
		lname = (EditText)findViewById(R.id.lname);
		
		ShP = getSharedPreferences("MyPrefs",Context.MODE_WORLD_WRITEABLE);
		
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View  v) {
				if(fname.getText().toString().equals("") || lname.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
				}
				else {
					String FirstName = fname.getText().toString();
					String LastName = fname.getText().toString();
					SharedPreferences.Editor editor = ShP.edit();
					editor.putString("FirstName",FirstName);
					editor.putString("LastName",LastName);
					editor.putString("Gender",gender);
					editor.putString("Greeting", "");
					editor.putBoolean("isOpened",true);
					editor.commit();
		            Intent intent = new Intent(FirstTime.this,MainActivity.class);
		            FirstTime.this.startActivity(intent);
				 }
			}
		});
	}
	
	public void onCheckBoxClicked(View view) {
		Male = (CheckBox)findViewById(R.id.CheckBoxM);
		Female = (CheckBox)findViewById(R.id.CheckBoxF);
		
		Male.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gender = "Male";
				if(Female.isChecked())
					Female.setChecked(false);
			}
		});
		
		Female.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gender = "Female";
				if(Male.isChecked())
					Male.setChecked(false);
			}
		});
	}
		
	@Override
	public void onBackPressed()
	{
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			FirstTime.this);

        // set title
        alertDialogBuilder.setTitle("Exit");

        // set dialog message
        alertDialogBuilder
			.setMessage("Do you really want to exit?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					moveTaskToBack(false);
					// current activity
					System.exit(1);
					finish();
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

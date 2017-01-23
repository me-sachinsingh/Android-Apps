
package com.mesachinsingh.ai;

import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothDevice;
import java.util.UUID;
import java.io.OutputStream;
import java.io.IOException;
import android.content.Intent;

public class Bluetooth extends Activity
{
	Button button;
	EditText cmd;
	String command;

	MainActivity mainAct = new MainActivity();

	private BluetoothAdapter mybluetooth;
	BluetoothSocket mSocket;
	OutputStream Ostream;
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.command_view);
		
		mybluetooth = BluetoothAdapter.getDefaultAdapter();

		if(!mybluetooth.isEnabled())  {
			mybluetooth.enable();
		}

		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{}

	    String Add = "98:D3:31:40:54:FF";
		bluetooth(Add);

		button=(Button)findViewById(R.id.commandsend);
		cmd=(EditText)findViewById(R.id.editcommand);
	}

	public void bluetooth(String address) {
		BluetoothDevice device = mybluetooth.getRemoteDevice(address);
		if(device.BOND_BONDED == 0) {
			Intent i = new Intent(Bluetooth.this,MainActivity.class);
			startActivity(i);
		}
			
		try
		{
			mSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);

		}
		catch (IOException e)
		{
			Toast.makeText(Bluetooth.this,"Exception",Toast.LENGTH_SHORT).show();
		}
		try
		{
			if(mybluetooth.isDiscovering()){
				mybluetooth.cancelDiscovery();
			}
			mSocket.connect();
			Ostream = mSocket.getOutputStream();
			Toast.makeText(Bluetooth.this,"Connected",Toast.LENGTH_SHORT).show();
			mainAct.speakOut("Connected to Morpheus");
		}
		catch (IOException e)
		{
			try 
			{
				mSocket.close();
			} catch(IOException f) {

			} }

	}

	public void sendData(View v) {
		String command = cmd.getText().toString();
		byte[] data = command.getBytes();
		try {
			Ostream.write(data);
		} catch(IOException e) {}
		cmd.setText("");
	}
}

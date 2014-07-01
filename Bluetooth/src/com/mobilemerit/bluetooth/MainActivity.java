package com.mobilemerit.bluetooth;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilemerit.bluetooth.R;

public class MainActivity extends Activity implements OnClickListener {
	private static final int REQUEST_ENABLE_BT = 1;
	private Button onOffBtn;
	private Button listBtn;
	private Button findBtn;
	private Button visibleBtn;
	private TextView text;
	private BluetoothAdapter myBluetoothAdapter;
	private Set<BluetoothDevice> pairedDevices;
	private ListView myListView;
	private ArrayAdapter<String> BTArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// take an instance of BluetoothAdapter - Bluetooth radio
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			onOffBtn.setEnabled(false);
			listBtn.setEnabled(false);
			findBtn.setEnabled(false);
			visibleBtn.setEnabled(false);
			text.setText("Status: not supported");
			Toast.makeText(getApplicationContext(),
					"Your device does not support Bluetooth", Toast.LENGTH_LONG)
					.show();
		} else {
			text = (TextView) findViewById(R.id.text);
			onOffBtn = (Button) findViewById(R.id.turnOn);
			onOffBtn.setOnClickListener(this);
			visibleBtn = (Button) findViewById(R.id.visible);
			visibleBtn.setOnClickListener(this);
			listBtn = (Button) findViewById(R.id.paired);
			listBtn.setOnClickListener(this);
			findBtn = (Button) findViewById(R.id.search);
			findBtn.setOnClickListener(this);
			myListView = (ListView) findViewById(R.id.listView1);

			// create the arrayAdapter that contains the BTDevices, and set it
			// to the ListView
			BTArrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1);
			myListView.setAdapter(BTArrayAdapter);
			/*
			 * checking while app start bluetooth is On or Off if bluetooth is
			 * on then search devices and set button and status "ON" if
			 * bluetooth is off then set button and status "OFF"
			 */
			if (!myBluetoothAdapter.isEnabled()) {
				onOffBtn.setText("Turn OFF");
				text.setText("Status: Disabled");
			} else {
				onOffBtn.setText("Turn ON");
				text.setText("Status: Enabled");
				find();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.turnOn:
			bluetoothOnOff();
			break;
		case R.id.visible:
			visible();
			break;
		case R.id.paired:
			list();
			break;
		case R.id.search:
			find();
			break;
		default:
			break;
		}
	}

	/* to enable and disable bluetooth */
	public void bluetoothOnOff() {
		if (!myBluetoothAdapter.isEnabled()) {
			Intent turnOnIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);

		} else {
			myBluetoothAdapter.disable();
			onOffBtn.setText("Turn OFF");
			BTArrayAdapter.clear();
			Toast.makeText(getApplicationContext(), "Bluetooth turned off",
					Toast.LENGTH_SHORT).show();
		}
	}

	/* to make visible your device to other devices */
	public void visible() {
		Intent getVisible = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(getVisible, 0);
	}

	/* to search pair devices */
	public void list() {
		if (!myBluetoothAdapter.isEnabled()) {
			Toast.makeText(getApplicationContext(), "First Trun On Bluetooth",
					Toast.LENGTH_SHORT).show();
		} else {
			// get paired devices
			pairedDevices = myBluetoothAdapter.getBondedDevices();
			// put it's one to the adapter
			BTArrayAdapter.clear();
			for (BluetoothDevice device : pairedDevices)
				BTArrayAdapter.add(device.getName() + "\n"
						+ device.getAddress());
			Toast.makeText(getApplicationContext(), "Show Paired Devices",
					Toast.LENGTH_SHORT).show();
		}
	}

	/* to find all available devices */
	public void find() {
		if (!myBluetoothAdapter.isEnabled()) {
			Toast.makeText(getApplicationContext(), "First Trun On Bluetooth",
					Toast.LENGTH_SHORT).show();
		} else {
			BTArrayAdapter.clear();
			myBluetoothAdapter.startDiscovery();
			registerReceiver(bReceiver, new IntentFilter(
					BluetoothDevice.ACTION_FOUND));
		}
	}

	final BroadcastReceiver bReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// add the name and the MAC address of the object to the
				// arrayAdapter
				BTArrayAdapter.add(device.getName() + "\n"
						+ device.getAddress());
				BTArrayAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (myBluetoothAdapter.isEnabled()) {
				text.setText("Status: Enabled");
				onOffBtn.setText("Turn ON");
				Toast.makeText(getApplicationContext(), "Bluetooth turned on",
						Toast.LENGTH_SHORT).show();
				find();
			} else {
				text.setText("Status: Disabled");
				onOffBtn.setText("Turn OFF");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bReceiver);
	}
}

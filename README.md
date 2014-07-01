Bluetooth
=========

The Android platform includes support for the Bluetooth network stack, which allows a device to wirelessly exchange data with other Bluetooth devices. The application framework provides access to the Bluetooth functionality through the Android Bluetooth APIs. These APIs let applications wirelessly connect to other Bluetooth devices, enabling point-to-point and multipoint wireless features. 
The BluetoothAdapter lets you perform fundamental Bluetooth tasks, such as initiate device discovery, query a list of bonded (paired) devices, instantiate a BluetoothDevice using a known MAC address, and create aBluetoothServerSocket to listen for connection requests from other devices, and start a scan for Bluetooth LE devices. this is your starting point for all Bluetooth actions.
 Once you have the local adapter, you can get a set of BluetoothDevice objects representing all paired devices with getBondedDevices(); start device discovery withstartDiscovery();. 
Now by using Intent you can perform various action. This action is “public static final String” like ACTION_REQUEST_ENABLE this is used to turn on Bluetooth. ACTION_REQUEST_DISCOVERABLE this action use to discover your Bluetooth to other devices. ACTION_FOUND Broadcast Action: use to Remote device discovered.


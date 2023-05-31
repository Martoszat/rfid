package com.zebra.rfid.demo.sdksample;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.INVENTORY_STATE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.LED_COLOR;
import com.zebra.rfid.api3.LedInfo;
import com.zebra.rfid.api3.LoginInfo;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.READER_TYPE;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.ReaderManagement;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.SECURE_MODE;
import com.zebra.rfid.api3.SESSION;
import com.zebra.rfid.api3.SL_FLAG;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.api3.TriggerInfo;


public class MainActivity extends AppCompatActivity implements  Readers.RFIDReaderEventHandler {

    public TextView statusTextViewRFID = null;
    private TextView textrfid;
    private TextView testStatus;

    private int MAX_POWER = 270;
    RFIDHandler rfidHandler;
    final static String TAG = "RFID_SAMPLE";
    RFIDReader reader = new RFIDReader("192.168.15.122", 5084, 1000);
    private EventHandler eventHandler;

    ReaderManagement readerManagement = new ReaderManagement();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            Thread rfidThr = new Thread(rfid);
            rfidThr.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // UI
        statusTextViewRFID = findViewById(R.id.textStatus);
        textrfid = findViewById(R.id.textViewdata);
        testStatus = findViewById(R.id.testStatus);

        // RFID Handler
        rfidHandler = new RFIDHandler();
        rfidHandler.onCreate(this);

        // set up button click listener
        Button test = findViewById(R.id.button);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = Test1();
//                testStatus.setText(result);
//                Boolean result = reader.isConnected();
//                String result1 = null;
//                try {
//                    result1 = String.valueOf(reader.versionInfo());
//                } catch (InvalidUsageException e) {
//                    throw new RuntimeException(e);
//                }
//                String result = rfidHandler.Test1();
                testStatus.setText(result);
            }
        });

        Button test2 = findViewById(R.id.button2);
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ConfigureReader();
                String result = Test2();
                testStatus.setText(result);
            }
        });

        Button defaultButton = findViewById(R.id.button3);
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String result = rfidHandler.Defaults(reader);
//                testStatus.setText(result);
//                rfidHandler.ConfigureReaderExtern(reader);
                ConfigureReader();
//                try {
//                    Thread rfidThr = new Thread(rfidstatus);
//                    rfidThr.start();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                LoginInfo loginInfo = new LoginInfo("FX9600F2B769", "admin", "change", SECURE_MODE.HTTP, false);
//                try {
//                    readerManagement.login(loginInfo, READER_TYPE.FX);
//                } catch (InvalidUsageException e) {
//                    e.printStackTrace();
//                } catch (OperationFailureException e) {
//                    e.printStackTrace();
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                LedInfo ledInfo = new LedInfo();
//                ledInfo.setLEDColor(LED_COLOR.LED_RED);
//                ledInfo.setDurationSeconds(0);
//                ledInfo.setBlink(true);
//
//                try {
//                    readerManagement.setUserLED(ledInfo);
//                } catch (InvalidUsageException e) {
//                    e.printStackTrace();
//                } catch (OperationFailureException e) {
//                    e.printStackTrace();
//                }


            }
        });


    }

//    private void ConfigureReader() {
//        if (reader.isConnected()) {
//            TriggerInfo triggerInfo = new TriggerInfo();
//            triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
//            triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
//            try {
//                // receive events from reader
//                if (eventHandler == null)
//                    eventHandler = new EventHandler();
//                reader.Events.addEventsListener(eventHandler);
//                reader.Events.setInventoryStartEvent(true);
//                reader.Events.setInventoryStopEvent(true);
//                // tag event with tag data
//                reader.Events.setTagReadEvent(true);
//                // application will collect tag using getReadTags API
//                reader.Events.setAttachTagDataWithReadEvent(false);
//
//            } catch (InvalidUsageException e) {
//                e.printStackTrace();
//            } catch (OperationFailureException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        rfidHandler.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String status = rfidHandler.onResume();
        statusTextViewRFID.setText(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidHandler.onDestroy();
    }


//    @Override
//    public void handleTagdata(TagData[] tagData) {
//        final StringBuilder sb = new StringBuilder();
//        for (int index = 0; index < tagData.length; index++) {
//            sb.append(tagData[index].getTagID() + "\n");
//        }
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textrfid.append(sb.toString());
//            }
//        });
//    }

//    @Override
//    public void handleTriggerPress(boolean pressed) {
//        if (pressed) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    textrfid.setText("");
//                }
//            });
//            rfidHandler.performInventory();
//        } else
//            rfidHandler.stopInventory();
//    }



    private Runnable rfidstatus = new Runnable() {
        @Override
        public void run() {
            try {
                Looper.prepare();
                try {
//                    reader.Config.getDeviceStatus(true, false, false);
                    LoginInfo loginInfo = new LoginInfo("192.168.15.122", "admin", "change", SECURE_MODE.HTTP, false);

                    try {
                        readerManagement.login(loginInfo, READER_TYPE.FX);
                    } catch (InvalidUsageException e) {
                        e.printStackTrace();
                    } catch (OperationFailureException e) {
                        e.printStackTrace();
                    }

                    LedInfo ledInfo = new LedInfo();
                    ledInfo.setLEDColor(LED_COLOR.LED_RED);
                    ledInfo.setDurationSeconds(0);
                    ledInfo.setBlink(true);

                    try {
                        readerManagement.setUserLED(ledInfo);
                    } catch (InvalidUsageException e) {
                        e.printStackTrace();
                    } catch (OperationFailureException e) {
                        e.printStackTrace();
                    }
//                    TagData[] myTags = reader.Actions.getReadTags(100);
//                    if (myTags != null) {
//                        for (int index = 0; index < myTags.length; index++) {
//                            Log.d(TAG, "Tag ID " + myTags[index].getTagID());
//                            if (myTags[index].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
//                                    myTags[index].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
//                                if (myTags[index].getMemoryBankData().length() > 0) {
//                                    Log.d(TAG, " Mem Bank Data " + myTags[index].getMemoryBankData());
//                                }
//                            }
//                            if (myTags[index].isContainsLocationInfo()) {
//                                short dist = myTags[index].LocationInfo.getRelativeDistance();
//                                Log.d(TAG, "Tag relative distance " + dist);
//                            }
//                        }
//                    }
                }catch (Exception e) {
                    Log.e("Data read final", e.getMessage() != null ? e.getMessage() : e.toString());
                }

            } catch (Exception de) {
                throw de;
            }
        }
    };

    private Runnable rfid = new Runnable() {
        @Override
        public void run() {
            try {
                Looper.prepare();
                try {
                    reader.connect();
                } catch (InvalidUsageException e) {
                    throw new RuntimeException(e);
                } catch (OperationFailureException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    Log.e("Data read final", e.getMessage() != null ? e.getMessage() : e.toString());
                }
                Log.d(TAG, "RFIDReader CONECTADO ");
            } catch (Exception de) {
                throw de;
            }
        }
    };

    @Override
    public void RFIDReaderAppeared(ReaderDevice readerDevice) {
        Log.d(TAG, "RFIDReaderAppeared " + readerDevice.getName());
    }

    @Override
    public void RFIDReaderDisappeared(ReaderDevice readerDevice) {
        Log.d(TAG, "RFIDReaderDisappeared " + readerDevice.getName());
    }

    public class EventHandler implements RfidEventsListener {
        // Read Event Notification
        public void eventReadNotify(RfidReadEvents e) {
            // Recommended to use new method getReadTagsEx for better performance in case of large tag population
            Log.d(TAG, " Mem Bank Data ");
            TagData[] myTags = reader.Actions.getReadTags(100);
            if (myTags != null) {
                for (int index = 0; index < myTags.length; index++) {
                    Log.d(TAG, "Tag ID " + myTags[index].getTagID());
                    if (myTags[index].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                            myTags[index].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
                        if (myTags[index].getMemoryBankData().length() > 0) {
                            Log.d(TAG, " Mem Bank Data " + myTags[index].getMemoryBankData());
                        }
                    }
                }
            }
        }

        @Override
        public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
            Log.d(TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
        }

        // Status Event Notification
//        public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
//            Log.d(TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
//        }
    }

    private void ConfigureReader() {
        Log.d(TAG, "ConfigureReader " + reader.getHostName());
        if (reader.isConnected()) {
            TriggerInfo triggerInfo = new TriggerInfo();
            triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
            try {
                // receive events from reader
                if (eventHandler == null)
                    eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                // HH event
                reader.Events.setHandheldEvent(true);
                // tag event with tag data
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(false);
                // set trigger mode as rfid so scanner beam will not come
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                // set start and stop triggers
                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
                reader.Config.setStopTrigger(triggerInfo.StopTrigger);
                // power levels are index based so maximum power supported get the last one
                MAX_POWER = reader.ReaderCapabilities.getTransmitPowerLevelValues().length - 1;
                // set antenna configurations
                Antennas.AntennaRfConfig config = reader.Config.Antennas.getAntennaRfConfig(1);
                config.setTransmitPowerIndex(MAX_POWER);
                config.setrfModeTableIndex(0);
                config.setTari(0);
                reader.Config.Antennas.setAntennaRfConfig(1, config);
                // Set the singulation control
                Antennas.SingulationControl s1_singulationControl = reader.Config.Antennas.getSingulationControl(1);
                s1_singulationControl.setSession(SESSION.SESSION_S0);
                s1_singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
                s1_singulationControl.Action.setSLFlag(SL_FLAG.SL_ALL);
                reader.Config.Antennas.setSingulationControl(1, s1_singulationControl);
                // delete any prefilters
                reader.Actions.PreFilters.deleteAll();
                //
            } catch (InvalidUsageException | OperationFailureException e) {
                e.printStackTrace();
            }
        }
    }


    public String Test1() {
        // check reader connection
        if (!reader.isConnected())
            return "Not connected";
        // set antenna configurations - reducing power to 200
        try {
//            RADIO_POWER_STATE radio =  myReader.Config.getRadioPowerState();
            Antennas.AntennaRfConfig config = null;
            config = reader.Config.Antennas.getAntennaRfConfig(1);
            config.setTransmitPowerIndex(20);
            config.setrfModeTableIndex(0);
            config.setTari(0);
            reader.Config.Antennas.setAntennaRfConfig(1, config);
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
            return e.getResults().toString() + " " + e.getVendorMessage();
        }
        return "Antenna power Set to 20";
    }

    public String Test2() {
        // check reader connection
        if (!reader.isConnected())
            return "Not connected";
        // Set the singulation control to S2 which will read each tag once only
        try {
            Antennas.SingulationControl s1_singulationControl = reader.Config.Antennas.getSingulationControl(1);
            s1_singulationControl.setSession(SESSION.SESSION_S2);
            s1_singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
            s1_singulationControl.Action.setSLFlag(SL_FLAG.SL_ALL);
            reader.Config.Antennas.setSingulationControl(1, s1_singulationControl);
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
            return e.getResults().toString() + " " + e.getVendorMessage();
        }
        return "Session set to S2";
    }

    // Read/Status Notify handler
    // Implement the RfidEventsLister class to receive event notifications

}

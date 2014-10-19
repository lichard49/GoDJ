package com.lichard49.godj;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AccelerometerLogService extends Service {

    private boolean mIsServiceStarted = false;
    private Context mContext = null;
    private SensorManager mSensorManager = null;
    private Sensor mSensor;
    private File mLogFile = null;
    private FileOutputStream mFileStream = null;
    private AccelerometerLogService mReference = null;
    private Float[] mValuesX = null;
    private Float[] mValuesY = null;
    private Float[] mValuesZ = null;
    private int mValueIndex = 0;
    private long mTimeStamp = 0;
    private ExecutorService mExecutor = null;
    private final int LOGGINGARRAYSIZE=25;
    private Handler handler = new Handler();
    private int syncToFirebase = 0;

    public AccelerometerLogService() {
        super();
    }

    public AccelerometerLogService(Context context) {
        super();

        if (context != null)
            mContext = context;
        else
            mContext = getBaseContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getBaseContext(), "Service onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (isServiceStarted() == false) {

            mContext = getBaseContext();
            mReference = this;
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mValuesX = new Float[LOGGINGARRAYSIZE];
            mValuesY = new Float[LOGGINGARRAYSIZE];
            mValuesZ = new Float[LOGGINGARRAYSIZE];
            for (int i = 0; i < LOGGINGARRAYSIZE; i++) {
            	mValuesX[i] = 0f;
            	mValuesY[i] = 0f;
            	mValuesZ[i] = 0f;
            }
            mTimeStamp = 0;
            mExecutor = Executors.newSingleThreadExecutor();
            startLogging();
            startIncrementalLogging();
        }

        //set started to true
        mIsServiceStarted = true;


        Toast.makeText(mContext, "Service onStartCommand", Toast.LENGTH_SHORT).show();
        return Service.START_STICKY;
    }

    private void startLogging() {

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mSensorManager.registerListener(
                        new SensorEventListener() {
                            @Override
                            public void onSensorChanged(SensorEvent sensorEvent) {
                                mTimeStamp = System.currentTimeMillis();
                                mValuesX[mValueIndex] = sensorEvent.values[0];
                                mValuesY[mValueIndex] = sensorEvent.values[1];
                                mValuesZ[mValueIndex] = sensorEvent.values[2];
                                if (syncToFirebase == 1) {
                                	float deltaX = 0;
                                	float deltaY = 0;
                                	float deltaZ = 0;
                                	int temp;
                                	for (int j = 0; j < LOGGINGARRAYSIZE; j++) {
                                		temp = (mValueIndex+1)%LOGGINGARRAYSIZE;
                                		deltaX = Math.abs(mValuesX[mValueIndex] - mValuesX[temp]);
                                		deltaY = Math.abs(mValuesY[mValueIndex] - mValuesY[temp]);
                                		deltaY = Math.abs(mValuesZ[mValueIndex] - mValuesZ[temp]);
                                		mValueIndex = temp;
                                	}
                                	float s = deltaX+deltaY+deltaZ;
                                	//SEND S TO THE DATABASE HERE
                                	syncToFirebase = 0;
                                	Toast.makeText(mContext, s + "", Toast.LENGTH_SHORT).show();
                                }
                                mValueIndex = (mValueIndex+1)%LOGGINGARRAYSIZE;
                            }
                            @Override
                            public void onAccuracyChanged(Sensor sensor, int i) {

                            }
                        }, mSensor, SensorManager.SENSOR_DELAY_FASTEST
                );
            }
        });
    }
    private void sync() {
    	syncToFirebase = 1;
    }

    private void startIncrementalLogging() {
    	Timer t = new Timer();
    	t.scheduleAtFixedRate(new TimerTask() {

    	    @Override
    	    public void run() {
    	        syncToFirebase = 1;
    	    }

    	}, 1000, 5000);
    	
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(mContext, "Service onDestroy", Toast.LENGTH_LONG).show();
        mIsServiceStarted = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Indicates if service is already started or not
     *
     * @return
     */
    public boolean isServiceStarted() {
        return mIsServiceStarted;
    }
}
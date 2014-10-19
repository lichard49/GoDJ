package com.lichard49.godj;

import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class MainActivity extends ActionBarActivity
{
	private Firebase myGoDJroot;
	
	private ProgressDialog pushDialog;
	private Dialog eventLoginDialog;
	
	private TextView artistNameText;
	private TextView eventNameText;
	private boolean artistSet = false;
	
	private Network network;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//how to launch the service:
		//startService(new Intent(this, AccelerometerLogService.class));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pushDialog = new ProgressDialog(MainActivity.this);
		pushDialog.setTitle("Syncing data");
		pushDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		eventLoginDialog = Dialogs.getDialog(this);
		eventLoginDialog.show();
		
		Firebase.setAndroidContext(this);
		myGoDJroot = new Firebase("https://lichard49.firebaseio.com/GoDJ");
		network = new Network(myGoDJroot, this);
		
		ImageButton misfitSyncButton = (ImageButton) findViewById(R.id.button_misfit_sync);
		misfitSyncButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// === query Misfit data
				String[] timestamps = new String[100];
				String[] data = new String[100];
				Hardware.queryMisfit(timestamps, data);
				
				network.pushData(timestamps, data, pushDialog);
			}
		});
		ImageButton endomondoSyncButton = (ImageButton) findViewById(R.id.button_endomondo_sync);
		endomondoSyncButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// === query Misfit data
				String[] timestamps = new String[100];
				String[] data = new String[100];
				Hardware.queryEndomondo(timestamps, data);
				
				network.pushData(timestamps, data, pushDialog);
			}
		});
		
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/EricssonCapital.ttf");
		artistNameText = (TextView) findViewById(R.id.text_artist_name);
		artistNameText.setTypeface(font);
		artistNameText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!artistSet)
				{
					eventLoginDialog.show();
				}
			}
		});
		eventNameText = (TextView) findViewById(R.id.text_event_name);
		eventNameText.setTypeface(font);
	}
	
	public void selectArtistEvent(JSONObject data)
	{
		try
		{
			artistNameText.setText(data.get("artist").toString());
			eventNameText.setText(data.get("event").toString());
		}
		catch (Exception e)
		{
			artistNameText.setText("No artist");
			eventNameText.setText("Press here to login to an event");
		}
	}
	
	public void selectArtistEvent(String id)
	{
		network.getArtistEvent(id);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

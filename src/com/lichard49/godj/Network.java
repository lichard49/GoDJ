package com.lichard49.godj;

import org.json.JSONObject;

import android.app.Dialog;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Network
{	
	private Firebase root;
	private MainActivity context;
	public Network(Firebase r, MainActivity c)
	{
		root = r;
		context = c;
	}
	
	public void getArtistEvent(final String id)
	{
		root.child("artists").child(id).addListenerForSingleValueEvent(new ValueEventListener()
		{
			@Override
			public void onCancelled(FirebaseError e) { }

			@Override
			public void onDataChange(DataSnapshot data)
			{
				JSONObject json = new JSONObject();
				try
				{
					Toast.makeText(context, ""+data.getChildrenCount(), Toast.LENGTH_LONG).show();
					json.put("artist", data.child("artist").getValue());
					json.put("event", data.child("event").getValue());
				} catch (Exception e) { }
				context.selectArtistEvent(json);
			}
			
		});
	}
	
	public void pushData(String[] timestamps, String[] data, final Dialog pushDialog)
	{
		pushDialog.show();
		int length = Math.min(timestamps.length, data.length);
		for(int i = 0; i < length; i++)
		{
			if(i < length-1)
			{
				root.child("me").child("users").child(timestamps[i]).setValue(data[i]);
			}
			else
			{
				root.child("me").child("users").child(timestamps[i]).setValue(data[i], new Firebase.CompletionListener()
				{
					@Override
					public void onComplete(FirebaseError error, Firebase firebase)
					{
						pushDialog.dismiss();
						//if(error != null) Toast.makeText(context, "Error :(", Toast.LENGTH_SHORT).show();
						//else Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
}

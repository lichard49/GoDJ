package com.lichard49.godj;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Network
{	
	private Firebase root;
	private Context context;
	public Network(Firebase r, Context c)
	{
		root = r;
		context = c;
	}
	
	public JSONObject getArtistEvent(String id)
	{
		JSONObject result = new JSONObject();
		
		//root.child("artists").
		
		return result;
	}
	
	public void pushData(String[] timestamps, String[] data, final Dialog pushDialog)
	{
		pushDialog.show();
		int length = Math.min(timestamps.length, data.length);
		for(int i = 0; i < length; i++)
		{
			if(i < length-1)
			{
				root.child("users").child(timestamps[i]).setValue(data[i]);
			}
			else
			{
				root.child("users").child(timestamps[i]).setValue(data[i], new Firebase.CompletionListener()
				{
					@Override
					public void onComplete(FirebaseError error, Firebase firebase)
					{
						pushDialog.dismiss();
						if(error != null) Toast.makeText(context, "Error :(", Toast.LENGTH_SHORT).show();
						else Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
}

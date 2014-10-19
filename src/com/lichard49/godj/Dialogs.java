package com.lichard49.godj;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Dialogs
{
	private Dialogs() { }
	
	private static Dialog dialog;
	private static Button registerButton;
	private static EditText eventCodeEdit;
	private static MainActivity context;
	
	public static Dialog getDialog(MainActivity c)
	{
		if(dialog == null)
		{
			context = c;
			dialog = new Dialog(context);
			dialog.setContentView(R.layout.dialog_event_login);
			dialog.setTitle("Enter your event ID:");
		
			eventCodeEdit = (EditText) dialog.findViewById(R.id.edit_event_code);
			registerButton = (Button) dialog.findViewById(R.id.button_event_code);
			registerButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					if(eventCodeEdit.getText().length() < 5)
					{
						Toast.makeText(context, "ID's are 5 digits", Toast.LENGTH_SHORT).show();
					}
					else
					{
						context.selectArtistEvent(eventCodeEdit.getText().toString());
						dialog.dismiss();
					}
				}
			});
			
		}
		return dialog;
	}
}

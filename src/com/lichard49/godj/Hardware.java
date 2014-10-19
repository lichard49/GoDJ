package com.lichard49.godj;

public class Hardware
{
	public static void queryEndomondo(String[] timestamps, String[] data)
	{
		for(int i = 0; i < Math.min(timestamps.length, data.length); i++)
		{
			int hour = (int)(Math.random()*12);
			int minute = (int)(Math.random()*60);
			int second = (int)(Math.random()*60);
			timestamps[i] = hour + ":" + minute + ":" + second;
			
			int x = (int)(Math.random()*255);
			int y = (int)(Math.random()*255);
			int z = (int)(Math.random()*255);
			data[i] = x + "," + y + "," + z;
		}
	}
	
	public static void queryMisfit(String[] timestamps, String[] data)
	{
		for(int i = 0; i < Math.min(timestamps.length, data.length); i++)
		{
			int hour = (int)(Math.random()*12);
			int minute = (int)(Math.random()*60);
			int second = (int)(Math.random()*60);
			timestamps[i] = hour + ":" + minute + ":" + second;
			
			int x = (int)(Math.random()*255);
			int y = (int)(Math.random()*255);
			int z = (int)(Math.random()*255);
			data[i] = x + "," + y + "," + z;
		}
	}
	
}

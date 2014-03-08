package edu.dhbw.andar.pub;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import edu.dhbw.andopenglcam.R;

import android.util.Log;
import android.widget.Toast;

public class Second implements Runnable {
	CustomActivity ca;
	Second(CustomActivity ca)
	{
		this.ca=ca;
		run();
	}
	
	public void run()
	{
//		Global.sendToServer("probando hola hola", "172.16.171.83");
//		if(Global.receiveFromServer().equals("2"))
//		{
//			Global.monstruo1.selected.model.setScale(0);
//			Global.monstruo2.selected.model.setScale(4);
//		}else
//		{
//			Global.monstruo1.selected.model.setScale(4);
//			Global.monstruo2.selected.model.setScale(0);
//		}
		
        Global.sendToServer("probando hola hola");
        String str=Global.receiveFromServer();
	}
}
package edu.dhbw.andar.pub;

import java.io.BufferedReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.res.AssetManager;

import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.nav.Wing;
import edu.dhbw.andobjviewer.graphics.MiCharacter;
import edu.dhbw.andobjviewer.models.Model;
import edu.dhbw.andobjviewer.parser.ObjParser;
import edu.dhbw.andobjviewer.util.AssetsFileUtil;
import edu.dhbw.andobjviewer.util.BaseFileUtil;

public class Global {
	public static String player_action = "nada";
	
	public static ARToolkit artoolkit;
	public static AssetManager am;
	public static MiCharacter monstruo1;
	public static MiCharacter monstruo2;
	public static Wing wing;
	
	static String SERVER_IP = "172.16.171.83";
	//static String SERVER_IP = "172.16.170.1";
	
	static boolean server_lock = false;
	
	public static Model getModel(String obj_path)
	{
		BaseFileUtil fileUtil = new AssetsFileUtil(am);
		fileUtil.setBaseFolder("models/");
		Model model = null;
		ObjParser parser = new ObjParser(fileUtil);
		if(fileUtil != null) {
			BufferedReader fileReader = fileUtil.getReaderFromName(obj_path);
			if(fileReader != null) {
				try {
					model = parser.parse("Model", fileReader);
					model.scale=4;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return model;
	}
	
	public static void sendToServer(String mensaje)
	{
		if(server_lock==true)
			return;
		server_lock=true;
		try
		{
			String messageStr = mensaje;
			int server_port = 9876;
			DatagramSocket s = new DatagramSocket();
			InetAddress local = InetAddress.getByName(SERVER_IP);
			int msg_length = messageStr.length();
			byte[] message = messageStr.getBytes();
			DatagramPacket p = new DatagramPacket(message, msg_length, local,server_port);
			s.send(p);
			s.close();
		}catch(Exception e)
		{
			
		}
	}
	
	public static String receiveFromServer()
	{
		String msg = "";
		try
		{
			byte[] receiveData = new byte[1024];
	        DatagramPacket receivePacket = new     DatagramPacket(receiveData,receiveData.length);
	        DatagramSocket s2 = new DatagramSocket(9876);
	        s2.receive(receivePacket);
	        msg = new String(receivePacket.getData(),0,receivePacket.getLength());
	        s2.close();
		}catch(Exception e)
		{
			
		}
		server_lock = false;
		return msg;
	}
}

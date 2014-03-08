package edu.dhbw.andar.pub;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.nav.Wing;
import edu.dhbw.andobjviewer.graphics.MiCharacter;
import edu.dhbw.andopenglcam.R;

/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class CustomActivity extends AndARActivity {
	
	private final int MENU_SCREENSHOT = 0;
	private final int MENU_THUNDER = 1;
	private final int MENU_HEAL = 2;
	
	Menu menu;
	
	boolean created =  false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CustomRenderer renderer = new CustomRenderer();//optional, may be set to null
		Global.am=getResources().getAssets();
		
		super.setNonARRenderer(renderer);//or might be omited
		try
		{
			Global.artoolkit = super.getArtoolkit();

			Global.monstruo1 = new MiCharacter(Global.getModel("monstruo1.obj"),"c2.patt");
			Global.monstruo2 = new MiCharacter(Global.getModel("monstruo2.obj"),"e2.patt");
			Global.wing = new Wing();
			
		}catch (Exception ex){
			//handle the exception, that means: show the user what happened
			System.out.println("");
		}
		
//        Thread t = new Thread(new Second(this));
//        t.start();
		
		
		
		final Handler mHandler = new Handler();
		
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            // TODO Auto-generated method stub
	            while (true) {
	                try {
	                    Thread.sleep(10000);
	                    mHandler.post(new Runnable() {

	                        @Override
	                        public void run() {
	                            // TODO Auto-generated method stub
	                            // Write your code here to update the UI.
	                            Global.sendToServer(Global.player_action);
	                            Global.player_action="nada";
	                            String str=Global.receiveFromServer();
	                            Toast.makeText(CustomActivity.this, str, Toast.LENGTH_LONG ).show();
	                            if(str.equals("1"))
	                            {
	                            	Global.monstruo1.model.scale=8;
	                            	Global.monstruo2.model.scale=4;
	                            }else
	                            {
	                            	Global.monstruo1.model.scale=4;
	                            	Global.monstruo2.model.scale=8;
	                            }
	                        }
	                    });
	                } catch (Exception e) {
	                    // TODO: handle exception
	                }
	            }
	        }
	    }).start();
		
		
	}
	


	/**
	 * Inform the user about exceptions that occurred in background threads.
	 * This exception is rather severe and can not be recovered from.
	 * Inform the user and shut down the application.
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("AndAR EXCEPTION", ex.getMessage());
		finish();
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		menu.add(0, MENU_SCREENSHOT, 0, getResources().getText(R.string.takescreenshot))
		.setIcon(R.drawable.screenshoticon);
		menu.add(1, MENU_THUNDER, 1, getResources().getText(R.string.thunder))
		.setIcon(R.drawable.thunder);
		menu.add(2, MENU_HEAL, 2, getResources().getText(R.string.heal))
		.setIcon(R.drawable.heal);
		
		this.menu=menu;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*if(item.getItemId()==1) {
			artoolkit.unregisterARObject(someObject);
		} else if(item.getItemId()==0) {
			try {
				someObject = new CustomObject
				("test", "patt.hiro", 80.0, new double[]{0,0});
				artoolkit.registerARObject(someObject);
			} catch (AndARException e) {
				e.printStackTrace();
			}
		}*/
		switch(item.getItemId()) {
		case MENU_SCREENSHOT:
			new TakeAsyncScreenshot().execute();
			break;
		}
		return true;
	}
	
	class TakeAsyncScreenshot extends AsyncTask<Void, Void, Void> {
		
		private String errorMsg = null;

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bm = takeScreenshot();
			FileOutputStream fos;
			try {
				fos = new FileOutputStream("/sdcard/AndARScreenshot"+new Date().getTime()+".png");
				bm.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();					
			} catch (FileNotFoundException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(Void result) {
			if(errorMsg == null)
				Toast.makeText(CustomActivity.this, getResources().getText(R.string.screenshotsaved), Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText(CustomActivity.this, getResources().getText(R.string.screenshotfailed)+errorMsg, Toast.LENGTH_SHORT ).show();
		};	
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		super.onTouchEvent(event);
//		if(monstruo1.isVisible() && !monstruo2.isVisible() && monstruo1.model.scale==4)
//		{
//			monstruo1.model.scale=8;
//			monstruo2.model.scale=4;
//			monstruo2.selected.model.scale=0;
//		}
//		if(monstruo2.isVisible() && !monstruo1.isVisible() && monstruo2.model.scale==4)
//		{
//			monstruo2.model.scale=8;
//			monstruo2.selected.model.scale=4;
//			monstruo1.model.scale=4;
//			hp.decrese(10);
//		}
		
//		Toast.makeText(CustomActivity.this,getIPAddress(), Toast.LENGTH_SHORT ).show();
        
//        Global.sendToServer("probando hola hola", "172.16.171.83");
//        Global.receiveFromServer();
//		if(!created)
//		{
//			created=true;
//		      Thread t = new Thread(new Second(this));
//		      t.start();
//		}
//		
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
        	Global.player_action = "touch";
//    		Global.sendToServer("touch");
//            String str=Global.receiveFromServer();
//            if(str.equals("1"))
//            {
//            	Global.monstruo1.model.scale=8;
//            	Global.monstruo2.model.scale=4;
//            }else
//            {
//            	Global.monstruo1.model.scale=4;
//            	Global.monstruo2.model.scale=8;
//            }
        }
		
		return true;
	}
	
    public String getIPAddress() {
    	WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
    	String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    	return ip;
    }
}

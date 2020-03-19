package com.example.lenovo.activeandroid3.asyntask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.example.lenovo.activeandroid3.activity.SplashScreen;
import com.example.lenovo.activeandroid3.model.Mobile;
import com.example.lenovo.activeandroid3.model.SwitchBoard;
import com.example.lenovo.activeandroid3.model.SwitchButton;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by anway on 14/8/18.
 */

public class IDIdentificationAsynTask  extends AsyncTask<Void, Void, Void>
{
    Context context ;
    List<String> ipList ;
    String TAG = "IDIdentificationAsynTask" ;

    Socket client ;
    Scanner scanner ;
    PrintStream printStream ;

    SplashScreen splashScreen ;

    public IDIdentificationAsynTask(Context context, List<String> ipList, SplashScreen splashScreen)
    {
        this.context = context;
        this.ipList = ipList ;
        this.splashScreen = splashScreen ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "onPreExecute: " );
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        for ( String ip : ipList )
        {
            try
            {
                Log.e(TAG, "doInBackground: "+ip  );

                // dev device ip and port
//                client = new Socket(ip , 8888);

                // test device ip and port
                client = new Socket(ip , 1234);
                client.setSoTimeout(1000);

//                Socket client = new Socket();
//                client.connect(new InetSocketAddress(ip, 8888), 1000);

                //test device ip and port
//                client = new Socket("192.168.1.101", 1344) ;

                Log.e(TAG,"Socket created ");

                scanner = new Scanner(client.getInputStream());
                Log.e(TAG, "scanner created ");

                printStream = new PrintStream(client.getOutputStream());
                Log.e(TAG,"printStream created ");

                printStream.println("*ID?#");
                Log.e(TAG,"after printing :  *ID?#");

                String response = scanner.nextLine();
                Log.e("Response is : ", response ) ;

                String deviceId = response.replaceAll("[^0-9]", "");
                Log.e(TAG, "deviceId: "+ deviceId);

                SwitchBoard switchBoard = getSingleBoardFromId( Long.parseLong( deviceId )  ) ;

                if( switchBoard != null )
                {
                    Log.e(TAG, "switchBoard ID Name and RoomId : " + switchBoard.getId() + " " + switchBoard.BoardName + "  " + switchBoard.RoomId);
                    SwitchBoard switchBoardNew =  getSingleBoardFromId( switchBoard.getId() );
                    Log.e(TAG, "switchBoard ID Name RoomId IP: " + switchBoardNew.getId() + " " + switchBoardNew.BoardName + "  " + switchBoardNew.RoomId+" "+switchBoard.IP );

                    switchBoard.IP =  ip  ;
                    switchBoard.save();

                    Socket statusSocket = new Socket(ip , 1234);
                    statusSocket.setSoTimeout(1000);
                    Scanner  scannerSocket = new Scanner(statusSocket.getInputStream());
                    PrintStream  printStreamSocket = new PrintStream(statusSocket.getOutputStream());
                    printStreamSocket.println("*STS,"+ switchBoard.getId() +"#");

                    // response2 contain data like this : "*REC,1,0,1,2,3,4,5,6,7#"
                    String response2 = scannerSocket.nextLine();
                    Log.e("status response2 : ", response2 ) ;

                    String data1 = response2.substring(response2.indexOf(",") + 1);
                    String deviceIdFromResponse = data1.substring(0, data1.indexOf(","));
                    String data2 = data1.substring(data1.indexOf(",") + 1);

                    List<SwitchButton> allButtonFromBoardId = getAllButtonFromBoardId( Long.parseLong( deviceId ) ) ;

                    Log.e(TAG, "doInBackground  data2 :  "+data2 );
                    String numberOnly = data2.replaceAll("[^0-9]", "");
                    Log.e(TAG, "numberOnly is : "+numberOnly );

                    for ( int i= 0 ;  i < allButtonFromBoardId.size() ; i++ )
                    {
                        SwitchButton switchButton = allButtonFromBoardId.get( i ) ;
                        String statusValue = String.valueOf( numberOnly.charAt( i ) ) ;
                        if( statusValue.equals("0") )
                        {
                            switchButton.is_on = true ;
                        }else {
                            switchButton.is_on = false ;
                        }
                        switchButton.save() ;
                    }
//                    printStream.println("*STS,1#");
//                    Log.e(TAG,"after printing :  *STS,1#");

//                    String statusResponse = scanner.nextLine();
//                    Log.e("statusResponse is : ", statusResponse ) ;

                }else {
                    Log.e(TAG, " switch board is not present with deviceId "+ deviceId );
                }

                printStream.close();
                scanner.close();
                client.close();
            } catch (Exception e)
            {
                Log.e(TAG, "exception in doInBackground: "+ e.getMessage());
                Log.e(TAG, "this ip is of mobile so create entry as mobile device" ) ;
                Date currentTime = Calendar.getInstance().getTime() ;
                Mobile mobile = new Mobile();
                mobile.IP = ip ;
                mobile.MobileName = "Mobile "+ip ;
                mobile.CreatedAt = currentTime.getTime() ;
                mobile.Updatedat = currentTime.getTime() ;
                mobile.save();
            }

//            finally
//            {
//                try
//                {
//                    Log.e(TAG, "doInBackground: execute finally block" );
//////                    printStream.close();
//////                    scanner.close();
//                    client.close();
//                } catch (IOException e)
//                {
//                    Log.e(TAG, "doInBackground: excep in finally"+e.getMessage() );
//                    e.printStackTrace();
//                }
//            }
        }
        return null;
    }

    private SwitchBoard getSingleBoardFromId(long deviceId)
    {
        return new Select().from(SwitchBoard.class).where("id = ?",   deviceId  ).executeSingle();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        splashScreen.readyToGoV1MainActivity();

    }

    List<SwitchButton> getAllButtonFromBoardId(Long boardId )
    {
//        return new Select().from(SwitchButton.class).where("is_on = ?", true ).where("id =?", roomId ).execute();
//        return new Select().from(SwitchButton.class).where("is_on = ? AND RoomId = ?", boardId).execute();
        return new Select().from(SwitchButton.class).where("SwitchBoardId = ?", boardId).execute();
    }
}

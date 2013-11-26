/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.web.renderer.utilities;

import ArmyC2.C2SD.Utilities.ErrorLogger;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import sec.web.renderer.SinglePointServer;

/**
 *
 * @author michael.spinelli
 */
public class SinglePointServerTester  implements Runnable {
    private int _port = 0;
    private Boolean _isRunning = false;
    private SinglePointServer _sps = null;
    public SinglePointServerTester(SinglePointServer sps)
    {
        _sps = sps;
        _port = _sps.getPortNumber();
    }
    public SinglePointServerTester(int portNumber)
    {
        _port = portNumber;
    }
    /*
    public void setPort(int portNumber)
    {
        port = portNumber;
    }*/

    public Boolean isRunning()
    {
        return _isRunning;
    }

    public void run() {
        //String host = "http://localhost:";
        String host = "http://127.0.0.1:";
        String strUrl = host + String.valueOf(_port) + "/SFGP-----------" ;

        int timeoutInSeconds = 4;
        int port = 0;
        int seconds = 0;
        int response = 0;
        try
        {

            System.out.println("testing connection...");
            Boolean running = false;
            int timeoutInMiliseconds = timeoutInSeconds*1000;
            while(running==false && seconds < 30)
            {

                try
                {
                    //System.out.println(".");
                    if(_sps != null)
                        port = _sps.getPortNumber();
                    else
                        port = _port;
                    strUrl = host + String.valueOf(port) + "/SFGP-----------" ;
                    //System.out.println("..");
                    java.net.URL url = new java.net.URL(strUrl);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    //urlConn.setInstanceFollowRedirects(false);
                    urlConn.setRequestMethod("GET");
                    //System.out.println("...");
                    urlConn.setConnectTimeout(timeoutInMiliseconds);//seems to connect even if not running
                    urlConn.setReadTimeout(timeoutInMiliseconds);//this is the one that matters
                    //System.out.println("....");
                    urlConn.connect();
                    //System.out.println(".....");
                    response = urlConn.getResponseCode();
                    //System.out.println("......");
                    if(response==HttpURLConnection.HTTP_OK)
                    {
                        _isRunning=true;
                        running=true;

                    }
                    //urlConn.disconnect();

                }
                catch(SocketTimeoutException stexc)
                {
                    //error creating http connection
                    //System.out.println("Single point server is not ready");
                }
                catch(IOException ioexc)
                {
                    //error creating http connection
                    // System.out.println("Single point server is not ready");

                }
                catch(Exception exc)
                {
                    //System.out.println("Single point server is not ready (exc)");
                }
                catch(Throwable t)
                {
                    //System.out.println("Single point server is not ready (t)");
                }
                if(running==false)
                {
                    //sleep for 1 second
                    Thread.sleep(1000);
                    //System.out.println("trying again...");
                    seconds = seconds + timeoutInSeconds + 1;
                    //System.out.println("seconds: " + String.valueOf(seconds));
                }

            }
            if(_isRunning)
            {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd - HH:mm:ss:SSS");
                System.out.println("Single point server is ready as of " + sdf.format(date));
            }
            else
                System.out.println("Single point server has not started for 30 seconds");
            _sps = null;
        }
        catch(InterruptedException iex){
            System.out.println("SPS connection test thread interupted");
        }
    }
}

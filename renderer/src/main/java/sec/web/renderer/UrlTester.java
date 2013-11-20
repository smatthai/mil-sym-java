/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.web.renderer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author michael.spinelli
 */
public class UrlTester implements Runnable  {
    
    private int _port = 0;
    private Boolean _isRunning = false;
    private Boolean _isAvailable = false;
    private Boolean _verbose = false;
    private String _strUrl = null;
    private String _name = null;
    private URL _url = null;
    private int _timeoutSeconds = 4;
    private int _timeoutDuration = 30;
    private int _retries = 3;
    
    /**
     * 
     * @param url URL being tested
     * @param name Name of the site or service
     * @param timeout # of seconds to wait for reply
     * @param retries max # of times we try to connect
     */      
    public UrlTester(String url, String name, int timeout, int retries)
    {
        _strUrl = url.toString();
        _name = name;
        _timeoutSeconds = timeout;
        _retries = retries;
    }
    
    public void setTimeoutInSeconds(int timeout)
    {
        _timeoutSeconds = timeout;
    }
    
    /**
     * 
     * @param duration 
     * @deprecated 
     */
    public void setRetryDurationInSeconds(int duration)
    {
        _timeoutDuration = duration;
    }
    
    public void setRetyCount(int retries)
    {
        _retries = retries;
    }
    
    public Boolean isAvailable()
    {
        return _isRunning;
    }
    
    public void setVerbose(boolean verbose)
    {
        _verbose = verbose;
    }
    

    @Override
    public void run() {
        
        int seconds = 0;
        int response = 0;
        
        try
        {
            if(_verbose)
            {
                if(_name != null)
                    System.out.println("testing connection to: " + _name);
                else
                    System.out.println("testing connection to: " + _strUrl);
            }
            Boolean running = false;
            int timeoutInMiliseconds = _timeoutSeconds*1000;

            int attemptCount = 1;
            //while(running==false && seconds < _timeoutDuration)
            while(running==false && attemptCount <= _retries)
            {
                if(_verbose)
                {
                    System.out.println("Attempt: #" + String.valueOf(attemptCount));
                }

                
                try
                {
                    java.net.URL url = new java.net.URL(_strUrl);
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
                    else
                    {
                        if(_verbose)
                        {
                            System.out.println("Response code: " + response);
                        }
                    }
                    //urlConn.disconnect();

                }
                catch(SocketTimeoutException stexc)
                {
                    //error creating http connection
                    if(_verbose)
                    {
                        System.out.println("SocketTimeoutException: " + stexc.getMessage());
                    }
                }
                catch(IOException ioexc)
                {
                    if(_verbose)
                    {
                        System.out.println("IOException: " + ioexc.getMessage());
                    }
                }
                catch(Exception exc)
                {
                    if(_verbose)
                    {
                        System.out.println("Exception: " + exc.getMessage());
                    }
                }
                catch(Throwable t)
                {
                    if(_verbose)
                    {
                        System.out.println("Throwable: " + t.getMessage());
                    }
                }
                if(running==false)
                {
                    //sleep for 1 second
                    Thread.sleep(1000);
                    //System.out.println("trying again...");
                    seconds = seconds + _timeoutSeconds + 1;
                }
                
                attemptCount++;
            }
            if(_isRunning)
            {
                //Date date = new Date();
                //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd - HH:mm:ss:SSS");
                if(_name != null)
                {
                    if(_verbose)
                        System.out.println(_name + " is available.");
                }
                else
                {
                    if(_verbose)
                        System.out.println("URL available: " + _strUrl);
                }
            }
            else
            {
                if(_name != null)
                {
                    if(_verbose)
                        System.out.println(_name + " is not available.");
                }
                else
                {
                    if(_verbose)
                        System.out.println("URL not available: " + _strUrl);
                }
            }
            
        }
        catch(InterruptedException iex){
            System.out.println("URL test thread interupted");
        }
    }

    
}

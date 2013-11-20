/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyC2.C2SD.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;



/**
 *
 * @author michael.spinelli
 */
public class FileHandler {

    static public String fileToString(String inFile)
  {
    return new String(fileToBytes(inFile));
  }

  static public byte[] fileToBytes(String inFile)
  {
    FileInputStream fis = null;
    try
    {
      fis = new FileInputStream(inFile);
    }
    catch(Throwable thrown)
    {
    }
    return inputStreamToBytes(fis);
  }

  static public String InputStreamToString(InputStream inFile)
  {
      return new String(InputStreamToBytes(inFile));
  }

  static public byte[] InputStreamToBytes(InputStream inFile)
  {
      return inputStreamToBytes(inFile);
  }

  static public byte[] inputStreamToBytes(InputStream is)
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      byte[] bytes = new byte[8192];
      int size = is.read(bytes);
      while(size > 0)
      {
        baos.write(bytes, 0, size);
        size = is.read(bytes);
      }
    }
    catch(Throwable thrown)
    {
    }
    finally
    {
      if(is != null)
      {
        try
        {
          is.close();
        }
        catch(Throwable thrown)
        {
        }
      }
    }
    return (baos.toByteArray());
  }

}

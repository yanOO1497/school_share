package com.yyy.school.share.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class RequestDispatcherUtil {
	

	public String getResponse(String requsetUrl, String content) {
        try {
            URL url = new URL(requsetUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//            httpConn.setDoOutput(true);
//            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("GET");
//            OutputStream outputStream = httpConn.getOutputStream();
//            outputStream.write(content.getBytes("utf-8"));
//            outputStream.close();
            BufferedReader responseReader = new BufferedReader(
                    new InputStreamReader(httpConn.getInputStream(), "utf-8"));
            String readLine;
            StringBuffer responseSb = new StringBuffer();
            while ((readLine = responseReader.readLine()) != null) {
                responseSb.append(readLine);
            }
            return responseSb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

}

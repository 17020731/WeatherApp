package com.example.weatherapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
    private static final String TAG = "HttpRequest" ;

    //HTTP Get Request
    public String sendGet(String urlStr){
        String result = "";     //Kết quả

        try {
            URL url = new URL(urlStr);      //Đường dẫn
            URLConnection connection = url.openConnection();    //Tạo kết nối
            InputStream is = connection.getInputStream();   //Tạo luồng

            //Đọc bằng BufferReader với kiểu UTF-8
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                result += inputLine;
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //Trả về kết quả
        return result;
    }
}

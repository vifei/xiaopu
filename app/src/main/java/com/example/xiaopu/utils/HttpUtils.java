package com.example.xiaopu.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtils {

    public HttpUtils(){

    }

    public static String postJsonContent(String url_path, String jsonData) {
        try{
            URL url = new URL(url_path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            // 将JSON数据写入输出流
            bufferedWriter.write(jsonData);
            // 刷新缓冲区
            bufferedWriter.flush();
            // 关闭写入流
            bufferedWriter.close();
            // 关闭输出流
            outputStream.close();


            int code = connection.getResponseCode();

            if(code == 200){
                return changeInputStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getJsonContent(String urlPath) {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            int code = connection.getResponseCode();

            if (code == HttpURLConnection.HTTP_OK) {
                return changeInputStream(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String changeInputStream(InputStream inputStream) {
        String jsonString = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];
        try{
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            jsonString = outputStream.toString();
            return jsonString;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

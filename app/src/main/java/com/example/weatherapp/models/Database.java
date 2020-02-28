package com.example.weatherapp.models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class Database {

    Set<City> usersList = new HashSet<>();


    public  Database(Context context){
        loadJSONFromAssset(context);
    }


    public Set<City> loadJSONFromAssset(Context context){

        try{
            InputStream is = context.getAssets().open("city.list.min.json");
            int size = is.available();

            // Đọc dữ liệu từ file .json trong Assets bằng buffer
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer, "utf-8");


            //Sử dụng Gson để chuyển Json về Object City
            Gson gson = new GsonBuilder().create();
            Type listType = new TypeToken<HashSet<City>>() {}.getType();
            usersList = gson.fromJson(text, listType);  //Chuyển toàn bộ json thành mảng City


            //Todo: TEST
//            System.out.println("So luong "+ usersList.size());
//            for(City c : usersList){
//                System.out.println("json " + c.toString());
//            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return usersList;
    }

    public Set<City> getUsersList() {
        return usersList;
    }

    public void setUsersList(Set<City> usersList) {
        this.usersList = usersList;
    }
}

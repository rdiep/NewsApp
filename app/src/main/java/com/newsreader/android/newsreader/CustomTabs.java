package com.newsreader.android.newsreader;

import android.content.Context;
import android.util.Log;

import com.newsreader.android.newsreader.adapter.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomTabs {

    private List<String> customTabs = new ArrayList<>();
    private DatabaseAdapter dbHelper;
    private Context context;

    public CustomTabs(Context context){
        this.context = context;
        dbHelper = new DatabaseAdapter(context);
        customTabs = dbHelper.getTabNames(customTabs);
    }

    public List<String> GetTabs(){
        return customTabs;
    }

    public void PrintTabs(){
        Log.i("PRINT",customTabs.toString());
    }

    public int GetSize(){
        return customTabs.size();
    }

}

package com.newsreader.android.newsreader;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.newsreader.android.newsreader.adapter.DatabaseAdapter;
import com.newsreader.android.newsreader.fragments.CustomFragment;
import com.newsreader.android.newsreader.fragments.TopNewsFragment;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navView;
    private Button tabButton,deleteTabButton;
    private DatabaseAdapter dbHelper;
    private CustomTabs customTabs;
    private RecyclerView recyclerView;
    private Menu menu;
    private SubMenu CustomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customTabs = new CustomTabs(this);
        customTabs.PrintTabs();
        dbHelper = new DatabaseAdapter(this);
        toolbar = (Toolbar) findViewById(R.id.nav_action);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        tabButton = (Button) findViewById(R.id.createButton);
        deleteTabButton = (Button) findViewById(R.id.deleteButton);
        //recyclerView = (RecyclerView) findViewById(R.id.navRecycler);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        toolbar.setTitle("News Stations");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();



        navView = (NavigationView) findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
        menu = navView.getMenu();
        CustomMenu = menu.addSubMenu("Other");
        UpdateNavi(menu);


        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.fragmentcontainer,new TopNewsFragment()).commit();

        }

        tabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox(v.getContext());
            }
        });
        deleteTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTab(v.getContext());
            }
        });
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new CustomListAdapter(customTabs.GetTabs());
//        recyclerView.setAdapter(adapter);


    }

    public void selectDrawerItem(MenuItem menuItem) {

        //custom check
        Boolean custom = false;
        Bundle bundle = new Bundle();

        // Create a new fragment and specify the fragment to show based on nav item clicked

        Fragment fragment = null;
        Class fragmentClass;

//        if(menuItem.getGroupId()==R.id.Other){
//        if(menuItem.getSubMenu().equals("Other")){
//            Log.i("OTHER", (String) menuItem.getTitle());
//
//        }


        switch(menuItem.getItemId()) {

//            case R.id.news_stations:
//
//                fragmentClass = TopNewsFragment.class;
//
//                break;

//            case R.id.entertainment:
//
//                fragmentClass = SecondFragment.class;
//
//                break;
//
//            case R.id.business:
//
//                fragmentClass = ThirdFragment.class;
//
//                break;

            default:
            //if make this far, is custom.
                custom = true;
                //fragmentClass = TopNewsFragment.class;
                String topic = dbHelper.getTopic(menuItem.getTitle().toString());
                bundle.putString("Topic",topic);
                fragmentClass = CustomFragment.class;
                CustomFragment f1 = new CustomFragment();
                //Log.i("MENU",menuItem.getTitle().toString());

        }



        try {

            fragment = (Fragment) fragmentClass.newInstance();
            if (custom){
                fragment.setArguments(bundle);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }



        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, fragment).commit();



        // Highlight the selected item has been done by NavigationView

        menuItem.setChecked(true);

        // Set action bar title

        //setTitle(menuItem.getTitle());
        toolbar.setTitle(menuItem.getTitle());
        setSupportActionBar(toolbar);
        // Close the navigation drawer

        drawerLayout.closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void UpdateNavi(Menu menu){
        CustomMenu.clear();
        customTabs = new CustomTabs(this);

        for(int i = 0; i < customTabs.GetSize(); i++){
            //menu.add(R.id.Other,i,Menu.NONE,customTabs.GetTabs().get(i));
            //CustomMenu.add(R.id.Other,i,i,customTabs.GetTabs().get(i));
            CustomMenu.add(customTabs.GetTabs().get(i));
        }
        //CustomMenu.add(R.id.Other,customTabs.GetSize(),customTabs.GetSize(),"");
        CustomMenu.add("");
        customTabs.PrintTabs();
        //Log.i("LAST",customTabs.GetTabs().get(customTabs.GetSize()-1).toString());
    }

    public void DeleteTab(Context context){
        customTabs = new CustomTabs(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tabs to Delete");
        // add a checkbox list
        String[] animals = new String[customTabs.GetSize()];
        animals = customTabs.GetTabs().toArray(animals);
        final boolean[] checkedItems = new boolean[animals.length];
        Arrays.fill(checkedItems,Boolean.FALSE);
        Log.i("FIRST",Arrays.toString(checkedItems));
        final String[] finalAnimals = animals;
        builder.setMultiChoiceItems(finalAnimals, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                //Log.i("STRING1",Arrays.toString(checkedItems));

//                if(checkedItems[which] == true){
//                    Log.i("TRUE","FASE");
//                    checkedItems[which] = false;
//                }else{
//                    checkedItems[which] = true;
//                    Log.i("FALSE","FASE");
//                }
            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                //Log.i("STRING1",Arrays.toString(checkedItems));
                for (int i = 0; i< checkedItems.length; i++){
                    if (checkedItems[i] == true){
                        Log.i("Checked", finalAnimals[i].toString());
                        long longnum = dbHelper.deleteData(finalAnimals[i].toString());
                    }

                }
                drawerLayout.closeDrawers();
                UpdateNavi(menu);
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        builder.setNegativeButton("Cancel", null);

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void DialogBox(Context context){
        final LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);
        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        final EditText userInputDialogEditText2 = (EditText) mView.findViewById(R.id.userInputDialog2);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        long longnum = dbHelper.insertData(userInputDialogEditText.getText().toString(),userInputDialogEditText2.getText().toString());
                        if(longnum<0){
                            Log.i("Failed","Insert Failed");
                        }else{
                            UpdateNavi(menu);
                            Log.i("Success","Insert Success");
                            Bundle bundle = new Bundle();
                            bundle.putString("Topic",userInputDialogEditText2.getText().toString());
                            toolbar.setTitle(userInputDialogEditText.getText().toString());
                            CustomFragment f1 = new CustomFragment();
                            f1.setArguments(bundle);
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = manager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentcontainer, f1);
                            fragmentTransaction.commit();
                            drawerLayout.closeDrawers();





                        }
                        Log.i("STRING",userInputDialogEditText.getText().toString());

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();

    }
}

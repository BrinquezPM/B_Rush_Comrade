package com.example.finalb_rushadmin;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DriverFragment extends Fragment {
    private Button b1, update, delete;
    private ArrayList<String> listDriverName;
    private ArrayAdapter adapter;
    private DatabaseHelper databaseHelper;
    private ListView listView;
    private Dialog dialog;
    private String string;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bus_driver, container, false);
        initializeDB();

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        update = (Button) dialog.findViewById(R.id.btnView);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateDriver.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", string);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        delete = (Button) dialog.findViewById(R.id.btnDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteDriver.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", string);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listDriverName = new ArrayList<String>();
        listView = v.findViewById(R.id.listViewDrivers);
        viewData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                string = (String) parent.getItemAtPosition(position);
                dialog.show();
            }
        });

        b1 = v.findViewById(R.id.btnAddDriver);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDriver.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private void initializeDB()
    {
        if(databaseHelper == null)
        {
            databaseHelper = new DatabaseHelper(getActivity());
        }
    }
    private void viewData()
    {
        Cursor cursor = databaseHelper.getListDrivers();
        if(cursor.getCount() == 0){ Toast.makeText(getActivity(), "Database is empty", Toast.LENGTH_SHORT).show(); }
        else{
            while(cursor.moveToNext()){
                long ID = cursor.getLong(cursor.getColumnIndex("ID"));
                String id = String.valueOf(ID);
                long personID = cursor.getLong(cursor.getColumnIndex("PersonID"));
                Cursor driver = databaseHelper.getPerson(personID);
                String name = id+"-"+driver.getString(driver.getColumnIndex("FirstName"))+" "+driver.getString(driver.getColumnIndex("LastName"));
                listDriverName.add(name);
            }
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listDriverName);
            listView.setAdapter(adapter);
        }
    }
}

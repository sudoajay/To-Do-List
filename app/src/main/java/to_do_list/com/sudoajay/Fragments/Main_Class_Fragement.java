package to_do_list.com.sudoajay.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import to_do_list.com.sudoajay.Adapter.Custom_Adapter_Recycleview;
import to_do_list.com.sudoajay.Create_New_To_Do_List;
import to_do_list.com.sudoajay.DataBase.DataBase;
import to_do_list.com.sudoajay.MainActivity;
import to_do_list.com.sudoajay.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Class_Fragement extends Fragment  implements View.OnClickListener{

    // global variable
    private MainActivity main_Activity;
    private String which_Tab;
    private RecyclerView recyclerView;
    private  View view;
    private Custom_Adapter_Recycleview custom_adapter_recycleview;
    private ArrayList<String> task_Name;
    private ArrayList<Boolean> check_Box_Array;
    private TextView nothing_Text_View;
    private ImageView nothing_Image_View;
    private DataBase dataBase ;

    public Main_Class_Fragement() {
        // Required empty public constructor
    }
    public Main_Class_Fragement createInstance(MainActivity main_Activity ,String which_Tab ) {
        this.main_Activity = main_Activity;
        this.which_Tab = which_Tab;
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_class, container, false);


        // Reference
        Reference();
        task_Name.add("fill");
        task_Name.add("fillit");
        task_Name.add("fillit");

        check_Box_Array.add(false);
        check_Box_Array.add(false);
        check_Box_Array.add(false);

        // setup Recycler view
        Setup_Recycler_View();

        // check for empty
//        if(custom_adapter_recycleview.getItemCount()!=0){
//            nothing_Image_View.setVisibility(View.GONE);
//            nothing_Text_View.setVisibility(View.GONE);
//        }

        // fill Recycler View
            Fill_Recycler_View();

        return view;
    }
    private void Reference(){
        recyclerView = view.findViewById(R.id.recycler_View);
        nothing_Image_View = view.findViewById(R.id.nothing_Image_View);
        nothing_Text_View = view.findViewById(R.id.nothing_Text_View);

        // setup onclick listener
        nothing_Text_View.setOnClickListener(this);
        nothing_Image_View.setOnClickListener(this);

        // array start
        task_Name = new ArrayList<>();
        check_Box_Array= new ArrayList<>();

        // database create object
        dataBase= new DataBase(main_Activity);

    }
    private void Setup_Recycler_View(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(main_Activity.getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void Fill_Recycler_View(){
        if(!dataBase.check_For_Empty()){
            Cursor cursor = dataBase.Get_All_Date();
            Toast.makeText(main_Activity,"yoo",Toast.LENGTH_SHORT).show();
            if (cursor != null) {
                cursor.moveToFirst();
               do
                {
                    Toast.makeText(main_Activity, cursor.getString(0)+"",Toast.LENGTH_SHORT).show();


                }while (cursor.moveToNext()); }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nothing_Text_View:
            case R.id.nothing_Image_View:
                Intent intent = new Intent(main_Activity.getApplicationContext(), Create_New_To_Do_List.class);
                startActivity(intent);
                break;

        }
    }
}

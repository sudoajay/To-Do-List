package to_do_list.com.sudoajay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;

import to_do_list.com.sudoajay.DataBase.DataBase;
import to_do_list.com.sudoajay.Fragments.Main_Class_Fragement;
import to_do_list.com.sudoajay.MainActivity;
import to_do_list.com.sudoajay.R;

public class Custom_Adapter_Recycleview extends RecyclerView.Adapter<Custom_Adapter_Recycleview.MyViewHolder> {
    private ArrayList<String> task_Name,task_Info;
    private ArrayList<Boolean> check_Box_Array,similar_Check_Box_Array;
    private ArrayList<Integer> array_Id;
    private MainActivity mainActivity;
    private Main_Class_Fragement main_class_fragement;
    private DataBase dataBase;
    private ActionMode actionMode;
    public Custom_Adapter_Recycleview(MainActivity mainActivity, ArrayList<String> task_Name, ArrayList<Boolean>
            check_Box_Array, ArrayList<String> task_Info, ArrayList<Integer> array_Id, DataBase dataBase,
                                      Main_Class_Fragement main_class_fragement) {
        this.mainActivity = mainActivity;
        this.task_Name = task_Name;
        this.check_Box_Array =check_Box_Array;
        this.task_Info=task_Info;
        this.array_Id=array_Id;
        this.dataBase= dataBase;
        this.main_class_fragement= main_class_fragement;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // set the data in items
        holder.task_Name_Text_View.setText(task_Name.get(position));
        holder.task_Info_Text_View.setText(task_Info.get(position));// Sunday , 10-oct-18 , 1:32 pm
        holder.check_Box.setChecked(check_Box_Array.get(position));

//        // convert to alpha
        if(actionMode == null) {
            if (check_Box_Array.get(position)) {
                holder.task_Name_Text_View.setPaintFlags(holder.task_Name_Text_View.getPaintFlags()
                        | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.task_Name_Text_View.setAlpha(0.3f);
                holder.task_Info_Text_View.setAlpha(0.3f);
                holder.check_Box.setAlpha(0.3f);
            }
        }

        // implement setOnClickListener event on item view.
        holder.check_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
               After_On_Click_Listner(position,holder);
            }
        });

        holder.task_Info_Text_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                After_On_Click_Listner(position,holder);
            }
        });
        holder.task_Name_Text_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                After_On_Click_Listner(position,holder);
            }
        });

        // for long Pressed Button
        holder.task_Info_Text_View.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(actionMode != null) return false;
                main_class_fragement.Make_Them_Default(1,position);
                actionMode = mainActivity.startSupportActionMode(mActionModeCallback);
                return true;
            }
        });
        holder.task_Name_Text_View.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(actionMode != null) return false;
                main_class_fragement.Make_Them_Default(1,position);
                actionMode = mainActivity.startSupportActionMode(mActionModeCallback);
                return true;
            }
        });
    }


        // action mode setup or you say configuration
        // anyomous class
    private ActionMode.Callback mActionModeCallback =new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.top_menu, menu);
                actionMode.setTitle("Choose your option");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.done:
                        Toast.makeText(mainActivity, "Option 1 selected", Toast.LENGTH_SHORT).show();
                        actionMode.finish();
                        return true;
                    case R.id.delete:
                        Toast.makeText(mainActivity, "Option 2 selected", Toast.LENGTH_SHORT).show();
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode Mode) {
                main_class_fragement.Make_Them_Default(2,0);
                actionMode = null;
            }

    };

    private void After_On_Click_Listner(int position ,MyViewHolder holder){
       if(actionMode == null){
        if(check_Box_Array.get(position)) {
            check_Box_Array.set(position, false);
            if ((holder.task_Name_Text_View.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                holder.task_Name_Text_View.setPaintFlags(holder.task_Name_Text_View.getPaintFlags()
                        & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.task_Name_Text_View.setAlpha(1);
                holder.task_Info_Text_View.setAlpha(1);
                holder.check_Box.setAlpha(1);
                dataBase.Update_The_Table_For_Done(array_Id.get(position) + "", 0);
                holder.check_Box.setChecked(false);
            }

        }else{
            check_Box_Array.set(position,true);
            holder.task_Name_Text_View.setPaintFlags(holder.task_Name_Text_View.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.check_Box.setChecked(true);
            holder.task_Name_Text_View.setAlpha(0.3f);
            holder.task_Info_Text_View.setAlpha(0.3f);
            holder.check_Box.setAlpha(0.3f);
            dataBase.Update_The_Table_For_Done(array_Id.get(position)+"",1);
            Toast.makeText(mainActivity,"Well Done You have Finish "+Done_Work_Count()+ " Out Of "+ task_Name.size() ,Toast.LENGTH_SHORT).show();
        }
       }else{

       }
    }
    @Override
    public int getItemCount() {
        return task_Name.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView task_Name_Text_View,task_Info_Text_View;// init the item view's
        private CheckBox check_Box;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            task_Name_Text_View = itemView.findViewById(R.id.task_Name_Text_View);
            task_Info_Text_View = itemView.findViewById(R.id.task_Info_Text_View);
            check_Box= itemView.findViewById(R.id.check_Box);

        }
    }
    private int Done_Work_Count(){
        int count =0;
        for(Boolean check:check_Box_Array) {
            if (check) count++;
        }
        return count;
    }


}

package to_do_list.com.sudoajay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import to_do_list.com.sudoajay.R;

public class Custom_Adapter_Recycleview extends RecyclerView.Adapter<Custom_Adapter_Recycleview.MyViewHolder> {
    private ArrayList<String> task_Name;
    private ArrayList<Boolean> check_Box_Array;
    private Context context;
    public Custom_Adapter_Recycleview(Context context, ArrayList<String> task_Name,ArrayList<Boolean> check_Box_Array) {
        this.context = context;
        this.task_Name = task_Name;
        this.check_Box_Array =check_Box_Array;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // set the data in items
        holder.text_View.setText(task_Name.get(position));
        holder.check_Box.setChecked(check_Box_Array.get(position));
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, task_Name.get(position)+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return task_Name.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text_View;// init the item view's
        private CheckBox check_Box;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            text_View = itemView.findViewById(R.id.text_View);
            check_Box= itemView.findViewById(R.id.check_Box);
        }
    }
}

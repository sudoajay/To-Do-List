package to_do_list.com.sudoajay.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import to_do_list.com.sudoajay.MainActivity;
import to_do_list.com.sudoajay.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Class_Fragement extends Fragment {

    // global variable
    private MainActivity main_Activity;
    public Main_Class_Fragement() {
        // Required empty public constructor
    }
    public Main_Class_Fragement createInstance(MainActivity main_Activity  ) {
        this.main_Activity = main_Activity;
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_class, container, false);
    }

    public void On_Click_Process(View view){
        switch (view.getId()){
            case R.id.nothing_Text:
            case R.id.nothing_Image_View:
                break;

        }
    }

}

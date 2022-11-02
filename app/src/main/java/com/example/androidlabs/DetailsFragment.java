package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailsFragment extends Fragment {
    private Bundle dataFromActvity;
    private long id;
    private AppCompatActivity parentActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        dataFromActvity = getArguments();

        assert dataFromActvity != null;

        String name = dataFromActvity.getString(MainActivity.ITEM_NAME);
        int height = dataFromActvity.getInt(MainActivity.ITEM_HEIGHT);
        int weight = dataFromActvity.getInt(MainActivity.ITEM_MASS);


        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_details, container, false);

        TextView nameText = result.findViewById(R.id.nameText);
        nameText.setText( name );

        TextView heightText = result.findViewById(R.id.heightText);
        heightText.setText( String.valueOf(height) );

        TextView weightText = result.findViewById(R.id.massText);
        weightText.setText( String.valueOf(weight) );

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}
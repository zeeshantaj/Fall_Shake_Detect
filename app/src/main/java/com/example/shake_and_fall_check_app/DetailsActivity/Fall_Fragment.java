package com.example.shake_and_fall_check_app.DetailsActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shake_and_fall_check_app.Adapter.DetectAdapter;
import com.example.shake_and_fall_check_app.Database.Detect_Data_Model;
import com.example.shake_and_fall_check_app.Database.Detect_Database;
import com.example.shake_and_fall_check_app.R;

import java.util.ArrayList;
import java.util.List;

public class Fall_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private DetectAdapter adapter;
    private List<Detect_Data_Model> detectDataModelsList;
    private Detect_Database detectDatabase;
    private TextView nothingTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_history_, container, false);

        recyclerView = view.findViewById(R.id.fallRecyclerView);
        nothingTxt = view.findViewById(R.id.fallNothingShowTxt);
        LottieAnimationView animationView = view.findViewById(R.id.animation_view_fall);
        animationView.playAnimation();



        detectDatabase = Detect_Database.getInstance(getActivity());
        detectDataModelsList = new ArrayList<>();
        detectDataModelsList = detectDatabase.mainDao().getALL();

        if (detectDataModelsList.isEmpty()){
            nothingTxt.setVisibility(View.VISIBLE);
        }else {
            nothingTxt.setVisibility(View.GONE);

            adapter = new DetectAdapter(getActivity(), detectDataModelsList, new DetectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Detect_Data_Model item) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("ACTION REQUIRE")
                            .setMessage("Do you want to Delete This Item")
                            .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("Delete Task", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    detectDatabase.mainDao().delete(item);
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                    detectDataModelsList.remove(item);
                                    adapter.notifyDataSetChanged();


                                }
                            })
                            .show();
                }


            });
            adapter.filteredList("Fall");
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }

        return view;
    }
}
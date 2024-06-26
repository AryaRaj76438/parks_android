package com.example.parks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parks.adapter.ViewPagerAdapter;
import com.example.parks.model.Park;
import com.example.parks.model.ParkViewModel;
import com.example.todo_application.R;

import java.util.ArrayList;
import java.util.List;


public class DetailsFragment extends Fragment {
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager;
    private ParkViewModel parkViewModel;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.details_view_pager);
        parkViewModel = new ViewModelProvider(requireActivity())
                .get(ParkViewModel.class);

        TextView parkName = view.findViewById(R.id.details_park_name);
        TextView parkDes = view.findViewById(R.id.details_park_designation);
        TextView description = view.findViewById(R.id.details_description);
        TextView activities= view.findViewById(R.id.details_activities);
        TextView entranceFees = view.findViewById(R.id.details_entrance_fees);
        TextView opHours = view.findViewById(R.id.details_operating_hours);
        TextView detailsTopics = view.findViewById(R.id.details_topics);
        TextView directions = view.findViewById(R.id.details_directions);

        parkViewModel.getSelectedPark().observe(getViewLifecycleOwner(), new Observer<Park>() {
            @Override
            public void onChanged(Park park) {
                viewPagerAdapter = new ViewPagerAdapter(park.getImages());
                viewPager.setAdapter(viewPagerAdapter);

                parkName.setText(park.getFullName());
                parkDes.setText(park.getDesignation());
                description.setText(park.getDescription());

                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0; i<park.getActivities().size(); i++){
                    stringBuilder.append(park.getActivities().get(i).getName())
                            .append(" | ");
                }
                activities.setText(stringBuilder);

                StringBuilder topicsString = new StringBuilder();
                for (int i = 0; i < park.getTopics().size(); i++) {
                    topicsString.append(park.getTopics().get(i).getName())
                            .append(" | ");
                }
                detailsTopics.setText(topicsString);

                // Set entrance fees
                if(park.getEntranceFees().size()>0){
                    entranceFees.setText(String.format("Cost: $ %s", park.getEntranceFees().get(0).getCost()));
                }else{
                    entranceFees.setText("Information not available");
                }

                // Setting up operating hour
                StringBuilder opsString = new StringBuilder();
                opsString.append("Wednesday: ").append(park.getOperatingHours().get(0)
                        .getStandardHours().getWednesday()).append("\n")
                        .append("Thrusday: ").append(park.getOperatingHours().get(0)
                                .getStandardHours().getThursday()).append("\n")
                        .append("Friday: ").append(park.getOperatingHours().get(0)
                                .getStandardHours().getFriday()).append("\n")
                        .append("Saturday: ").append(park.getOperatingHours().get(0)
                                .getStandardHours().getSaturday()).append("\n")
                        .append("Sunday: ").append(park.getOperatingHours().get(0)
                                .getStandardHours().getSunday()).append("\n")
                        .append("Monday: ").append(park.getOperatingHours().get(0)
                                .getStandardHours().getMonday()).append("\n")
                        .append("Tuesday: ").append(park.getOperatingHours().get(0)
                                .getStandardHours().getTuesday());

                opHours.setText(opsString);

                // Direction
                if(!TextUtils.isEmpty(park.getDirectionsInfo())){
                    directions.setText(park.getDirectionsInfo());
                }else{
                    directions.setText("Direction not available");
                }
                

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }
}
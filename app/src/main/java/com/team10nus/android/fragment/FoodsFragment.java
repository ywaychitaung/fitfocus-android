package com.team10nus.android.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.team10nus.android.R;
import com.team10nus.android.activity.FoodActivity;
import com.team10nus.android.adapter.FoodListViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodsFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    ListView listView;
    String[] items = { "Breakfast", "Lunch", "Dinner" };


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodsFragment newInstance(String param1, String param2) {
        FoodsFragment fragment = new FoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_foods, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize your ListView here
        listView = view.findViewById(R.id.listView);
        FoodListViewAdapter adapter = new FoodListViewAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        // Set the item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Start a new activity when an item is clicked
                // You can use position to start different activities for each item
                Intent intent = new Intent(getActivity(), FoodActivity.class);
                intent.putExtra("ITEM_KEY", items[position]); // Optional: pass data to the new activity
                startActivity(intent);
            }
        });
    }
}
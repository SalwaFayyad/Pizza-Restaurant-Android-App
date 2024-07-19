package com.example.pizza_appproject.ui.CustomerFragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.pizza_appproject.LogIn_Activity;
import com.example.pizza_appproject.MainActivity;
import com.example.pizza_appproject.PizzaAdapter;
import com.example.pizza_appproject.R;

public class favoritepizza extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private PizzaAdapter pizzaAdapter;
    private ImageView imageView;
    private TextView textViewFavouriteAlert;
    private HorizontalScrollView horizontalScrollView;
    private ProgressBar progressBarPizzaMenu;
    public static com.example.pizza_appproject.ui.CustomerFragments.favoritepizza newInstance(String param1, String param2) {
        com.example.pizza_appproject.ui.CustomerFragments.favoritepizza fragment = new com.example.pizza_appproject.ui.CustomerFragments.favoritepizza();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public favoritepizza() {
        // Required empty public constructor
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
        View root = inflater.inflate(R.layout.fragment_favoritepizza, container, false);
        initializeLayout(root);
        return root;
    }
    public void initializeLayout(View root) {
        imageView = root.findViewById(R.id.imageView);
//        horizontalScrollView = root.findViewById(R.id.horizontalScrollView);
        //-----------------
        recyclerView = root.findViewById(R.id.recycler_pizza_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pizzaAdapter = new PizzaAdapter(MainActivity.allPizza,getActivity(), LogIn_Activity.favoritePizza, LogIn_Activity.userEmail,true);
        recyclerView.setAdapter(pizzaAdapter);
    }
}
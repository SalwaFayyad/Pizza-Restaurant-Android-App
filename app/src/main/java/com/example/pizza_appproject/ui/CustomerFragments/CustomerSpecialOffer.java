package com.example.pizza_appproject.ui.CustomerFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pizza_appproject.DataBaseHelper;
import com.example.pizza_appproject.R;
import com.example.pizza_appproject.SpecialOffer;
import com.example.pizza_appproject.SpecialOfferAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerSpecialOffer extends Fragment {

    private RecyclerView recyclerView;
    private SpecialOfferAdapter specialOfferAdapter;
    private List<SpecialOffer> specialOfferList = new ArrayList<>();

    public CustomerSpecialOffer() {
        // Required empty public constructor
    }

    public static CustomerSpecialOffer newInstance(String param1, String param2) {
        CustomerSpecialOffer fragment = new CustomerSpecialOffer();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_special_offer, container, false);

        recyclerView = view.findViewById(R.id.recycler_pizza_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext(), "UserDB", null, 1);
        List<SpecialOffer> allSpecialOffers = dataBaseHelper.getAllSpecialOffers();
        specialOfferList.clear();
        specialOfferList.addAll(filterSpecialOffers(allSpecialOffers));
        allSpecialOffers.clear();
        Log.d("SpecialOffers", "Filtered Special Offers: " + specialOfferList.toString());
        specialOfferAdapter = new SpecialOfferAdapter(getContext(), specialOfferList);
        recyclerView.setAdapter(specialOfferAdapter);

        return view;
    }

    private List<SpecialOffer> filterSpecialOffers(List<SpecialOffer> allSpecialOffers) {
        List<SpecialOffer> validOffers = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
        String currentDateStr = dateFormat.format(new Date());
        Date currentDate;

        try {
            currentDate = dateFormat.parse(currentDateStr);
            Log.d("SpecialOffers", "Current Date: " + currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return validOffers;
        }

        for (SpecialOffer offer : allSpecialOffers) {
            try {
                String endDateStr = offer.getEndDate();
                Log.d("SpecialOffers", "Offer End Date (String): " + endDateStr);

                Date endDate = dateFormat.parse(endDateStr);
                Log.d("SpecialOffers", "Offer End Date (Parsed): " + endDate);

                if (endDate != null && !endDate.before(currentDate)) {
                    validOffers.add(offer);
                    Log.d("SpecialOffers", "Offer added: " + offer);
                } else {
                    Log.d("SpecialOffers", "Offer expired or invalid date: " + offer);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("SpecialOffers", "Error parsing date for offer: " + offer);
            }
        }

        return validOffers;
    }
}

package com.example.pizza_appproject.ui.AdminFragments;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pizza_appproject.DataBaseHelper;
import com.example.pizza_appproject.R;
import com.example.pizza_appproject.SpecialOffer;
import com.example.pizza_appproject.SpecialOfferItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddSpecialOffer extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner pizzaTypeSpinner;
    private Spinner pizzaSizeSpinner;
    private EditText quantityEditText;
    private Button addSpecialOfferItemButton;
    private Button submitSpecialOfferButton;
    private EditText offerNameEditText;
    private EditText totalPriceEditText;
    private CheckBox checkBoxPotatoes;
    private CheckBox checkBoxCola;
    private CheckBox checkBoxExtraCheese;
    private Button startDateButton;
    private Button endDateButton;
    private DatePickerDialog startPickerDialog;
    private DatePickerDialog endPickerDialog;
    private TextView offerDetailsTextView;
    private Button finalSubmit;
    DataBaseHelper dbHelper;
    private List<SpecialOfferItem> specialOfferItems = new ArrayList<>();

    private String[] pizzasNames = {
            "Select Pizza Type",
            "Margarita",
            "Neapolitan",
            "Hawaiian",
            "Pepperoni",
            "New York Style",
            "Calzone",
            "Tandoori Chicken Pizza",
            "BBQ Chicken Pizza",
            "Seafood Pizza",
            "Vegetarian Pizza",
            "Buffalo Chicken Pizza",
            "Mushroom Truffle Pizza",
            "Pesto Chicken Pizza"
    };

    private String[] pizzasSize = {
            "Select Pizza Size",
            "Small",
            "Medium",
            "Large"
    };

    public static AddSpecialOffer newInstance() {
        return new AddSpecialOffer();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dbHelper = new DataBaseHelper(getContext(), "UserDB", null, 1);
        View root = inflater.inflate(R.layout.fragment_add_special_offer, container, false);
        initializeLayout(root);
        initDatePicker();

        addSpecialOfferItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpecialOfferItem();
            }
        });

        submitSpecialOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubmitDialog();
            }
        });

        return root;
    }

    private void initializeLayout(View root) {
        pizzaTypeSpinner = root.findViewById(R.id.pizzatypespinner);
        pizzaSizeSpinner = root.findViewById(R.id.pizzasizespinner);
        quantityEditText = root.findViewById(R.id.quantity_edit_text);
        addSpecialOfferItemButton = root.findViewById(R.id.AddSpecialOfferItem_button);
        submitSpecialOfferButton = root.findViewById(R.id.AddSpecialOffer_button);

        ArrayAdapter<String> pizzaTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, pizzasNames);
        pizzaTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pizzaTypeSpinner.setAdapter(pizzaTypeAdapter);
        pizzaTypeSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> pizzaSizeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, pizzasSize);
        pizzaSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pizzaSizeSpinner.setAdapter(pizzaSizeAdapter);
        pizzaSizeSpinner.setOnItemSelectedListener(this);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                startDateButton.setText(date);
            }
        };

        DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                endDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = DatePickerDialog.THEME_HOLO_LIGHT;

        startPickerDialog = new DatePickerDialog(getContext(), style, startDateSetListener, year, month, day);
        endPickerDialog = new DatePickerDialog(getContext(), style, endDateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        if (month >= 1 && month <= 12) {
            return monthNames[month - 1];
        } else {
            return "Invalid Month";
        }
    }

    private void openStartDatePicker() {
        startPickerDialog.show();
    }

    private void openEndDatePicker() {
        endPickerDialog.show();
    }

    private void addSpecialOfferItem() {
        String selectedPizzaType = pizzaTypeSpinner.getSelectedItem().toString();
        String selectedPizzaSize = pizzaSizeSpinner.getSelectedItem().toString();
        String quantityText = quantityEditText.getText().toString();

        if (selectedPizzaType.equals("Select Pizza Type") || selectedPizzaSize.equals("Select Pizza Size") || quantityText.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityText);
        SpecialOfferItem item = new SpecialOfferItem(selectedPizzaType, selectedPizzaSize, quantity);
        specialOfferItems.add(item);
        Toast.makeText(getContext(), "Special offer item added", Toast.LENGTH_SHORT).show();
        // Clear input fields
        pizzaTypeSpinner.setSelection(0);
        pizzaSizeSpinner.setSelection(0);
        quantityEditText.setText("");
    }

    private void showSubmitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.special_offer_item_card, null);
        // Initialize views
        offerNameEditText = dialogView.findViewById(R.id.offer_name_edit_text);
        totalPriceEditText = dialogView.findViewById(R.id.total_price_text);
        checkBoxPotatoes = dialogView.findViewById(R.id.extras_potatoes);
        checkBoxCola = dialogView.findViewById(R.id.extras_cola);
        checkBoxExtraCheese = dialogView.findViewById(R.id.extras_extra_cheese);
        offerDetailsTextView = dialogView.findViewById(R.id.offer_details_text_view);
        startDateButton = dialogView.findViewById(R.id.startdatebt);
        endDateButton = dialogView.findViewById(R.id.enddatebt);
        offerDetailsTextView = dialogView.findViewById(R.id.offer_details_text_view);
        finalSubmit = dialogView.findViewById(R.id.final_submit);
        updateOfferDetailsTextView();
        startDateButton.setOnClickListener(v -> openStartDatePicker());
        endDateButton.setOnClickListener(v -> openEndDatePicker());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        finalSubmit.setOnClickListener(v -> {
            if (startDateButton.getText().toString().startsWith("Start Date")) {
                Toast.makeText(getContext(), "Please select start date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endDateButton.getText().toString().startsWith("End Date")) {
                Toast.makeText(getContext(), "Please select end date", Toast.LENGTH_SHORT).show();
                return;
            }

            String totalPriceText = totalPriceEditText.getText().toString().trim();

            if (totalPriceText.isEmpty()) {
                totalPriceEditText.setError("Please enter total price");
                return;
            }
            String offerNameText = offerNameEditText.getText().toString().trim();
            if (offerNameText.isEmpty()) {
                offerNameEditText.setError("Please enter offer name");
                return;
            }
            // Gather inputs
            String offerName = offerNameEditText.getText().toString();
            String totalPrice = totalPriceEditText.getText().toString();
            String startDate = startDateButton.getText().toString();
            String endDate = endDateButton.getText().toString();
            String extras = "";

            if (checkBoxPotatoes.isChecked()) {
                extras += "Potatoes, ";
            }
            if (checkBoxCola.isChecked()) {
                extras += " Chat Cola, ";
            }
            if (checkBoxExtraCheese.isChecked()) {
                extras += "Extra Cheese, ";
            }
            if (!extras.isEmpty()) {
                extras = extras.substring(0, extras.length() - 2);
            }

            SpecialOffer specialOffer = new SpecialOffer(offerName, startDate, endDate, Double.parseDouble(totalPrice), extras);
            specialOffer.setSpecialOfferItems(specialOfferItems);
            // Insert the special offer into the database
            long offerId = dbHelper.insertSpecialOffer(specialOffer);
            if (offerId != -1) {
                // Insert each special offer item into the database
                for (SpecialOfferItem item : specialOfferItems) {
                    item.setOfferId(offerId);
                    dbHelper.insertSpecialOfferItem(item);
                }
                Toast.makeText(getContext(), "Special Offer Submitted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Failed to submit Special Offer", Toast.LENGTH_SHORT).show();
            }
            specialOfferItems.clear();
            dialog.dismiss();
        });
    }

    private void updateOfferDetailsTextView() {
        StringBuilder details = new StringBuilder();
        for (SpecialOfferItem item : specialOfferItems) {
            details.append(("Details: ")).append(item.getPizzaName())
                    .append(" - ")
                    .append(item.getQuantity())
                    .append("\n");
        }
        offerDetailsTextView.setText(details.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

package com.oshop.hm.fragments;


import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oshop.hm.R;
import com.oshop.hm.activities.MainActivity;
import com.oshop.hm.app.App;
import com.oshop.hm.app.MyAppPrefsManager;
import com.oshop.hm.constant.ConstantValues;
import com.oshop.hm.customs.DialogLoader;
import com.oshop.hm.models.address_model.AddressData;
import com.oshop.hm.models.address_model.AddressDetails;
import com.oshop.hm.models.address_model.Cities;
import com.oshop.hm.models.address_model.CityDetails;
import com.oshop.hm.models.address_model.Countries;
import com.oshop.hm.models.address_model.CountryDetails;
import com.oshop.hm.models.address_model.ZoneDetails;
import com.oshop.hm.models.address_model.Zones;
import com.oshop.hm.network.APIClient;
import com.oshop.hm.utils.ValidateInputs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Billing_Address extends Fragment {

    View rootView;

    Boolean isUpdate = false;
    String customerID, defaultAddressID;
    int selectedCityID , selectedZoneID, selectedCountryID;

    List<String> zoneNames;
    List<String> countryNames;
    List<String> cityNames;

    List<CityDetails> cityList = new ArrayList<>();
    List<ZoneDetails> zoneList;
    List<CountryDetails> countryList;

    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> zoneAdapter;
    ArrayAdapter<String> countryAdapter;

    Button proceed_checkout_btn;
    CheckBox default_shipping_checkbox;
    //NEW
    MyAppPrefsManager prefsManager;

    EditText input_firstname, input_lastname, input_address, input_country, input_zone, input_city, input_postcode;

    DialogLoader dialogLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.address, container, false);

        if (getArguments() != null) {
            if (getArguments().containsKey("isUpdate")) {
                isUpdate = getArguments().getBoolean("isUpdate", false);
            }
        }


        // Set the Title of Toolbar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.billing_address));

        // Get the customersID and defaultAddressID from SharedPreferences
        customerID = getActivity().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userID", "");
        defaultAddressID = getActivity().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userDefaultAddressID", "");


        // Binding Layout Views
        input_firstname = (EditText) rootView.findViewById(R.id.firstname);
        input_lastname = (EditText) rootView.findViewById(R.id.lastname);
        input_address = (EditText) rootView.findViewById(R.id.address);
        input_country = (EditText) rootView.findViewById(R.id.country);
        input_zone = (EditText) rootView.findViewById(R.id.zone);
        input_city = (EditText) rootView.findViewById(R.id.city);
        input_postcode = (EditText) rootView.findViewById(R.id.postcode);
        default_shipping_checkbox = (CheckBox) rootView.findViewById(R.id.default_shipping_checkbox);
        proceed_checkout_btn = (Button) rootView.findViewById(R.id.save_address_btn);


        // Set KeyListener of some View to null
        input_country.setKeyListener(null);
        input_zone.setKeyListener(null);

        cityNames = new ArrayList<>();
        zoneNames = new ArrayList<>();
        countryNames = new ArrayList<>();
        

        // Set the text of Button
        proceed_checkout_btn.setText(getContext().getString(R.string.next));

        //New
        // Initialize dialog loader
        dialogLoader = new DialogLoader(getContext());
        // set pref manager
        prefsManager = new MyAppPrefsManager(getContext());
        //NEW



        // Request for Countries List
        RequestCountries();


        // If an existing Address is being Edited // from checkout fragment
        if (isUpdate) {
            // Get the Address details from AppContext
            AddressDetails billingAddress = ((App) getContext().getApplicationContext()).getBillingAddress();

            // Set the Address details
            selectedZoneID = billingAddress.getZoneId();
            selectedCountryID = billingAddress.getCountriesId();
            //selectedCityID = addressInfo.getString("addressCityID");

            input_firstname.setText(billingAddress.getFirstname());
            input_lastname.setText(billingAddress.getLastname());
            input_address.setText(billingAddress.getStreet());
            input_country.setText(billingAddress.getCountryName());
            input_zone.setText(billingAddress.getZoneName());
            input_city.setText(billingAddress.getCity());
            input_postcode.setText(billingAddress.getPostcode());

            RequestZones(String.valueOf(selectedCountryID));
           // RequestCities(String.valueOf(selectedZoneID));

            // check if use default shipping address
            if (prefsManager.getBooleanKey("default_billing_address")){
                default_shipping_checkbox.setChecked(true);
                inputsEnabled(false);
            }

        }
        else {
            // Get the Shipping AddressDetails from AppContext that is being Edited
            AddressDetails shippingAddress = ((App) getContext().getApplicationContext()).getShippingAddress();
    
            // Set the Address details
            selectedZoneID = shippingAddress.getZoneId();
            selectedCountryID = shippingAddress.getCountriesId();

            RequestZones(String.valueOf(selectedCountryID));
           // RequestCities(String.valueOf(selectedZoneID));

            input_firstname.setText(shippingAddress.getFirstname());
            input_lastname.setText(shippingAddress.getLastname());
            input_address.setText(shippingAddress.getStreet());
            input_country.setText(shippingAddress.getCountryName());
            input_zone.setText(shippingAddress.getZoneName());
            input_city.setText(shippingAddress.getCity());
            input_postcode.setText(shippingAddress.getPostcode());
    
            // if not updated set it to the default shipping
            prefsManager.setBooleanKey("default_billing_address" , true);
            // Initialize billing address id
            prefsManager.setStringKey("billing_address_id" , "");

            default_shipping_checkbox.setChecked(true);
            inputsEnabled(false);
        }


        // Handle Touch event of input_country EditText
        input_country.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
    
                    countryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                    countryAdapter.addAll(countryNames);
    
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                    dialog.setView(dialogView);
                    dialog.setCancelable(false);
    
                    Button dialog_button = (Button) dialogView.findViewById(R.id.dialog_button);
                    EditText dialog_input = (EditText) dialogView.findViewById(R.id.dialog_input);
                    TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                    ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
    
                    dialog_title.setText(getString(R.string.country));
                    dialog_list.setVerticalScrollBarEnabled(true);
                    dialog_list.setAdapter(countryAdapter);
    
                    dialog_input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                            countryAdapter.getFilter().filter(charSequence);
                        }
                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
    
    
                    final AlertDialog alertDialog = dialog.create();
    
                    dialog_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
    
                    alertDialog.show();
    
    
    
                    dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
                            alertDialog.dismiss();
                            final String selectedItem = countryAdapter.getItem(position);
            
                            int countryID = 0;
                            input_country.setText(selectedItem);
            
                            if (!selectedItem.equalsIgnoreCase("Other")) {
                
                                for (int i=0;  i<countryList.size();  i++) {
                                    if (countryList.get(i).getName().equalsIgnoreCase(selectedItem)) {
                                        // Get the ID of selected Country
                                        countryID = countryList.get(i).getId();
                                    }
                                }
                
                            }
            
                            selectedCountryID = countryID;
            
                            input_zone.setText("");
            
                            // Request for all Zones in the selected Country
                            RequestZones(String.valueOf(selectedCountryID));
                        }
                    });
                }

                return false;
            }
        });

        
        // Handle Touch event of input_zone EditText
        input_zone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP  &&  zoneNames.size() > 0 ) {
    
                    zoneAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                    zoneAdapter.addAll(zoneNames);
    
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                    dialog.setView(dialogView);
                    dialog.setCancelable(false);
    
                    Button dialog_button = (Button) dialogView.findViewById(R.id.dialog_button);
                    EditText dialog_input = (EditText) dialogView.findViewById(R.id.dialog_input);
                    TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                    ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);
    
                    dialog_title.setText(getString(R.string.zone));
                    dialog_list.setVerticalScrollBarEnabled(true);
                    dialog_list.setAdapter(zoneAdapter);
    
                    dialog_input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                            zoneAdapter.getFilter().filter(charSequence);
                        }
                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
    
    
                    final AlertDialog alertDialog = dialog.create();
    
                    dialog_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
    
                    alertDialog.show();
    
    
    
                    dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
                            alertDialog.dismiss();
                            final String selectedItem = zoneAdapter.getItem(position);
            
                            int zoneID = 0;
                            input_zone.setText(selectedItem);
            
                            if (!zoneAdapter.getItem(position).equalsIgnoreCase("Other")) {
                
                                for (int i=0;  i<zoneList.size();  i++) {
                                    if (zoneList.get(i).getName().equalsIgnoreCase(selectedItem)) {
                                        // Get the ID of selected Country
                                        zoneID = zoneList.get(i).getId();
                                    }
                                }
                            }
            
                            selectedZoneID = zoneID;

                            //NEW
                            cityNames.clear();
                            input_city.setText("");

                            //RequestCities(String.valueOf(selectedZoneID));
                        }
                    });
                }

                return false;
            }
        });


        // Handle Touch event of input_city EditText
        /*input_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    cityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                    cityAdapter.addAll(cityNames);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                    dialog.setView(dialogView);
                    dialog.setCancelable(false);

                    Button dialog_button = (Button) dialogView.findViewById(R.id.dialog_button);
                    EditText dialog_input = (EditText) dialogView.findViewById(R.id.dialog_input);
                    TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                    ListView dialog_list = (ListView) dialogView.findViewById(R.id.dialog_list);

                    dialog_title.setText(getString(R.string.city));
                    dialog_list.setVerticalScrollBarEnabled(true);
                    dialog_list.setAdapter(cityAdapter);

                    dialog_input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                            // Filter CityAdapter
                            cityAdapter.getFilter().filter(charSequence);
                        }
                        @Override
                        public void afterTextChanged(Editable s) {}
                    });


                    final AlertDialog alertDialog = dialog.create();

                    dialog_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();



                    dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            alertDialog.dismiss();
                            final String selectedItem = cityAdapter.getItem(position);

                            int cityID = 0;
                            input_city.setText(selectedItem);

                            if (!cityAdapter.getItem(position).equalsIgnoreCase("Other")) {

                                for (int i=0;  i<cityList.size();  i++) {
                                    if (cityList.get(i).getName().equalsIgnoreCase(selectedItem)) {
                                        // Get the ID of selected Country
                                        cityID = cityList.get(i).getId();
                                    }
                                }
                            }

                            selectedCityID = cityID;
                        }
                    });
                }

                return false;
            }
        });*/




        // Handle the Click event of Default Shipping Address CheckBox
        default_shipping_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Check if the CheckBox is Checked
                if (isChecked) {
                    // change is_default_billing_address to true
                    prefsManager.setBooleanKey("default_billing_address" , true);
                    inputsEnabled(false);

                    // Get the Shipping AddressDetails from AppContext that is being Edited
                    AddressDetails shippingAddress = ((App) getContext().getApplicationContext()).getShippingAddress();

                    // Set the Address details
                    selectedZoneID = shippingAddress.getZoneId();
                    selectedCountryID = shippingAddress.getCountriesId();
                    input_firstname.setText(shippingAddress.getFirstname());
                    input_lastname.setText(shippingAddress.getLastname());
                    input_address.setText(shippingAddress.getStreet());
                    input_country.setText(shippingAddress.getCountryName());
                    input_zone.setText(shippingAddress.getZoneName());
                    input_city.setText(shippingAddress.getCity());
                    input_postcode.setText(shippingAddress.getPostcode());

                    input_zone.setFocusableInTouchMode(true);
                    
                }
                else {
                    // change is_default_billing_address to false
                    prefsManager.setBooleanKey("default_billing_address" , false);
                    inputsEnabled(true);

                    input_firstname.setText("");
                    input_lastname.setText("");
                    input_address.setText("");
                    input_country.setText("");
                    input_zone.setText("");
                    input_city.setText("");
                    input_postcode.setText("");
                }
            }
        });


        // Handle the Click event of Proceed Order Button
        proceed_checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate Address Form Inputs
                boolean isValidData = validateAddressForm();

                if (isValidData) {
                    // New Instance of AddressDetails
                    AddressDetails billingAddress = new AddressDetails();

                    billingAddress.setFirstname(input_firstname.getText().toString().trim());
                    billingAddress.setLastname(input_lastname.getText().toString().trim());
                    billingAddress.setCountryName(input_country.getText().toString().trim());
                    billingAddress.setZoneName(input_zone.getText().toString().trim());
                    billingAddress.setCity(input_city.getText().toString().trim());//""+selectedCityID
                    billingAddress.setStreet(input_address.getText().toString().trim());
                    billingAddress.setPostcode(input_postcode.getText().toString().trim());
                    billingAddress.setZoneId(selectedZoneID);
                    billingAddress.setCountriesId(selectedCountryID);

                    // Save the AddressDetails
                    ((App) getContext().getApplicationContext()).setBillingAddress(billingAddress);

                    // check if use default shipping address
                    if (prefsManager.getBooleanKey("default_billing_address") || default_shipping_checkbox.isChecked()){
                        prefsManager.setBooleanKey("default_billing_address", true);
                        prefsManager.setStringKey("billing_address_id" , defaultAddressID );
                        // Check if an Address is being Edited so return to checkout
                        if (isUpdate) {
                            // Navigate to Checkout Fragment
                            ((MainActivity) getContext()).getSupportFragmentManager().popBackStack();
                        }
                        else {
                            // Navigate to Shipping_Methods Fragment
                            Fragment fragment = new Shipping_Methods();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment)
                                    .addToBackStack(null).commit();
                        }
                    }else {
                        // if is update from checkout and at the same time theres billing address add so id not empty
                        // and not equals to default shipping one
                        if(isUpdate && !prefsManager.getStringKey("billing_address_id").isEmpty()
                                && !prefsManager.getStringKey("billing_address_id").equalsIgnoreCase(defaultAddressID)) {
                            updateUserAddress(prefsManager.getStringKey("billing_address_id"));
                        }else {
                            // if not default and no was added :  add it to addresses
                            addUserAddress();
                        }
                    }


                    // Check if an Address is being Edited so return to checkout
                    /*if (isUpdate) {
                        // Navigate to Checkout Fragment
                        ((MainActivity) getContext()).getSupportFragmentManager().popBackStack();
                    }
                    else {
                        // Navigate to Shipping_Methods Fragment
                        Fragment fragment = new Shipping_Methods();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment)
                                .addToBackStack(null).commit();
                    }*/

                }
            }
        });


        return rootView;
    }



    //*********** Get Countries List from the Server ********//

    private void RequestCountries() {

        Call<Countries> call = APIClient.getInstance()
                .getCountries(ConstantValues.LANGUAGE_ID);

        call.enqueue(new Callback<Countries>() {
            @Override
            public void onResponse(Call<Countries> call, Response<Countries> response) {
                
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
    
                        countryList = response.body().getData();
    
                        // Add the Country Names to the countryNames List
                        for (int i=0;  i<countryList.size();  i++) {
                            countryNames.add(countryList.get(i).getName());
                        }
    
                        //countryNames.add("Other");
                        
                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        
                    }
                    else {
                        // Unable to get Success status
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Countries> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }



    //*********** Get Zones List of the Country from the Server ********//

    private void RequestZones(String countryID) {

        Call<Zones> call = APIClient.getInstance()
                .getZones
                        (
                                ConstantValues.LANGUAGE_ID,
                                countryID
                        );

        call.enqueue(new Callback<Zones>() {
            @Override
            public void onResponse(Call<Zones> call, Response<Zones> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
    
                        zoneNames.clear();
                        zoneList = response.body().getData();
    
                        // Add the Zone Names to the zoneNames List
                        for (int i=0;  i<zoneList.size();  i++){
                            zoneNames.add(zoneList.get(i).getName());
                        }
    
                        zoneNames.add("Other");
                        
                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
    
                    }
                    else {
                        // Unable to get Success status
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Zones> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }


    //*********** Get Cities List of the Zone from the Server ********//

    private void RequestCities(String stateID) {

        Call<Cities> call = APIClient.getInstance()
                .getCities
                        (
                                ConstantValues.LANGUAGE_ID,
                                stateID
                        );

        call.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {

                if (response.isSuccessful()) {

                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        cityList = response.body().getData();

                        // Add the Cities Names to the CitiesNames List
                        for (int i=0;  i<cityList.size();  i++){
                            cityNames.add(cityList.get(i).getName());

                            //New
                            try {
                                AddressDetails address;
                                if (isUpdate)
                                    address = ((App) getContext().getApplicationContext()).getBillingAddress();
                                else
                                    address = ((App) getContext().getApplicationContext()).getShippingAddress();
                                if (String.valueOf(i).equalsIgnoreCase(address.getCity()))
                                    input_city.setText(cityList.get(i).getName());
                            }catch (Exception e){

                            }
                        }

                        cityNames.add("Other");

                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                    else {
                        // Unable to get Success status
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }


    //*********** Get the User's Default Address from all the Addresses ********//

    private void filterDefaultAddress(AddressData addressData) {

        // Get CountryList from Response
        List<AddressDetails> addressesList = addressData.getData();

        // Initialize new AddressDetails for DefaultAddress
        AddressDetails defaultAddress = new AddressDetails();


        for (int i=0;  i<addressesList.size();  i++) {
            // Check if the Current Address is User's Default Address
            if (addressesList.get(i).getAddressId() == addressesList.get(i).getDefaultAddress()) {
                // Set the Default AddressDetails
                defaultAddress = addressesList.get(i);
            }
        }

        // make it default
        if (addressesList.size() == 1) {
            My_Addresses.MakeAddressDefault(customerID, String.valueOf(addressesList.get(0).getAddressId()), getContext(), rootView);
        }


        // Request Zones of selected Country
        RequestZones(String.valueOf(selectedCountryID));
        //RequestCities(String.valueOf(selectedZoneID));

        // Set Default Address Data and Display to User
        defaultAddressID = String.valueOf(defaultAddress.getAddressId());
        selectedZoneID = defaultAddress.getZoneId();
        selectedCountryID = defaultAddress.getCountriesId();
        input_firstname.setText(defaultAddress.getFirstname());
        input_lastname.setText(defaultAddress.getLastname());
        input_address.setText(defaultAddress.getStreet());
        input_country.setText(defaultAddress.getCountryName());
        input_zone.setText(defaultAddress.getZoneName());
        input_postcode.setText(defaultAddress.getPostcode());

        input_city.setText(defaultAddress.getCity());
        selectedCityID = -1;


    }



    //*********** Request List of User Addresses ********//

    public void RequestAllAddresses() {

        dialogLoader.showProgressDialog();

        Call<AddressData> call = APIClient.getInstance()
                .getAllAddress
                        (
                                ConstantValues.TOKEN,
                                customerID
                        );

        call.enqueue(new Callback<AddressData>() {
            @Override
            public void onResponse(Call<AddressData> call, Response<AddressData> response) {

                dialogLoader.hideProgressDialog();

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        // Filter all the Addresses to get the Default Address
                        filterDefaultAddress(response.body());

                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {

                    }

                }
            }

            @Override
            public void onFailure(Call<AddressData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    //*********** Proceed the Request of New Address ********//

    public void addUserAddress() {

        //final String customers_default_address_id = getActivity().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userDefaultAddressID", "");
        // NEW website default true or false to make this default shipping address so we put it for billing to false :
        String customers_default_address_id = "0";

        Call<AddressData> call = APIClient.getInstance()
                .addUserAddress
                        (
                                ConstantValues.TOKEN,
                                customerID,
                                input_firstname.getText().toString().trim(),
                                input_lastname.getText().toString().trim(),
                                input_address.getText().toString().trim(),
                                input_postcode.getText().toString().trim(),
                                String.valueOf(input_city.getText().toString().trim()),//,selectedCityID
                                String.valueOf(selectedCountryID),
                                String.valueOf(selectedZoneID),
                                customers_default_address_id
                        );

        call.enqueue(new Callback<AddressData>() {
            @Override
            public void onResponse(Call<AddressData> call, Response<AddressData> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                        // Address has been added to User's Addresses
                        // save address_id to pref
                        prefsManager.setStringKey("billing_address_id" , response.body().getAddressId());
                        // Check if an Address is being Edited so return to checkout
                        if (isUpdate) {
                            // Navigate to Checkout Fragment
                            ((MainActivity) getContext()).getSupportFragmentManager().popBackStack();
                        }
                        else {
                            // Navigate to Shipping_Methods Fragment
                            Fragment fragment = new Shipping_Methods();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment)
                                    .addToBackStack(null).commit();
                        }
                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        Snackbar.make(rootView, response.body().getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                    else {
                        // Unable to get Success status
                        Snackbar.make(rootView, getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddressData> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }



    //*********** Proceed the Request of Update Address ********//

    public void updateUserAddress(String addressID) {

        //final String customers_default_address_id = getActivity().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userDefaultAddressID", "");
        // NEW website default true or false to make this default shipping address so we put it for billing to false :
        String customers_default_address_id = "0";


        Call<AddressData> call = APIClient.getInstance()
                .updateUserAddress
                        (
                                ConstantValues.TOKEN,
                                customerID,
                                addressID,
                                input_firstname.getText().toString().trim(),
                                input_lastname.getText().toString().trim(),
                                input_address.getText().toString().trim(),
                                input_postcode.getText().toString().trim(),
                                String.valueOf(input_city.getText().toString().trim()),//,selectedCityID
                                String.valueOf(selectedCountryID),
                                String.valueOf(selectedZoneID),
                                customers_default_address_id
                        );

        call.enqueue(new Callback<AddressData>() {
            @Override
            public void onResponse(Call<AddressData> call, Response<AddressData> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                        // Address has been Edited
                        // Check if an Address is being Edited so return to checkout
                        if (isUpdate) {
                            // Navigate to Checkout Fragment
                            ((MainActivity) getContext()).getSupportFragmentManager().popBackStack();
                        }
                        else {
                            // Navigate to Shipping_Methods Fragment
                            Fragment fragment = new Shipping_Methods();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment)
                                    .addToBackStack(null).commit();
                        }


                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        // Address has not been Edited
                        // Show the Message to the User
                        Toast.makeText(getContext(), ""+response.body().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressData> call, Throwable t) {
                Toast.makeText(getContext(), "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }




    //*********** Validate Address Form Inputs ********//
    
    private boolean validateAddressForm() {
        if (!ValidateInputs.isValidName(input_firstname.getText().toString().trim())) {
            input_firstname.setError(getString(R.string.invalid_first_name));
            return false;
        } else if (!ValidateInputs.isValidName(input_lastname.getText().toString().trim())) {
            input_lastname.setError(getString(R.string.invalid_last_name));
            return false;
        } else if (!ValidateInputs.isValidInput(input_address.getText().toString().trim())) {
            input_address.setError(getString(R.string.invalid_address));
            return false;
        } else if (!ValidateInputs.isValidInput(input_country.getText().toString().trim())) {
            input_country.setError(getString(R.string.select_country));
            return false;
        } else if (!ValidateInputs.isValidInput(input_zone.getText().toString().trim())) {
            input_zone.setError(getString(R.string.select_zone));
            return false;
        } else if (!ValidateInputs.isValidInput(input_city.getText().toString().trim())) {
            input_city.setError(getString(R.string.enter_city));
            return false;
        } else if (!ValidateInputs.isValidNumber(input_postcode.getText().toString().trim())) {
            input_postcode.setError(getString(R.string.invalid_post_code));
            return false;
        } else {
            return true;
        }
    }

    //*********** Set Prevent Inputs ********//

    private void inputsEnabled(boolean set){
        /*
        * old way
        * textView.setTag(textView.getKeyListener());
        *textView.setKeyListener(null);
        * textView.setKeyListener((KeyListener) textView.getTag());
        * */
        input_firstname.setEnabled(set);
        input_lastname.setEnabled(set);
        input_address.setEnabled(set);
        input_country.setEnabled(set);
        input_zone.setEnabled(set);
        input_city.setEnabled(set);
        input_postcode.setEnabled(set);
    }

    
}


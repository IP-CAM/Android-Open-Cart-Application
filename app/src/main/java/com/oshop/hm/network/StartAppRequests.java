package com.oshop.hm.network;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import com.oshop.hm.R;
//import com.onesignal.OneSignal;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import com.oshop.hm.app.App;
import com.oshop.hm.app.MyAppPrefsManager;
import com.oshop.hm.models.currencies.CurrenciesData;
import com.oshop.hm.models.login_status.LoginStatus;
import com.oshop.hm.utils.Utilities;
import com.oshop.hm.constant.ConstantValues;
import com.oshop.hm.models.user_model.UserData;
import com.oshop.hm.models.banner_model.BannerData;
import com.oshop.hm.models.category_model.CategoryData;
import com.oshop.hm.models.device_model.AppSettingsData;
import com.oshop.hm.models.device_model.DeviceInfo;
import com.oshop.hm.models.pages_model.PagesDetails;
import com.oshop.hm.models.pages_model.PagesData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * StartAppRequests contains some Methods and API Requests, that are Executed on Application Startup
 **/

public class StartAppRequests {

    private App app = new App();


    public StartAppRequests(Context context) {
        app = ((App) context.getApplicationContext());
    }



    //*********** Contains all methods to Execute on Startup ********//

    public void StartRequests(){

        RequestLoginStatus();
        RequestCurrencies();
        RequestBanners();
        RequestAllCategories();
        RequestAppSetting();
        RequestStaticPagesData();
        
    }
    
    
    
    //*********** Register Device to Admin Panel with the Device's Info ********//
    
    public static void RegisterDeviceForFCM(final Context context) {
    
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
    
        String deviceID = "";
        DeviceInfo device = Utilities.getDeviceInfo(context);
        String customer_ID = sharedPreferences.getString("userID", "");
        
        
//        if (ConstantValues.DEFAULT_NOTIFICATION.equalsIgnoreCase("onesignal")) {
//            deviceID = OneSignal.getPermissionSubscriptionState ().getSubscriptionStatus().getUserId();
//        }
//        else if (ConstantValues.DEFAULT_NOTIFICATION.equalsIgnoreCase("fcm")) {
//            deviceID = FirebaseInstanceId.getInstance().getToken();
//        }

        deviceID = FirebaseInstanceId.getInstance().getToken();
        
        
        
        Call<UserData> call = APIClient.getInstance()
                .registerDeviceToFCM
                        (
                                deviceID,
                                device.getDeviceType(),
                                device.getDeviceRAM(),
                                device.getDeviceProcessors(),
                                device.getDeviceAndroidOS(),
                                device.getDeviceLocation(),
                                device.getDeviceModel(),
                                device.getDeviceManufacturer(),
                                customer_ID
                        );
        
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                        
                        Log.i("notification", response.body().getMessage());
//                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    
                    }
                    else {
                        
                        Log.i("notification", response.body().getMessage());
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.i("notification", response.errorBody().toString());
                }
            }
            
            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
//                Toast.makeText(context, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
        
    }


    //*********** API Request Method to Fetch App Banners ********//

    private void RequestLoginStatus() {

        Call<LoginStatus> call = APIClient.getInstance()
                .checkLogin(ConstantValues.TOKEN);

        try {
            Response<LoginStatus> response = call.execute();

            if (response.isSuccessful()) {

                LoginStatus loginStatus = response.body();

                // IF Logged
                if(!loginStatus.getSuccess().equalsIgnoreCase("1")){
                    if (ConstantValues.IS_USER_LOGGED_IN) {
                        // Edit UserID in SharedPreferences
                        SharedPreferences sharedPreferences =  app.getSharedPreferences("UserInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userID", "");
                        editor.apply();

                        // Set UserLoggedIn in MyAppPrefsManager
                        MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(app.getApplicationContext());
                        myAppPrefsManager.setUserLoggedIn(false);

                        // Set isLogged_in of ConstantValues
                        ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //*********** API Request Method to Fetch App Banners ********//

    private void RequestCurrencies() {

        Call<CurrenciesData> call = APIClient.getInstance()
                .getCurrencies(ConstantValues.LANGUAGE_ID);

        try {
            Response<CurrenciesData> response = call.execute();

            CurrenciesData currenciesData = new CurrenciesData();

            if (response.isSuccessful()) {

                currenciesData = response.body();
                /*for(int i =0 ; i< response.body().getData().size(); i++){
                    Log.w("currencies" , response.body().getData().get(i).getTitle());
                    Log.w("currencies" , response.body().getData().get(i).getCode());
                    Log.w("currencies" ,""+ response.body().getData().get(i).getValue());
                    Log.w("currencies" ,""+ response.body().getData().get(i).getSymbol_left());
                    Log.w("currencies" ,""+ response.body().getData().get(i).getSymbol_right());
                }*/

                //Set default AED currency
                MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(app.getApplicationContext());
                myAppPrefsManager.setAedCurrencyRate(0);
                myAppPrefsManager.setAedCurrencySymbol("AED");

                if (!currenciesData.getSuccess().isEmpty()){
                    if(currenciesData.getData().size()>0) {
                        for(int i = 0 ; i< currenciesData.getData().size() ; i++){
                            if(currenciesData.getData().get(i).getCode().equalsIgnoreCase("AED")){
                                float rate = currenciesData.getData().get(i).getValue().floatValue();
                                String symbol = currenciesData.getData().get(i).getCode();
                                myAppPrefsManager.setAedCurrencyRate(rate);
                                myAppPrefsManager.setAedCurrencySymbol(symbol);
                            }
                        }

                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //*********** API Request Method to Fetch App Banners ********//

    private void RequestBanners() {
    
        Call<BannerData> call = APIClient.getInstance()
                .getBanners(ConstantValues.LANGUAGE_ID);
    
        try {
            Response<BannerData> response = call.execute();
    
            BannerData bannerData = new BannerData();
        
            if (response.isSuccessful()) {

                /*for(int i =0 ; i< response.body().getData().size(); i++){
                    Log.w("banners" , response.body().getData().get(i).getImage());
                }*/
    
                bannerData = response.body();
    
                if (!bannerData.getSuccess().isEmpty())
                    app.setBannersList(bannerData.getData());
            
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }



    //*********** API Request Method to Fetch All Categories ********//
    
    private void RequestAllCategories() {
    
        Call<CategoryData> call = APIClient.getInstance()
                .getAllCategories
                        (
                                ConstantValues.LANGUAGE_ID
                        );
    
        try {
            Response<CategoryData> response = call.execute();
        
            CategoryData categoryData = new CategoryData();
        
            if (response.isSuccessful()) {
                /*for(int i =0 ; i< response.body().getData().size(); i++){
                    Log.w("Categories" , response.body().getData().get(i).getImage());
                }*/
            
                categoryData = response.body();
            
                if (!categoryData.getSuccess().isEmpty())
                    app.setCategoriesList(categoryData.getData());
            
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }



    //*********** Request App Settings from the Server ********//
    
    private void RequestAppSetting() {
    
        Call<AppSettingsData> call = APIClient.getInstance()
                .getAppSetting();
    
        try {
            Response<AppSettingsData> response = call.execute();
        
            if (response.isSuccessful()) {


                //Log.w("settings" , response.body().getAppDetails().get(0).getAppName());
    
                AppSettingsData appSettingsData = null;
    
                appSettingsData = response.body();
    
                if (!appSettingsData.getSuccess().isEmpty())
                    app.setAppSettingsDetails(appSettingsData.getProductData().get(0));
            
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
    
    
    
    //*********** Request Static Pages Data from the Server ********//
    
    private void RequestStaticPagesData() {
    
        ConstantValues.ABOUT_US = app.getString(R.string.lorem_ipsum);
        ConstantValues.TERMS_SERVICES = app.getString(R.string.lorem_ipsum);
        ConstantValues.PRIVACY_POLICY = app.getString(R.string.lorem_ipsum);
        ConstantValues.REFUND_POLICY = app.getString(R.string.lorem_ipsum);
    
    
        Call<PagesData> call = APIClient.getInstance()
                .getStaticPages
                        (
                                ConstantValues.LANGUAGE_ID
                        );
    
        try {
            Response<PagesData> response = call.execute();
    
            PagesData pages = new PagesData();
            
            if (response.isSuccessful()) {
                
                pages = response.body();
                //Log.w("php $" , pages.getMessage());
    
                if (pages.getSuccess().equalsIgnoreCase("1")) {
        
                    app.setStaticPagesDetails(pages.getPagesData());
        
                    for (int i=0;  i<pages.getPagesData().size();  i++) {
                        /*Log.w("php $" , pages.getPagesData().get(i).getName());
                        Log.w("php $" , pages.getPagesData().get(i).getDescription());
                        Log.w("php $" , pages.getPagesData().get(i).getSlug());
                        Log.w("php $" , pages.getPagesData().get(i).getStatus());*/
                        PagesDetails page = pages.getPagesData().get(i);
            
                        if (page.getSlug().equalsIgnoreCase("about-us")) {
                            ConstantValues.ABOUT_US = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("term-services")) {
                            ConstantValues.TERMS_SERVICES = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("privacy-policy")) {
                            ConstantValues.PRIVACY_POLICY = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("shiping-delivery")) {
                            ConstantValues.DELIVERY_POLICY = page.getDescription();
                        }
                        else if (page.getSlug().equalsIgnoreCase("refund-policy")) {
                            ConstantValues.REFUND_POLICY = page.getDescription();
                        }
                    }
                }else {
                    //Log.w("php $" , response.body().getMessage());
                }
            }else {
               // Log.w("php $" , response.message());
            }
        
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}

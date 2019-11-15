package com.oshop.hm.network;


import com.oshop.hm.models.address_model.AddressData;
import com.oshop.hm.models.address_model.Cities;
import com.oshop.hm.models.address_model.Countries;
import com.oshop.hm.models.address_model.Zones;
import com.oshop.hm.models.banner_model.BannerData;
import com.oshop.hm.models.category_model.CategoryData;
import com.oshop.hm.models.contact_model.ContactUsData;
import com.oshop.hm.models.currencies.CurrenciesData;
import com.oshop.hm.models.device_model.AppSettingsData;
import com.oshop.hm.models.filter_model.get_filters.FilterData;
import com.oshop.hm.models.language_model.LanguageData;
import com.oshop.hm.models.login_status.LoginStatus;
import com.oshop.hm.models.news_model.all_news.NewsData;
import com.oshop.hm.models.news_model.news_categories.NewsCategoryData;
import com.oshop.hm.models.pages_model.PagesData;
import com.oshop.hm.models.phone_model.PhoneData;
import com.oshop.hm.models.product_model.GetAllProducts;
import com.oshop.hm.models.coupons_model.CouponsData;
import com.oshop.hm.models.payment_model.PaymentMethodsData;
import com.oshop.hm.models.shipping_model.PostTaxAndShippingData;
import com.oshop.hm.models.order_model.OrderData;
import com.oshop.hm.models.payment_model.GetBrainTreeToken;
import com.oshop.hm.models.order_model.PostOrder;
import com.oshop.hm.models.product_model.ProductData;
import com.oshop.hm.models.search_model.SearchData;
import com.oshop.hm.models.shipping_model.ShippingRateData;
import com.oshop.hm.models.user_model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * APIRequests contains all the Network Request Methods with relevant API Endpoints
 **/
/*
eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImM5YjA5MzVhMzc4ODRlMTMwYjkyMGIyZGE5MzBiNWQ2N2FkOTFiOTQ0ZDY5YjFlYjI1NjA4NDNjMGMxMWNlZWIzNDJlYWQ0Y2EwMmFjNThhIn0.eyJhdWQiOiIxIiwianRpIjoiYzliMDkzNWEzNzg4NGUxMzBiOTIwYjJkYTkzMGI1ZDY3YWQ5MWI5NDRkNjliMWViMjU2MDg0M2MwYzExY2VlYjM0MmVhZDRjYTAyYWM1OGEiLCJpYXQiOjE1MzI5Nzg5NTksIm5iZiI6MTUzMjk3ODk1OSwiZXhwIjoxNTY0NTE0OTU5LCJzdWIiOiIxNyIsInNjb3BlcyI6W119.k3_Fsz4daKV9X7Ehp82HVkROfriC2NGdduy48Y3GbGbLLXCA8HIBYHk2VXQuZOpMf4_ZGUOyDfV10MD_Jw7h1DXpkZ6p6ltNjPNf3-rc_5_1X5pq3I0gDqDPdok0Y01I-V_vBRTSmnkEG_4QDItHZYzH9xcnPGbCFWkMBqA9jhwrtEIIGLrCxYnuuAOs6Ko3hsiR_w3EBW_WDVyDVGzh9JvCtzMkuXYYCl-G6WY6xTXjEe0Z6rVAFfgLMnXmXdOve5DhogkuHee6ACTS-ITqVrecSk3lbXhANxo4Qxgpo-hfL_6Om-bEivMD7oTISVH2dqRD0J_-vym9Xl-gqeHZUabL7_WaJtIM7BCdc6flZcD8z1rlDkUDo3PmZbzAMt0vyVNIUd7JzhUtwJuCV7pWF8_2KcQJu9X242F1MBO0o1lb8ZH2oLcUby6ElwEWN-T-zPd5Z9vGQ3pVavowy4dfBy7EjB-ZMwUkHyyhuRFs0X1pl1BGKcMvTFmp3Cs096CpVec7kIUc9rt7QqBMl1OGxTGxt4UO1inNQzzEQgKx3U_GUqFVH0HXGHSecHAELBKtaAcDDqB5_1B0vuNqRb8GrUj-V6KwcVkSrpvybtv4BvMDdn0a_Ch_nj9YjNo4I-YZvrTUAQfc7MDC8kmNTU4UxICfrxqkc-Vibyo3j53fRxk
 */
public interface APIRequests {

    //******************** User Data ********************//

    @FormUrlEncoded
    @POST("index.php?route=api/app/customer/processRegistration")
    Call<UserData> processRegistration(     @Field("customers_firstname") String customers_firstname,
                                            @Field("customers_lastname") String customers_lastname,
                                            @Field("customers_email_address") String customers_email_address,
                                            @Field("customers_password") String customers_password,
                                            @Field("customers_telephone") String customers_telephone,
                                            @Field("customers_picture") String customers_picture);

    @FormUrlEncoded
    @POST("index.php?route=api/app/customer/processLogin")
    Call<UserData> processLogin(            @Field("customers_email_address") String customers_email_address,
                                            @Field("customers_password") String customers_password );

    @FormUrlEncoded
    @POST("index.php?route=api/app/customer/facebookRegistration")
    Call<UserData> facebookRegistration(    @Field("access_token") String access_token);

    @FormUrlEncoded
    @POST("index.php?route=api/app/customer/googleRegistration")
    Call<UserData> googleRegistration(      @Field("idToken") String idToken
                                            /*,@Field("userId") String userId,
                                            @Field("givenName") String givenName,
                                            @Field("familyName") String familyName,
                                            @Field("email") String email,
                                            @Field("imageUrl") String imageUrl*/
                                            );

    @FormUrlEncoded
    @POST("index.php?route=api/app/customer/processForgotPassword")
    Call<UserData> processForgotPassword(   @Field("customers_email_address") String customers_email_address );

    //Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImM5YjA5MzVhMzc4ODRlMTMwYjkyMGIyZGE5MzBiNWQ2N2FkOTFiOTQ0ZDY5YjFlYjI1NjA4NDNjMGMxMWNlZWIzNDJlYWQ0Y2EwMmFjNThhIn0.eyJhdWQiOiIxIiwianRpIjoiYzliMDkzNWEzNzg4NGUxMzBiOTIwYjJkYTkzMGI1ZDY3YWQ5MWI5NDRkNjliMWViMjU2MDg0M2MwYzExY2VlYjM0MmVhZDRjYTAyYWM1OGEiLCJpYXQiOjE1MzI5Nzg5NTksIm5iZiI6MTUzMjk3ODk1OSwiZXhwIjoxNTY0NTE0OTU5LCJzdWIiOiIxNyIsInNjb3BlcyI6W119.k3_Fsz4daKV9X7Ehp82HVkROfriC2NGdduy48Y3GbGbLLXCA8HIBYHk2VXQuZOpMf4_ZGUOyDfV10MD_Jw7h1DXpkZ6p6ltNjPNf3-rc_5_1X5pq3I0gDqDPdok0Y01I-V_vBRTSmnkEG_4QDItHZYzH9xcnPGbCFWkMBqA9jhwrtEIIGLrCxYnuuAOs6Ko3hsiR_w3EBW_WDVyDVGzh9JvCtzMkuXYYCl-G6WY6xTXjEe0Z6rVAFfgLMnXmXdOve5DhogkuHee6ACTS-ITqVrecSk3lbXhANxo4Qxgpo-hfL_6Om-bEivMD7oTISVH2dqRD0J_-vym9Xl-gqeHZUabL7_WaJtIM7BCdc6flZcD8z1rlDkUDo3PmZbzAMt0vyVNIUd7JzhUtwJuCV7pWF8_2KcQJu9X242F1MBO0o1lb8ZH2oLcUby6ElwEWN-T-zPd5Z9vGQ3pVavowy4dfBy7EjB-ZMwUkHyyhuRFs0X1pl1BGKcMvTFmp3Cs096CpVec7kIUc9rt7QqBMl1OGxTGxt4UO1inNQzzEQgKx3U_GUqFVH0HXGHSecHAELBKtaAcDDqB5_1B0vuNqRb8GrUj-V6KwcVkSrpvybtv4BvMDdn0a_Ch_nj9YjNo4I-YZvrTUAQfc7MDC8kmNTU4UxICfrxqkc-Vibyo3j53fRxk
    @FormUrlEncoded
    /*@Headers("Authorization: Bearer " + token )
    @Headers({
            "Accept: application/json",
            "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImM5YjA5MzVhMzc4ODRlMTMwYjkyMGIyZGE5MzBiNWQ2N2FkOTFiOTQ0ZDY5YjFlYjI1NjA4NDNjMGMxMWNlZWIzNDJlYWQ0Y2EwMmFjNThhIn0.eyJhdWQiOiIxIiwianRpIjoiYzliMDkzNWEzNzg4NGUxMzBiOTIwYjJkYTkzMGI1ZDY3YWQ5MWI5NDRkNjliMWViMjU2MDg0M2MwYzExY2VlYjM0MmVhZDRjYTAyYWM1OGEiLCJpYXQiOjE1MzI5Nzg5NTksIm5iZiI6MTUzMjk3ODk1OSwiZXhwIjoxNTY0NTE0OTU5LCJzdWIiOiIxNyIsInNjb3BlcyI6W119.k3_Fsz4daKV9X7Ehp82HVkROfriC2NGdduy48Y3GbGbLLXCA8HIBYHk2VXQuZOpMf4_ZGUOyDfV10MD_Jw7h1DXpkZ6p6ltNjPNf3-rc_5_1X5pq3I0gDqDPdok0Y01I-V_vBRTSmnkEG_4QDItHZYzH9xcnPGbCFWkMBqA9jhwrtEIIGLrCxYnuuAOs6Ko3hsiR_w3EBW_WDVyDVGzh9JvCtzMkuXYYCl-G6WY6xTXjEe0Z6rVAFfgLMnXmXdOve5DhogkuHee6ACTS-ITqVrecSk3lbXhANxo4Qxgpo-hfL_6Om-bEivMD7oTISVH2dqRD0J_-vym9Xl-gqeHZUabL7_WaJtIM7BCdc6flZcD8z1rlDkUDo3PmZbzAMt0vyVNIUd7JzhUtwJuCV7pWF8_2KcQJu9X242F1MBO0o1lb8ZH2oLcUby6ElwEWN-T-zPd5Z9vGQ3pVavowy4dfBy7EjB-ZMwUkHyyhuRFs0X1pl1BGKcMvTFmp3Cs096CpVec7kIUc9rt7QqBMl1OGxTGxt4UO1inNQzzEQgKx3U_GUqFVH0HXGHSecHAELBKtaAcDDqB5_1B0vuNqRb8GrUj-V6KwcVkSrpvybtv4BvMDdn0a_Ch_nj9YjNo4I-YZvrTUAQfc7MDC8kmNTU4UxICfrxqkc-Vibyo3j53fRxk"
    })*/
    @POST("index.php?route=api/app/customer/updateCustomerInfo")
    Call<UserData> updateCustomerInfo(      @Field("Authorization") String token ,
                                            @Field("customers_id") String customers_id,
                                            @Field("customers_firstname") String customers_firstname,
                                            @Field("customers_lastname") String customers_lastname,
                                            @Field("customers_telephone") String customers_telephone,
                                            @Field("customers_dob") String customers_dob,
                                            @Field("customers_picture") String customers_picture,
                                            @Field("customers_old_picture") String customers_old_picture,
                                            @Field("customers_password") String customers_password );

    @FormUrlEncoded
    @POST("index.php?route=api/app/verify_phone/verifyPhoneNumber")
    Call<PhoneData> verifyPhoneNumber(
                                            @Field("Authorization") String token ,
                                            @Field("phone_token") String phone_token);
    
    
    
    //******************** Address Data ********************//

    @FormUrlEncoded
    @POST("index.php?route=api/app/address/getCountries")
    Call<Countries> getCountries(           @Field("language_id") int language_id);

    @FormUrlEncoded
    @POST("index.php?route=api/app/address/getZones")
    Call<Zones> getZones(                   @Field("language_id") int language_id ,
                                            @Field("zone_country_id") String zone_country_id);

    @FormUrlEncoded
    @POST("index.php?route=api/app/address/getCities")
    Call<Cities> getCities(                 @Field("language_id") int language_id,
                                            @Field("state_id") String state_id);


    @FormUrlEncoded
    @POST("index.php?route=api/app/address/getAllAddress")
    Call<AddressData> getAllAddress(
                                            @Field("Authorization") String token,
                                            @Field("customers_id") String customers_id);

    @FormUrlEncoded
    @POST("index.php?route=api/app/address/addShippingAddress")
    Call<AddressData> addUserAddress(
                                            @Field("Authorization") String token,
                                            @Field("customers_id") String customers_id,
                                            @Field("entry_firstname") String entry_firstname,
                                            @Field("entry_lastname") String entry_lastname,
                                            @Field("entry_street_address") String entry_street_address,
                                            @Field("entry_postcode") String entry_postcode,
                                            @Field("entry_city") String entry_city,
                                            @Field("entry_country_id") String entry_country_id,
                                            @Field("entry_zone_id") String entry_zone_id,
                                            @Field("customers_default_address_id") String customers_default_address_id );

    @FormUrlEncoded
    @POST("index.php?route=api/app/address/updateShippingAddress")
    Call<AddressData> updateUserAddress(
                                            @Field("Authorization") String token,
                                            @Field("customers_id") String customers_id,
                                            @Field("address_id") String address_id,
                                            @Field("entry_firstname") String entry_firstname,
                                            @Field("entry_lastname") String entry_lastname,
                                            @Field("entry_street_address") String entry_street_address,
                                            @Field("entry_postcode") String entry_postcode,
                                            @Field("entry_city") String entry_city,
                                            @Field("entry_country_id") String entry_country_id,
                                            @Field("entry_zone_id") String entry_zone_id,
                                            @Field("customers_default_address_id") String customers_default_address_id );

    @FormUrlEncoded
    @POST("index.php?route=api/app/address/updateDefaultAddress")
    Call<AddressData> updateDefaultAddress(
                                            @Field("Authorization") String token,
                                            @Field("customers_id") String customers_id,
                                            @Field("address_id") String address_book_id ); // FIELD WAS : 'address_book_id'

    @FormUrlEncoded
    @POST("index.php?route=api/app/address/deleteShippingAddress")
    Call<AddressData> deleteUserAddress(
                                            @Field("Authorization") String token,
                                            @Field("customers_id") String customers_id,
                                            @Field("address_id") String address_book_id ); // FIELD WAS : 'address_book_id'

    

    //******************** Category Data ********************//

    @FormUrlEncoded
    @POST("index.php?route=api/app/categories")
    Call<CategoryData> getAllCategories(    @Field("language_id") int language_id);
    


    //******************** Product Data ********************//
    //@Field("Authorization") String token, added in form to avoid errors
    @POST("index.php?route=api/app/products")
    Call<ProductData> getAllProducts(
                                            //@Header("Authorization") String token,
                                            @Body GetAllProducts getAllProducts
    );



    @FormUrlEncoded
    @POST("index.php?route=api/app/products/likeProduct")
    Call<ProductData> likeProduct(
                                            @Field("Authorization") String token,
                                            @Field("liked_products_id") int liked_products_id,
                                            @Field("liked_customers_id") String liked_customers_id );

    @FormUrlEncoded
    @POST("index.php?route=api/app/products/unlikeProduct")
    Call<ProductData> unlikeProduct(
                                            @Field("Authorization") String token,
                                            @Field("liked_products_id") int liked_products_id,
                                            @Field("liked_customers_id") String liked_customers_id );
    

    @FormUrlEncoded
    @POST("index.php?route=api/app/products/getFilters")
    Call<FilterData> getFilters(
                                            @Field("Authorization") String token,
                                            @Field("categories_id") int categories_id,
                                            @Field("language_id") int language_id);


    @FormUrlEncoded
    @POST("index.php?route=api/app/products/getSearchData")
    Call<SearchData> getSearchData(         @Field("searchValue") String searchValue,
                                            @Field("language_id") int language_id);


    
    //******************** News Data ********************//

    @FormUrlEncoded
    @POST("index.php?route=api/app/news")
    Call<NewsData> getAllNews(              @Field("language_id") int language_id,
                                            @Field("page_number") int page_number,
                                            @Field("is_feature") int is_feature,
                                            @Field("categories_id") String categories_id);

    @FormUrlEncoded
    @POST("index.php?route=api/app/news/allNewsCategories")
    Call<NewsCategoryData> allNewsCategories(@Field("language_id") int language_id,
                                            @Field("page_number") int page_number);



    //******************** Order Data ********************//
    
    @POST("index.php?route=api/app/order/addToOrder")
    Call<OrderData> addToOrder(
                                            //@Header("Authorization") String token,
                                            @Body PostOrder postOrder);

    @FormUrlEncoded
    @POST("index.php?route=api/app/order/getOrders")
    Call<OrderData> getOrders(
                                            @Field("Authorization") String token,
                                            @Field("customers_id") String customers_id,
                                            @Field("language_id") int language_id);


    @FormUrlEncoded
    @POST("index.php?route=api/app/order/getCoupon")
    Call<CouponsData> getCouponInfo(
                                            @Field("Authorization") String token,
                                            @Field("code") String code);

    @FormUrlEncoded
    @POST("index.php?route=api/app/payment/getPaymentMethods")
    Call<PaymentMethodsData> getPaymentMethods(
                                            @Field("Authorization") String token,
                                            @Field("payment_address_id") String address_id,
                                            @Field("language_id") int language_id);

    @GET("index.php?route=api/app/order/generateBraintreeToken")
    Call<GetBrainTreeToken> generateBraintreeToken(
                                            @Header("Authorization") String token);



    //******************** Banner Data ********************//

    @GET("index.php?route=api/app/banners")
    Call<BannerData> getBanners(            @Query("language_id") int language_id);


    
    //******************** Tax & Shipping Data ********************//

    @POST("index.php?route=api/app/shipping/getRate")
    Call<ShippingRateData> getShippingMethodsAndTax(
                                            //@Header("Authorization") String token,
                                            @Body PostTaxAndShippingData postTaxAndShippingData);



    //******************** Contact Us Data ********************//

    @FormUrlEncoded
    @POST("index.php?route=api/app/contactUs")
    Call<ContactUsData> contactUs(          @Field("name") String name,
                                            @Field("email") String email,
                                            @Field("message") String message);


    
    //******************** Languages Data ********************//
    
    @GET("index.php?route=api/app/languages")
    Call<LanguageData> getLanguages();


    
    //******************** App Settings Data ********************//

    @GET("index.php?route=api/app/settings")
    Call<AppSettingsData> getAppSetting();
    
    
    
    //******************** Static Pages Data ********************//
    
    @FormUrlEncoded
    @POST("index.php?route=api/app/getAllPages")
    Call<PagesData> getStaticPages(         @Field("language_id") int language_id);
    
    
    
    //******************** Notifications Data ********************//
    
    @FormUrlEncoded
    @POST("index.php?route=api/app/registerDevices")
    Call<UserData> registerDeviceToFCM(     @Field("device_id") String device_id,
                                            @Field("device_type") String device_type,
                                            @Field("ram") String ram,
                                            @Field("processor") String processor,
                                            @Field("device_os") String device_os,
                                            @Field("location") String location,
                                            @Field("device_model") String device_model,
                                            @Field("manufacturer") String manufacturer,
                                            @Field("customers_id") String customers_id);


    @FormUrlEncoded
    @POST("index.php?route=api/app/notify_me")
    Call<ContactUsData> notify_me(          @Field("is_notify") String is_notify,
                                            @Field("device_id") String device_id);

    //******************** Currencies Data ********************//

    //@FormUrlEncoded not needed for Get method
    @GET("index.php?route=api/app/currencies")
    Call<CurrenciesData> getCurrencies( @Query("language_id") int language_id   );

    //******************** Check Login ********************//

    // Check login status
    @FormUrlEncoded
    @POST("index.php?route=api/app/checklogin")
    Call<LoginStatus> checkLogin(@Field("Authorization") String token   );

    // Logout the customer
    @FormUrlEncoded
    @POST("index.php?route=api/app/checklogin/logout")
    Call<LoginStatus> logout(@Field("Authorization") String token   );





}


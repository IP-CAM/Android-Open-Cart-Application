package com.oshop.hm.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.content.Intent;
import android.graphics.Paint;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;

import com.oshop.hm.activities.MainActivity;
import com.oshop.hm.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oshop.hm.app.MyAppPrefsManager;
import com.oshop.hm.customs.DialogLoader;
import com.oshop.hm.models.product_model.GetAllProducts;
import com.oshop.hm.models.product_model.Option;
import com.oshop.hm.models.product_model.Value;
import com.oshop.hm.network.APIClient;
import com.oshop.hm.utils.Utilities;
import com.oshop.hm.activities.Login;
import com.oshop.hm.adapters.ProductAttributesAdapter;
import com.oshop.hm.constant.ConstantValues;
import com.oshop.hm.models.cart_model.CartProduct;
import com.oshop.hm.models.cart_model.CartProductAttributes;
import com.oshop.hm.models.product_model.Attribute;
import com.oshop.hm.models.product_model.Image;
import com.oshop.hm.models.product_model.ProductData;
import com.oshop.hm.models.product_model.ProductDetails;

import retrofit2.Call;
import retrofit2.Callback;


public class Product_Description extends Fragment {

    View rootView;
    int productID;
    String customerID;
    double attributesPrice;
    double productBasePrice;
    double productFinalPrice;

    Button productCartBtn;
    ImageView sliderImageView;
    SliderLayout sliderLayout;
    PagerIndicator pagerIndicator;
    ImageButton product_share_btn;
    ToggleButton product_like_btn;
    LinearLayout product_attributes;
    RecyclerView attribute_recycler;
    WebView product_description_webView;
    TextView title, category, price_new, price_old,product_price_aed, product_stock, product_likes, product_tag_new, product_tag_discount;

    DialogLoader dialogLoader;
    ProductDetails productDetails;
    ProductAttributesAdapter attributesAdapter;

    List<Image> itemImages = new ArrayList<>();
    List<Attribute> attributesList = new ArrayList<>();
    List<CartProductAttributes> selectedAttributesList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.product_description, container, false);


        // Set the Title of Toolbar
        MainActivity.actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.product_description));

        // Get the CustomerID from SharedPreferences
        customerID = this.getContext().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userID", "");


        // Binding Layout Views
        title = (TextView) rootView.findViewById(R.id.product_title);
        category = (TextView) rootView.findViewById(R.id.product_category);
        price_old = (TextView) rootView.findViewById(R.id.product_price_old);
        price_new = (TextView) rootView.findViewById(R.id.product_price_new);
        product_price_aed = (TextView) rootView.findViewById(R.id.product_price_aed);
        product_stock = (TextView) rootView.findViewById(R.id.product_stock);
        product_likes = (TextView) rootView.findViewById(R.id.product_total_likes);
        product_tag_new = (TextView) rootView.findViewById(R.id.product_tag_new);
        product_tag_discount = (TextView) rootView.findViewById(R.id.product_tag_discount);
        product_description_webView = (WebView) rootView.findViewById(R.id.product_description_webView);
        sliderLayout = (SliderLayout) rootView.findViewById(R.id.product_cover_slider);
        pagerIndicator = (PagerIndicator) rootView.findViewById(R.id.product_slider_indicator);
        product_like_btn = (ToggleButton) rootView.findViewById(R.id.product_like_btn);
        product_share_btn = (ImageButton) rootView.findViewById(R.id.product_share_btn);
        product_attributes = (LinearLayout) rootView.findViewById(R.id.product_attributes);
        attribute_recycler = (RecyclerView) rootView.findViewById(R.id.product_attributes_recycler);
        productCartBtn = (Button) rootView.findViewById(R.id.product_cart_btn);
    
        
        product_tag_new.setVisibility(View.GONE);
        product_tag_discount.setVisibility(View.GONE);
        product_attributes.setVisibility(View.VISIBLE);
    
        attribute_recycler.setNestedScrollingEnabled(false);

        // Set Paint flag on price_old TextView that applies a strike-through decoration to price_old Text
        price_old.setPaintFlags(price_old.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);


        dialogLoader = new DialogLoader(getContext());
    
        
        selectedAttributesList = new ArrayList<>();
    
        // Get product Info from bundle arguments
        if (getArguments() != null) {
        
            if (getArguments().containsKey("itemID")) {
                productID = getArguments().getInt("itemID");
            
                // Request Product Details
                RequestProductDetail(productID);
            
            }
            else if (getArguments().containsKey("productDetails")) {
                productDetails = getArguments().getParcelable("productDetails");
            
                // Set Product Details
                setProductDetails(productDetails);
            }
        }


        return rootView;

    }



    //*********** Adds Product's Details to the Views ********//

    private void setProductDetails(final ProductDetails productDetails) {

        // Get Product Images and Attributes
        itemImages = productDetails.getImages();
        attributesList = productDetails.getAttributes();


        // Setup the ImageSlider of Product Images
        ImageSlider(productDetails.getProductsImage(), itemImages);


        // Set Product's Information
        title.setText(productDetails.getProductsName());
        category.setText(productDetails.getCategoriesName());
        

        if (productDetails.getProductsLiked() > 0) {
            product_likes.setText(getString(R.string.likes) + " (" + String.valueOf(productDetails.getProductsLiked()) +")");
        }
        else {
            product_likes.setText(getString(R.string.likes) + " (0)");
        }


        // Check Discount on Product with the help of static method of Helper class
        String discount = Utilities.checkDiscount(productDetails.getProductsPrice(), productDetails.getDiscountPrice());

        if (discount != null) {
            productDetails.setIsSaleProduct("1");
            
            // Set Discount Tag
            product_tag_discount.setVisibility(View.VISIBLE);
            product_tag_discount.setText(discount);
            // Set Price info based on Discount
            price_old.setVisibility(View.VISIBLE);
            price_old.setText(ConstantValues.CURRENCY_SYMBOL + productDetails.getProductsPrice());
            productBasePrice = Utilities.parseArabicDouble(productDetails.getDiscountPrice());

        }
        else {
            productDetails.setIsSaleProduct("0");

            price_old.setVisibility(View.GONE);
            product_tag_discount.setVisibility(View.GONE);
            productBasePrice = Utilities.parseArabicDouble(productDetails.getProductsPrice());
        }



        // Check if the Product is Out of Stock
        if (productDetails.getProductsQuantity() < 1) {
            product_stock.setText(getString(R.string.outOfStock));
            productCartBtn.setText(getString(R.string.outOfStock));
            product_stock.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentRed));
            productCartBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_corners_button_red));

        }
        else {
            product_stock.setText(getString(R.string.in_stock));
            productCartBtn.setText(getString(R.string.addToCart));
            product_stock.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccentBlue));
            productCartBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_corners_button_accent));
        }



        // Check if the Product is Newly Added with the help of static method of Helper class
        if (Utilities.checkNewProduct(productDetails.getProductsDateAdded())) {
            product_tag_new.setVisibility(View.VISIBLE);
        }
        else {
            product_tag_new.setVisibility(View.GONE);
        }
    
    
        String description = productDetails.getProductsDescription();
        String styleSheet = "<style> " +
                                "body{background:#ffffff; margin:0; padding:0} " +
                                "p{color:#757575;} " +
                                "img{display:inline; height:auto; max-width:100%;}" +
                            "</style>";
        //Log.w("Prp DSC php $ =" , description );
    
        product_description_webView.setHorizontalScrollBarEnabled(false);
        product_description_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
        


        // Set Product's Prices
        attributesPrice = 0;


        if (attributesList.size() > 0) {
            product_attributes.setVisibility(View.VISIBLE);
            
            for (int i=0;  i<attributesList.size();  i++) {
        
                CartProductAttributes productAttribute = new CartProductAttributes();
        
                // Get Name and First Value of current Attribute
                Option option = attributesList.get(i).getOption();
                Value value = attributesList.get(i).getValues().get(0);
        
        
                // Add the Attribute's Value Price to the attributePrices
                String attrPrice = value.getPricePrefix() + value.getPrice();
                attributesPrice += Utilities.parseArabicDouble(attrPrice);
        
        
                // Add Value to new List
                List<Value> valuesList = new ArrayList<>();
                valuesList.add(value);
        
        
                // Set the Name and Value of Attribute
                productAttribute.setOption(option);
                productAttribute.setValues(valuesList);
        
        
                // Add current Attribute to selectedAttributesList
                selectedAttributesList.add(i, productAttribute);
            }
            
    
            // Initialize the ProductAttributesAdapter for RecyclerView
            attributesAdapter = new ProductAttributesAdapter(getContext(), Product_Description.this, attributesList, selectedAttributesList);
    
            // Set the Adapter and LayoutManager to the RecyclerView
            attribute_recycler.setAdapter(attributesAdapter);
            attribute_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            attributesAdapter.notifyDataSetChanged();
            
        }
        else {
            product_attributes.setVisibility(View.GONE);
        }
    
        
        productFinalPrice = productBasePrice + attributesPrice;
        price_new.setText(ConstantValues.CURRENCY_SYMBOL + new DecimalFormat("#0.00").format(productFinalPrice));

        // Check if the User has Liked the Product
        if (productDetails.getIsLiked().equalsIgnoreCase("1")) {
            product_like_btn.setChecked(true);
        } else {
            product_like_btn.setChecked(false);
        }


        // Handle Click event of product_share_btn Button
        product_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Share Product with the help of static method of Helper class
                // New For The Error
                try {
                    Utilities.shareProduct
                            (
                                    getContext(),
                                    productDetails.getProductsName(),
                                    sliderImageView,
                                    productDetails.getProductsUrl()
                            );
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error in your share App's", Toast.LENGTH_SHORT).show();
                }
                // New For The Error
            }
        });


        // Handle Click event of product_like_btn Button
        product_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if the User is Authenticated
                if (ConstantValues.IS_USER_LOGGED_IN) {

                    // Check if the User has Checked the Like Button
                    if(product_like_btn.isChecked()) {
                        productDetails.setIsLiked("1");
                        product_like_btn.setChecked(true);

                        // Request the Server to Like the Product for the User
                        LikeProduct(productDetails.getProductsId(), customerID, getContext(), view);

                    } else {
                        productDetails.setIsLiked("0");
                        product_like_btn.setChecked(false);

                        // Request the Server to Unlike the Product for the User
                        UnlikeProduct(productDetails.getProductsId(), customerID, getContext(), view);
                    }

                } else {
                    // Keep the Like Button Unchecked
                    product_like_btn.setChecked(false);

                    // Navigate to Login Activity
                    Intent i = new Intent(getContext(), Login.class);
                    getContext().startActivity(i);
                    ((MainActivity) getContext()).finish();
                    ((MainActivity) getContext()).overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
                }
            }
        });



        // Handle Click event of productCartBtn Button
        productCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (productDetails.getProductsQuantity() > 0) {
                    
                    CartProduct cartProduct = new CartProduct();
    
                    // Set Product's Price, Quantity and selected Attributes Info
                    productDetails.setCustomersBasketQuantity(1);
                    productDetails.setProductsPrice(new DecimalFormat("#0.00").format(productBasePrice));
                    productDetails.setAttributesPrice(new DecimalFormat("#0.00").format(attributesPrice));
                    productDetails.setProductsFinalPrice(new DecimalFormat("#0.00").format(productFinalPrice));
                    productDetails.setTotalPrice(new DecimalFormat("#0.00").format(productFinalPrice));
                    cartProduct.setCustomersBasketProduct(productDetails);
                    cartProduct.setCustomersBasketProductAttributes(selectedAttributesList);
    
    
                    // Add the Product to User's Cart with the help of static method of My_Cart class
                    My_Cart.AddCartItem
                            (
                                    cartProduct
                            );
    
    
                    // Recreate the OptionsMenu
                    ((MainActivity) getContext()).invalidateOptionsMenu();
    
                    Snackbar.make(view, getContext().getString(R.string.item_added_to_cart), Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        //New
        try {
            MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(getContext());
            String aed_symbol = myAppPrefsManager.getAedCurrencySymbol();
            float aed_price = myAppPrefsManager.getAedCurrencyRate() * (float) productFinalPrice;//Utilities.parseArabicFloat(product.getDiscountPrice());
            aed_price = Utilities.round(aed_price, 2);
//            product_price_aed.setText(aed_symbol + aed_price);
            if(ConstantValues.CURRENCY_TYPE == "AED"){
                // if aed currency
                if (discount != null) {
                    // set discount
                    float aed_discount = myAppPrefsManager.getAedCurrencyRate() * Utilities.parseArabicFloat(discount);
                    aed_discount = Utilities.round(aed_price, 2);
                    product_tag_discount.setText(aed_symbol + aed_discount);

                    // Set old price
                    float aed_price_old = myAppPrefsManager.getAedCurrencyRate() * Utilities.parseArabicFloat(productDetails.getProductsPrice());
                    aed_price_old = Utilities.round(aed_price, 2);
                    price_old.setText(aed_symbol + aed_price_old);
                }
                price_new.setText(aed_symbol + aed_price);

            }else{
                // usd
                if (discount != null) {
                    product_tag_discount.setText(discount);
                    // Set Price info based on Discount
                    price_old.setText(ConstantValues.CURRENCY_SYMBOL + productDetails.getProductsPrice());
                }
                price_new.setText(ConstantValues.CURRENCY_SYMBOL + new DecimalFormat("#0.00").format(productFinalPrice));
            }
        }catch (Exception error){
            product_price_aed.setText("");
        }
        //New
    }



    //*********** Update Product's final Price based on selected Attributes ********//

    public void updateProductPrice() {
        
        attributesPrice = 0;

        // Get Attribute's Prices List from ProductAttributesAdapter
        String[] attributePrices = attributesAdapter.getAttributePrices();

        double attributesTotalPrice = 0.0;

        for (int i=0;  i<attributePrices.length;  i++) {
            // Get the Price of Attribute at given Position in attributePrices array
            double price = Utilities.parseArabicDouble(attributePrices[i]);

            attributesTotalPrice += price;
        }
    
        attributesPrice = attributesTotalPrice;


        // Calculate and Set Product's total Price
        productFinalPrice = productBasePrice + attributesPrice;
        price_new.setText(ConstantValues.CURRENCY_SYMBOL + new DecimalFormat("#0.00").format(productFinalPrice));

        //New
        try {
            if(ConstantValues.CURRENCY_TYPE == "AED"){
                MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(getContext());
                String aed_symbol = myAppPrefsManager.getAedCurrencySymbol();
                float aed_price = myAppPrefsManager.getAedCurrencyRate() * (float) productFinalPrice;//Utilities.parseArabicFloat(product.getDiscountPrice());
                aed_price = Utilities.round(aed_price, 2);
                // if aed currency
                price_new.setText(aed_symbol + aed_price);

            }else{
                // usd
                price_new.setText(ConstantValues.CURRENCY_SYMBOL + new DecimalFormat("#0.00").format(productFinalPrice));
            }
        }catch (Exception error){
            product_price_aed.setText("");
        }
        //New
        
    }



    //*********** Setup the ImageSlider with the given List of Product Images ********//

    private void ImageSlider(String itemThumbnail, List<Image> itemImages) {

        // to delete spaces in image link
        itemThumbnail = itemThumbnail.replace(" " , "%20");
        // Initialize new HashMap<ImageName, ImagePath>
        final HashMap<String, String> slider_covers = new HashMap<>();
        // Initialize new Array for Image's URL
        final String[] images = new String[itemImages.size()];


        if (itemImages.size() > 0) {
            for (int i=0;  i< itemImages.size();  i++) {
                // Get Image's URL at given Position from itemImages List
                images[i] = itemImages.get(i).getImage().replace(" " , "%20"); // to delete spaces in image link
            }
        }


        // Put Image's Name and URL to the HashMap slider_covers
        if (itemThumbnail.equalsIgnoreCase("")) {
            slider_covers.put("a", ""+R.drawable.placeholder);

        } else if (images.length == 0) {
            slider_covers.put("a", ConstantValues.ECOMMERCE_URL+itemThumbnail);


        } else {
            slider_covers.put("a", ConstantValues.ECOMMERCE_URL+itemThumbnail);


            for (int i=0;  i<images.length;  i++) {
                slider_covers.put("b"+i, ConstantValues.ECOMMERCE_URL+images[i]);
                //Log.w("ssxxzz" ,  ConstantValues.ECOMMERCE_URL+images[i]);
            }
        }


        for(String name : slider_covers.keySet()) {

            // Initialize DefaultSliderView
            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext()) {
                @Override
                public View getView() {
                    View v = LayoutInflater.from(getContext()).inflate(com.daimajia.slider.library.R.layout.render_type_default,null);

                    // Get daimajia_slider_image ImageView of DefaultSliderView
                    sliderImageView = (ImageView)v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);

                    // Set ScaleType of ImageView
                    sliderImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    bindEventAndShow(v, sliderImageView);

                    return v;
                }
            };

            // Set Attributes(Name, Placeholder, Image, Type etc) to DefaultSliderView
            defaultSliderView
                    .description(name)
                    .empty(R.drawable.placeholder)
                    .image(slider_covers.get(name))
                    .setScaleType(DefaultSliderView.ScaleType.CenterInside);

            // Add DefaultSliderView to the SliderLayout
            sliderLayout.addSlider(defaultSliderView);
        }

        // Set PresetTransformer type of the SliderLayout
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);


        // Check if the size of Images in the Slider is less than 2
        if (slider_covers.size() < 2) {
            // Disable PagerTransformer
            sliderLayout.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });

            // Hide Slider PagerIndicator
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        }
        else {
            // Set custom PagerIndicator to the SliderLayout
            sliderLayout.setCustomIndicator(pagerIndicator);
            // Make PagerIndicator Visible
            sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
        }
    }



    //*********** Request Product Details from the Server based on productID ********//

    public void RequestProductDetail(final int productID) {

        dialogLoader.showProgressDialog();


        GetAllProducts getAllProducts = new GetAllProducts();
        getAllProducts.setPageNumber(0);
        getAllProducts.setLanguageId(ConstantValues.LANGUAGE_ID);
        getAllProducts.setCustomersId(customerID);
        getAllProducts.setProductsId(String.valueOf(productID));

        //NEW
        getAllProducts.setToken(ConstantValues.TOKEN);


        Call<ProductData> call = APIClient.getInstance()
                .getAllProducts
                        (
                                getAllProducts
                        );

        call.enqueue(new Callback<ProductData>() {
            @Override
            public void onResponse(Call<ProductData> call, retrofit2.Response<ProductData> response) {

                dialogLoader.hideProgressDialog();

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        // Product's Details has been returned
                        setProductDetails(response.body().getProductData().get(0));

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
            public void onFailure(Call<ProductData> call, Throwable t) {
                Toast.makeText(getContext(), "Pr_Ds_NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }



    //*********** Request the Server to Like the Product based on productID and customerID ********//

    public static void LikeProduct(int productID, String customerID, final Context context, final View view) {

        Call<ProductData> call = APIClient.getInstance()
                .likeProduct
                        (
                                ConstantValues.TOKEN,
                                productID,
                                customerID
                        );

        call.enqueue(new Callback<ProductData>() {
            @Override
            public void onResponse(Call<ProductData> call, retrofit2.Response<ProductData> response) {
                // Check if the Response is successful
                if (response.isSuccessful()) {

                    // Check the Success status
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        // Product has been Liked. Show the message to User
                        Snackbar.make(view, context.getString(R.string.added_to_favourites), Snackbar.LENGTH_SHORT).show();

                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        Snackbar.make(view, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
    
                    }
                    else {
                        // Unable to get Success status
                        Snackbar.make(view, context.getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductData> call, Throwable t) {
                Toast.makeText(context, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }



    //*********** Request the Server to Unlike the Product based on productID and customerID ********//

    public static void UnlikeProduct(int productID, String customerID, final Context context, final View view) {

        Call<ProductData> call = APIClient.getInstance()
                .unlikeProduct
                        (
                                ConstantValues.TOKEN,
                                productID,
                                customerID
                        );

        call.enqueue(new Callback<ProductData>() {
            @Override
            public void onResponse(Call<ProductData> call, retrofit2.Response<ProductData> response) {
                // Check if the Response is successful
                if (response.isSuccessful()) {

                    // Check the Success status
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {

                        // Product has been Disliked. Show the message to User
                        Snackbar.make(view, context.getString(R.string.removed_from_favourites), Snackbar.LENGTH_SHORT).show();

                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                        Snackbar.make(view, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
    
                    }
                    else {
                        // Unable to get Success status
                        Snackbar.make(view, context.getString(R.string.unexpected_response), Snackbar.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductData> call, Throwable t) {
                Toast.makeText(context, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

}


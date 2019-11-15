package com.oshop.hm.adapters;


import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.oshop.hm.R;

import java.util.ArrayList;
import java.util.List;

import com.oshop.hm.app.MyAppPrefsManager;
import com.oshop.hm.constant.ConstantValues;
import com.oshop.hm.models.cart_model.CartProduct;
import com.oshop.hm.models.cart_model.CartProductAttributes;
import com.oshop.hm.models.product_model.Value;
import com.oshop.hm.utils.Utilities;


/**
 * CheckoutItemsAdapter is the adapter class of RecyclerView holding List of Products to be Ordered in Checkout
 **/

public class CheckoutItemsAdapter extends RecyclerView.Adapter<CheckoutItemsAdapter.MyViewHolder> {

    private Context context;
    private List<CartProduct> checkoutItemsList;
    
    private ProductAttributeValuesAdapter attributesAdapter;


    public CheckoutItemsAdapter(Context context, List<CartProduct> checkoutItemsList) {
        this.context = context;
        this.checkoutItemsList = checkoutItemsList;
    }



    //********** Called to Inflate a Layout from XML and then return the Holder *********//

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_checkout_items, parent, false);

        return new MyViewHolder(itemView);
    }



    //********** Called by RecyclerView to display the Data at the specified Position *********//

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // Get the data model based on Position
        final CartProduct cartProduct = checkoutItemsList.get(position);

        // Set Product Image on ImageView with Glide Library
        Glide.with(context).load(ConstantValues.ECOMMERCE_URL+ cartProduct.getCustomersBasketProduct().getProductsImage()).into(holder.checkout_item_cover);

        holder.checkout_item_title.setText(cartProduct.getCustomersBasketProduct().getProductsName());
        holder.checkout_item_quantity.setText(String.valueOf(cartProduct.getCustomersBasketProduct().getCustomersBasketQuantity()));

        String productPrice = cartProduct.getCustomersBasketProduct().getProductsPrice();
        holder.checkout_item_price.setText(ConstantValues.CURRENCY_SYMBOL + productPrice );
        //New
        try {
            MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(context);
            String aed_symbol = myAppPrefsManager.getAedCurrencySymbol();
            float aed_price = myAppPrefsManager.getAedCurrencyRate() * Utilities.parseArabicFloat(productPrice);
            aed_price = Utilities.round(aed_price, 2);
            holder.checkout_item_price_aed.setText(aed_symbol + aed_price);
        }catch (Exception error){
            holder.checkout_item_price_aed.setText("");
        }
        //New

        String finalPrice = cartProduct.getCustomersBasketProduct().getTotalPrice();
        holder.checkout_item_price_final.setText(ConstantValues.CURRENCY_SYMBOL + finalPrice);
        //New
        try {
            MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(context);
            String aed_symbol = myAppPrefsManager.getAedCurrencySymbol();
            float aed_price = myAppPrefsManager.getAedCurrencyRate() * Utilities.parseArabicFloat(finalPrice);
            aed_price = Utilities.round(aed_price, 2);
            holder.checkout_item_price_final_aed.setText(aed_symbol + aed_price);
        }catch (Exception error){
            holder.checkout_item_price_final_aed.setText("");
        }
        //New

        holder.checkout_item_category.setText(cartProduct.getCustomersBasketProduct().getCategoriesName());
    
    
        List<Value> selectedAttributeValues= new ArrayList<>();
        List<CartProductAttributes> productAttributes= new ArrayList<>();
    
        productAttributes = cartProduct.getCustomersBasketProductAttributes();
    
        for (int i=0;  i<productAttributes.size();  i++) {
            selectedAttributeValues.add(productAttributes.get(i).getValues().get(0));
        }
    
        // Initialize the ProductAttributeValuesAdapter for RecyclerView
        attributesAdapter = new ProductAttributeValuesAdapter(context, selectedAttributeValues);
    
        // Set the Adapter and LayoutManager to the RecyclerView
        holder.attributes_recycler.setAdapter(attributesAdapter);
        holder.attributes_recycler.setLayoutManager(new LinearLayoutManager(context));
    
        attributesAdapter.notifyDataSetChanged();
        

    }



    //********** Returns the total number of items in the data set *********//

    @Override
    public int getItemCount() {
        return checkoutItemsList.size();
    }



    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/

    public static class MyViewHolder extends RecyclerView.ViewHolder {
    
        private ImageView checkout_item_cover;
        private RecyclerView attributes_recycler;
        private TextView checkout_item_title, checkout_item_quantity,
                checkout_item_price,checkout_item_price_aed,
                checkout_item_price_final,checkout_item_price_final_aed,
                checkout_item_category;


        public MyViewHolder(final View itemView) {
            super(itemView);

            checkout_item_cover = (ImageView) itemView.findViewById(R.id.checkout_item_cover);
            checkout_item_title = (TextView) itemView.findViewById(R.id.checkout_item_title);
            checkout_item_quantity = (TextView) itemView.findViewById(R.id.checkout_item_quantity);
            checkout_item_price = (TextView) itemView.findViewById(R.id.checkout_item_price);
            checkout_item_price_aed = (TextView) itemView.findViewById(R.id.checkout_item_price_aed);
            checkout_item_price_final = (TextView) itemView.findViewById(R.id.checkout_item_price_final);
            checkout_item_price_final_aed = (TextView) itemView.findViewById(R.id.checkout_item_price_final_aed);
            checkout_item_category = (TextView) itemView.findViewById(R.id.checkout_item_category);
    
            attributes_recycler = (RecyclerView) itemView.findViewById(R.id.order_item_attributes_recycler);
        }
    }

}


package com.example.shipmenttracking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.ViewHolder>{
    private ArrayList<ShipmentTracking> mItemsData;
    private Context mContext;
    private int lastPosition = -1;

    PackagesAdapter(Context context, ArrayList<ShipmentTracking> itemsData){
        this.mItemsData = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PackagesAdapter.ViewHolder holder, int position) {
        ShipmentTracking currentItem = mItemsData.get(position);
        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mItemsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView queriedCountry;
        private TextView queriedLocality;
        private TextView queriedPostcode;
        private TextView queriedStateOrProvince;
        private TextView queriedStreetName;
        private TextView queriedStreetNumber;
        private TextView queriedStreetType;
        private TextView queriedCarrier;
        private TextView queriedTrackingCode;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            queriedCountry = itemView.findViewById(R.id.queriedCountryTextView);
            queriedLocality = itemView.findViewById(R.id.queriedLocalityTextView);
            queriedPostcode = itemView.findViewById(R.id.queriedPostcodeTextView);
            queriedStateOrProvince = itemView.findViewById(R.id.queriedStateOrProvinceTextView);
            queriedStreetName = itemView.findViewById(R.id.queriedStreetNameTextView);
            queriedStreetNumber = itemView.findViewById(R.id.queriedStreetNumberTextView);
            queriedStreetType = itemView.findViewById(R.id.queriedStreetTypeTextView);
            queriedCarrier = itemView.findViewById(R.id.queriedCarrierTextView);
            queriedTrackingCode = itemView.findViewById(R.id.queriedTrackingCodeTextView);
        }

        public void bindTo(ShipmentTracking currentItem) {
            queriedCountry.setText(currentItem.getAddressTo().get("country"));
            queriedLocality.setText(currentItem.getAddressTo().get("locality"));
            queriedPostcode.setText(currentItem.getAddressTo().get("postcode"));
            queriedStateOrProvince.setText(currentItem.getAddressTo().get("stateOrProvince"));
            queriedStreetName.setText(currentItem.getAddressTo().get("streetName"));
            queriedStreetNumber.setText(currentItem.getAddressTo().get("streetNr"));
            queriedStreetType.setText(currentItem.getAddressTo().get("streetType"));
            queriedCarrier.setText(currentItem.getCarrier());
            queriedTrackingCode.setText(currentItem.getTrackingCode());
        }
    }
}

package com.example.beamagayver.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beamagayver.R;
import com.example.beamagayver.pojo.LocationModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddLocationsRecAdapter extends RecyclerView.Adapter<AddLocationsRecAdapter.viewHolder> {

    private static final String TAG = "AddLocationsRecAdapter";
    private int selectedPosition = -1;
    private Context context;
    private ArrayList<LocationModel> data;

    public AddLocationsRecAdapter(Context context, ArrayList<LocationModel> locationModels) {
        this.context = context;
        this.data = locationModels;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_add_place_locations_rv_row_item, parent, false);

        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.countryTV.setText(data.get(position).getCountry());
        holder.cityTV.setText(data.get(position).getCity());
        holder.streetTV.setText(data.get(position).getStreet());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.frag_add_place_locations_rv_ri_place_ic)
        ImageView mIconImage;

        @BindView(R.id.frag_add_place_locations_rv_ri_delete_ic)
        ImageView mDeleteIcon;

        @BindView(R.id.frag_add_place_locations_rv_ri_popup_container)
        CardView detailsCardView;

        @BindView(R.id.frag_add_place_locations_rv_ri_popup_city_tv)
        TextView cityTV;

        @BindView(R.id.frag_add_place_locations_rv_ri_popup_country_tv)
        TextView countryTV;

        @BindView(R.id.frag_add_place_locations_rv_ri_popup_street_tv)
        TextView streetTV;

        viewHolder(@NonNull View itemView) {
            super(itemView);

            try {

                ButterKnife.bind(this, itemView);

                mDeleteIcon.setOnClickListener(e -> {
                    data.remove(getAdapterPosition());
                    notifyDataSetChanged();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
package com.natesanchez.knowyourgovernment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

  private List<Official> officialList;
  private MainActivity mainActivity;

  OfficialAdapter(List<Official> officialList, MainActivity mainActivity) {
    this.officialList = officialList;
    this.mainActivity = mainActivity;
  }

  @NonNull
  @Override
  public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View officialView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_list_entry, parent, false);
    officialView.setOnClickListener(mainActivity);
    return new OfficialViewHolder(officialView);
  }

  @Override
  public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {
    Official official = officialList.get(position);
    holder.name.setText(official.getName());
    holder.seat.setText(official.getSeat());
    holder.party.setText(String.format("(%s)", official.getParty()));
  }

  @Override
  public int getItemCount() {
    return officialList.size();
  }
}

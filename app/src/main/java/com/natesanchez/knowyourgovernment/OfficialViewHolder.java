package com.natesanchez.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {

  TextView name;
  TextView seat;
  TextView party;

  public OfficialViewHolder(@NonNull View itemView) {
    super(itemView);

    name = itemView.findViewById(R.id.entry_name);
    seat = itemView.findViewById(R.id.entry_seat);
    party = itemView.findViewById(R.id.entry_party);
  }
}

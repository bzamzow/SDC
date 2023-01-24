package edu.wgu.zamzow.medalert.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.objects.Schedule;

public class SchedAdapter extends RecyclerView.Adapter<SchedAdapter.ViewHolder> {

    private final ArrayList<Schedule> schedules;
    private final LayoutInflater inflater;
    public ItemClickListener clickListener;

    public SchedAdapter(Context context, ArrayList<Schedule> schedules) {
        inflater = LayoutInflater.from(context);
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sched_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTxtMedName().setText(schedules.get(position).getName());
        holder.getTxtTime().setText(schedules.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtMedName, txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMedName = itemView.findViewById(R.id.txtMedName);
            txtTime = itemView.findViewById(R.id.txtTime);
            itemView.setOnClickListener(this);
        }

        public TextView getTxtTime() {
            return txtTime;
        }

        public TextView getTxtMedName() {
            return txtMedName;
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

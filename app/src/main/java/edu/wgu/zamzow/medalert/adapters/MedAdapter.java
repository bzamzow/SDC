package edu.wgu.zamzow.medalert.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.objects.Med;

public class MedAdapter extends RecyclerView.Adapter<MedAdapter.ViewHolder> {

    private final ArrayList<Med> meds;
    private final LayoutInflater inflater;
    public ItemClickListener clickListener;

    public MedAdapter(Context context, ArrayList<Med> meds) {
        inflater = LayoutInflater.from(context);
        this.meds = meds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.med_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTxtMedName().setText(meds.get(position).getDrugName());
        holder.getTxtDosage().setText(meds.get(position).getStrength());
    }

    @Override
    public int getItemCount() {
        return meds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtMedName, txtDosage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMedName = itemView.findViewById(R.id.txtMedName);
            txtDosage = itemView.findViewById(R.id.txtDosage);
            itemView.setOnClickListener(this);
        }

        public TextView getTxtDosage() {
            return txtDosage;
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

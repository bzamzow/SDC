package edu.wgu.zamzow.medalert.ui.cabinet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.wgu.zamzow.medalert.databinding.FragmentCabinetBinding;

public class CabinetFragment extends Fragment {

    private FragmentCabinetBinding binding;
    private FloatingActionButton fabAddMed;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CabinetViewModel cabinetViewModel =
                new ViewModelProvider(this).get(CabinetViewModel.class);

        binding = FragmentCabinetBinding.inflate(inflater, container, false);

        fabAddMed = binding.fabAddMed;
        fabAddMed.setOnClickListener(view -> {
            Intent addMedActivity = new Intent(getActivity(), AddMedActivity.class);
            startActivity(addMedActivity);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
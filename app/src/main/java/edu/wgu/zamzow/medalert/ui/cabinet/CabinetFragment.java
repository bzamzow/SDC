package edu.wgu.zamzow.medalert.ui.cabinet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.wgu.zamzow.medalert.databinding.FragmentCabinetBinding;

public class CabinetFragment extends Fragment {

    private FragmentCabinetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CabinetViewModel cabinetViewModel =
                new ViewModelProvider(this).get(CabinetViewModel.class);

        binding = FragmentCabinetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        cabinetViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
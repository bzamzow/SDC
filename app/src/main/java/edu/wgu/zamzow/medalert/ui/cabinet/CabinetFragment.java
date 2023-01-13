package edu.wgu.zamzow.medalert.ui.cabinet;

import static edu.wgu.zamzow.medalert.utils.Vars.ADD_MED_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.adapters.MedAdapter;
import edu.wgu.zamzow.medalert.communicate.Meds;
import edu.wgu.zamzow.medalert.databinding.FragmentCabinetBinding;
import edu.wgu.zamzow.medalert.objects.Med;

public class CabinetFragment extends Fragment {

    private FragmentCabinetBinding binding;
    private ArrayList<Med> meds;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CabinetViewModel cabinetViewModel =
                new ViewModelProvider(this).get(CabinetViewModel.class);

        binding = FragmentCabinetBinding.inflate(inflater, container, false);

        FloatingActionButton fabAddMed = binding.fabAddMed;
        fabAddMed.setOnClickListener(view -> {
            Intent addMedActivity = new Intent(getActivity(), AddMedActivity.class);
            startActivityForResult(addMedActivity,ADD_MED_ACTIVITY);
        });

        recyclerView = binding.recyclerCabinet;

        SetupDrugs();
        return binding.getRoot();
    }

    public void SetupDrugs() {
        Runnable getBasicDrugs = () -> {
            meds = new ArrayList<>();
            try {
                Meds medsComm = new Meds(getActivity());
                meds = medsComm.getUserBasicDrugs();

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            requireActivity().runOnUiThread(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                MedAdapter medAdapter = new MedAdapter(getActivity(),meds);
                medAdapter.setClickListener(new MedAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                recyclerView.setAdapter(medAdapter);
            });
        };
        Thread getDrugsThread = new Thread(getBasicDrugs);
        getDrugsThread.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MED_ACTIVITY) {
            SetupDrugs();
        }
    }
}
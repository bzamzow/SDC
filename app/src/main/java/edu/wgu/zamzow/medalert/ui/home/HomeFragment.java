package edu.wgu.zamzow.medalert.ui.home;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import edu.wgu.zamzow.medalert.adapters.MedAdapter;
import edu.wgu.zamzow.medalert.communicate.Meds;
import edu.wgu.zamzow.medalert.databinding.FragmentHomeBinding;
import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.ui.cabinet.MedViewerActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<Med> meds;
    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerSchedule;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityResultLauncher<String[]> notificationPermissionRequest =
                    registerForActivityResult(new ActivityResultContracts
                                    .RequestMultiplePermissions(), result -> {
                                    Boolean postNotification = result.getOrDefault(
                                            Manifest.permission.POST_NOTIFICATIONS, false);
                            }
                    );
            notificationPermissionRequest.launch(new String[] {Manifest.permission.POST_NOTIFICATIONS});
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void SetupDrugs() {
        Runnable getBasicDrugs = () -> {
            meds = new ArrayList<>();
            try {
                Meds medsComm = new Meds(getActivity());
                meds = medsComm.getUserDrugs();

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            requireActivity().runOnUiThread(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                MedAdapter medAdapter = new MedAdapter(getActivity(),meds);
                medAdapter.setClickListener((view, position) -> {
                    Med selectedMed = meds.get(position);
                    Intent medViewerActivity = new Intent(getActivity(), MedViewerActivity.class);
                    medViewerActivity.putExtra("selectedMed", selectedMed);
                    startActivity(medViewerActivity);
                });
                recyclerView.setAdapter(medAdapter);
            });
        };
        Thread getDrugsThread = new Thread(getBasicDrugs);
        getDrugsThread.start();
    }
}
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
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import edu.wgu.zamzow.medalert.adapters.MedAdapter;
import edu.wgu.zamzow.medalert.adapters.SchedAdapter;
import edu.wgu.zamzow.medalert.communicate.Meds;
import edu.wgu.zamzow.medalert.databinding.FragmentHomeBinding;
import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.objects.Schedule;
import edu.wgu.zamzow.medalert.ui.cabinet.MedViewerActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<Med> meds;
    private RecyclerView recyclerView;
    private ArrayList<Schedule> schedules;
    private Schedule next;
    private TextView txtName, txtTime;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerSchedule;
        txtName = binding.txtDrug;
        txtTime = binding.txtTime;

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

        SetupSchedules();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void SetupSchedules() {
        Runnable getBasicDrugs = () -> {
            meds = new ArrayList<>();
            try {
                Meds medsComm = new Meds(getActivity());
                meds = medsComm.getUserDrugs();
                CreateSchedules();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            requireActivity().runOnUiThread(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                SchedAdapter schedAdapter = new SchedAdapter(getActivity(),schedules);
                recyclerView.setAdapter(schedAdapter);

                next = schedules.get(0);
                for (Schedule schedule : schedules) {
                    Time currentNext = Time.valueOf(next.getTime());
                    if (schedule.getTime() != null) {
                        Time checkingTime = Time.valueOf(schedule.getTime());
                        System.out.println(schedule.getTime());

                        if (checkingTime.before(currentNext)) {
                            next = schedule;
                        }
                    }
                }
                txtName.setText(next.getName());
                txtTime.setText(next.getTime());
            });
        };
        Thread getDrugsThread = new Thread(getBasicDrugs);
        getDrugsThread.start();
    }

    private void CreateSchedules() {
        schedules = new ArrayList<>();
        meds.forEach((med -> {
            Date startDate = med.getStartDate();
            Time startTime = med.getStartTime();
            int freqType = med.getFreqType();
            int freqNo = med.getFreqNo();
            Time currentTime = new Time(System.currentTimeMillis());
            Calendar rightTime = Calendar.getInstance();
            Calendar trueStart = Calendar.getInstance();
            trueStart.set(Calendar.HOUR_OF_DAY, startTime.getHours());
            trueStart.set(Calendar.MINUTE,startTime.getMinutes());

            switch (freqType) {
                case 0:
                    if (freqNo > 0) {
                        int count = 24 / freqNo;
                        if (currentTime.before(startTime)) {
                            long current = rightTime.getTimeInMillis();
                            long start = startDate.getTime();
                            if ((TimeUnit.MILLISECONDS.toHours(Math.abs(start - current))) > freqNo) {
                                Schedule hourlySched = new Schedule();
                                hourlySched.setName(med.getDrugName());
                                trueStart.add(Calendar.HOUR, -freqNo);
                                Time schedTime = new Time(trueStart.getTimeInMillis());
                                hourlySched.setTime(schedTime.toString());
                                schedules.add(hourlySched);
                                trueStart = Calendar.getInstance();
                                trueStart.set(Calendar.HOUR_OF_DAY, startTime.getHours());
                                trueStart.set(Calendar.MINUTE,startTime.getMinutes());
                                for (int i = 1; i < count; i++) {
                                    Calendar midnight = Calendar.getInstance();
                                    midnight.add(Calendar.DAY_OF_MONTH,1);
                                    midnight.set(Calendar.HOUR_OF_DAY,0);
                                    trueStart.add(Calendar.HOUR, i * freqNo);
                                    if (trueStart.before(midnight)) {
                                        Time newSchedTime = new Time(trueStart.getTimeInMillis());
                                        Schedule schedule = new Schedule();
                                        schedule.setName(med.getDrugName());
                                        schedule.setTime(newSchedTime.toString());
                                        schedules.add(schedule);
                                    }
                                }
                            }
                        } else {
                            for (int i = 1; i < count; i++) {
                                Calendar midnight = Calendar.getInstance();
                                midnight.add(Calendar.DAY_OF_MONTH,1);
                                midnight.set(Calendar.HOUR_OF_DAY,0);
                                trueStart.add(Calendar.HOUR, i * freqNo);
                                if (trueStart.before(midnight)) {
                                    Time schedTime = new Time(trueStart.getTimeInMillis());
                                    Schedule schedule = new Schedule();
                                    schedule.setName(med.getDrugName());
                                    schedule.setTime(schedTime.toString());
                                    schedules.add(schedule);
                                }
                            }
                        }
                    }
                case 1:
                    Schedule dailySchedule = new Schedule();
                    dailySchedule.setName(med.getDrugName());
                    dailySchedule.setTime(med.getStartTime().toString());
                    schedules.add(dailySchedule);
                case 2:
                    Schedule weeklySchedule = new Schedule();
                    if (startDate.equals(new Date(rightTime.getTimeInMillis()))) {
                        weeklySchedule.setName(med.getDrugName());
                        weeklySchedule.setTime(med.getStartTime().toString());
                    } else {
                        long current = rightTime.getTimeInMillis();
                        long start = startDate.getTime();
                        if ((TimeUnit.MILLISECONDS.toDays(Math.abs(current - start))) == 7) {
                            weeklySchedule.setName(med.getDrugName());
                            weeklySchedule.setTime(med.getStartTime().toString());
                        }
                    }
                    schedules.add(weeklySchedule);
                case 3:
                    if (startDate.getDay() == rightTime.get(Calendar.DAY_OF_MONTH)) {
                        Schedule monthlySchedule = new Schedule();
                        monthlySchedule.setTime(med.getStartTime().toString());
                        schedules.add(monthlySchedule);
                    }
            }
        }));
    }
}
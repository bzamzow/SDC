package edu.wgu.zamzow.medalert.ui.reports;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.adapters.MedAdapter;
import edu.wgu.zamzow.medalert.communicate.Meds;
import edu.wgu.zamzow.medalert.communicate.Reports;
import edu.wgu.zamzow.medalert.databinding.FragmentReportsBinding;
import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.objects.Report;
import edu.wgu.zamzow.medalert.ui.cabinet.MedViewerActivity;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding binding;
    private TextView txtTaken, txtMissed, txtPercent, txtNumMeds, txtMedNameLabel;
    private Spinner spinnerReportType,spinnerMed;
    private RunReport runReport = new RunReport();
    private ArrayList<Med> meds;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportsViewModel reportsViewModel =
                new ViewModelProvider(this).get(ReportsViewModel.class);

        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinnerReportType = binding.spinnerReportType;
        spinnerMed = binding.spinnerMed;
        txtTaken = binding.txtTaken;
        txtMissed = binding.txtMissed;
        txtPercent = binding.txtPercent;
        txtNumMeds = binding.txtNumMeds;
        txtMedNameLabel = binding.medNameLabel;

        SetupDrugs();
        ArrayAdapter<CharSequence> reportTypeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.report_type, android.R.layout.simple_spinner_item);
        reportTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportType.setAdapter(reportTypeAdapter);

        spinnerReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                runReport = new RunReport();
                if (i == 0) {
                    spinnerMed.setVisibility(View.INVISIBLE);
                    txtMedNameLabel.setVisibility(View.INVISIBLE);
                } else {
                    spinnerMed.setVisibility(View.VISIBLE);
                    txtMedNameLabel.setVisibility(View.VISIBLE);
                }
                try {
                    runReport.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerMed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                runReport = new RunReport();

                try {
                    runReport.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class RunReport extends AsyncTask<Void, Void, Boolean> {

        private int reportType;
        private Report report;
        private boolean didGet = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reportType = spinnerReportType.getSelectedItemPosition();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Reports reports = new Reports(getContext());
            if (reportType == 0) {
                try {
                    report = reports.getAll();
                    didGet = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    report = reports.getAllMed((Med) spinnerMed.getSelectedItem());
                    didGet = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            return didGet;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if (didGet) {
                txtTaken.setText(String.valueOf(report.getNumTaken()));
                txtMissed.setText(String.valueOf(report.getNumMissed()));
                txtNumMeds.setText(String.valueOf(report.getNumMeds()));
                double basePerc = ((double)report.getNumTaken() / (double) report.getNumMeds());
                DecimalFormat df = new DecimalFormat("##.##%");
                txtPercent.setText(df.format(basePerc));
            }

            super.onPostExecute(aBoolean);
        }
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
                ArrayAdapter<Med> medsListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, meds);
                medsListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMed.setAdapter(medsListAdapter);
            });
        };
        Thread getDrugsThread = new Thread(getBasicDrugs);
        getDrugsThread.start();
    }
}
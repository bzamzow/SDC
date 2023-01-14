package edu.wgu.zamzow.medalert.ui.cabinet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.objects.Med;

public class MedViewerActivity extends AppCompatActivity {

    private TextView txtDrugName, txtStrength, txtForm, txtHrs;
    private EditText editFreqNo, editStartDate, editStartTime;
    private Spinner spinnerFreqType;
    private Med med;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_viewer);

        med = (Med) getIntent().getSerializableExtra("selectedMed");

        txtDrugName = findViewById(R.id.txtDrugName);
        txtForm = findViewById(R.id.txtForm);
        txtStrength = findViewById(R.id.txtStrength);
        editFreqNo = findViewById(R.id.editFreqNo);
        editStartDate = findViewById(R.id.editStartDate);
        editStartTime = findViewById(R.id.editStartTime);
        spinnerFreqType = findViewById(R.id.spinnerFreqType);
        txtHrs = findViewById(R.id.txtHrs);


        txtDrugName.setText(med.getDrugName());
        txtStrength.setText(med.getStrength());
        txtForm.setText(med.getForm());
        editStartTime.setText(med.getStartTime().toString());
        editStartDate.setText(med.getStartDate().toString());
        editFreqNo.setText(String.valueOf(med.getFreqNo()));

        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(this, R.array.freq_type, android.R.layout.simple_spinner_item);
        freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFreqType.setAdapter(freqAdapter);

        spinnerFreqType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    txtHrs.setVisibility(View.VISIBLE);
                    editFreqNo.setVisibility(View.VISIBLE);
                } else {
                    txtHrs.setVisibility(View.GONE);
                    editFreqNo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
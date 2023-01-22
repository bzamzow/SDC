package edu.wgu.zamzow.medalert.ui.cabinet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.communicate.Meds;
import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.utils.DateHelper;

public class MedViewerActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private TextView txtDrugName, txtStrength, txtForm, txtHrs;
    private EditText editFreqNo, editStartDate, editStartTime;
    private Spinner spinnerFreqType;
    private Med med;
    private final Calendar myCalendar= Calendar.getInstance();
    private FloatingActionButton fabSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_viewer);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            setdate();
        };

        med = (Med) getIntent().getSerializableExtra("selectedMed");

        txtDrugName = findViewById(R.id.txtDrugName);
        txtForm = findViewById(R.id.txtForm);
        txtStrength = findViewById(R.id.txtStrength);
        editFreqNo = findViewById(R.id.editFreqNo);
        editStartDate = findViewById(R.id.editStartDate);
        editStartTime = findViewById(R.id.editStartTime);
        spinnerFreqType = findViewById(R.id.spinnerFreqType);
        txtHrs = findViewById(R.id.txtHrs);
        fabSave = findViewById(R.id.fabSave);


        txtDrugName.setText(med.getDrugName());
        txtStrength.setText(med.getStrength());
        txtForm.setText(med.getForm());
        editStartTime.setText(med.getStartTime().toString());
        if (DateHelper.showDate(med.getStartDate()).equals("30 Nov 0002")) {
            editStartDate.setText("");
        } else {
            editStartDate.setText(med.getStartDate().toString());
        }

        editStartDate.setOnFocusChangeListener((view, b) -> {
            if (view.hasFocus()) {
                new DatePickerDialog(MedViewerActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editStartTime.setOnFocusChangeListener((view, b) -> {
            if (view.hasFocus()) {
                new TimePickerDialog(MedViewerActivity.this, this, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });
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

        fabSave.setOnClickListener(v -> {
            med.setFreqType(spinnerFreqType.getSelectedItemPosition());
            med.setFreqNo(Integer.parseInt(editFreqNo.getText().toString()));
            med.setStartDate(DateHelper.getDate(editStartDate.getText().toString()));
            Time time = new Time(myCalendar.getTimeInMillis());
            med.setStartTime(time);
            Meds meds = new Meds(this);
            meds.UpdateDrug(med);
        });
    }

    public void setdate() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hour = String.valueOf(hourOfDay);
        String min;
        if (minute < 10) {
            min = "0" + minute;
        } else {
            min = String.valueOf(minute);
        }
        myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);
        editStartTime.setText(hour + ":" + min);
    }
}
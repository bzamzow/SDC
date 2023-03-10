package edu.wgu.zamzow.medalert.ui.cabinet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import edu.wgu.zamzow.medalert.MainActivity;
import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.communicate.Meds;
import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.utils.DateHelper;
import edu.wgu.zamzow.medalert.utils.NotificationReceiver;
import edu.wgu.zamzow.medalert.utils.Vars;

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
            UpdateMed updateMed = new UpdateMed();
            try {
                updateMed.execute().get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
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

    private class UpdateMed extends AsyncTask<Void, Void, Boolean> {

        private boolean didUpdate;

        @Override
        protected Boolean doInBackground(Void... voids) {
            med.setFreqType(spinnerFreqType.getSelectedItemPosition());
            med.setFreqNo(Integer.parseInt(editFreqNo.getText().toString()));
            med.setStartDate(DateHelper.getDate(editStartDate.getText().toString()));
            int hour = Integer.parseInt(editStartTime.getText().toString().split(":")[0]);
            int min = Integer.parseInt(editStartTime.getText().toString().split(":")[1]);
            myCalendar.set(Calendar.HOUR_OF_DAY, hour);
            myCalendar.set(Calendar.MINUTE, min);
            Time time = new Time(myCalendar.getTimeInMillis());
            med.setStartTime(time);
            Meds meds = new Meds(getApplicationContext());
            try {
                didUpdate = meds.UpdateDrug(med);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return didUpdate;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (didUpdate) {
                setAlarm();
                SetNext();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to update the medicine, try again later", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setAlarm() {
        Intent notificationIntent = new Intent( getApplicationContext(), NotificationReceiver.class ) ;
        notificationIntent.putExtra(Vars.NOTIFICATION_ID , new Random().nextInt() ) ;
        notificationIntent.putExtra(Vars.NOTIFICATION , getNotification()) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast ( getApplicationContext(), new Random().nextInt(), notificationIntent , PendingIntent.FLAG_IMMUTABLE ) ;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), pendingIntent); ;
    }

    private void SetNext() {
        Intent notificationIntent = new Intent( getApplicationContext(), NotificationReceiver.class ) ;
        notificationIntent.putExtra(Vars.NOTIFICATION_ID , new Random().nextInt() ) ;
        notificationIntent.putExtra(Vars.NOTIFICATION , getNotification()) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast ( getApplicationContext(), new Random().nextInt(), notificationIntent , PendingIntent.FLAG_IMMUTABLE ) ;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE ) ;
        assert alarmManager != null;
        if (med.getFreqType() == 0) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getDelay(), AlarmManager.INTERVAL_HOUR * med.getFreqNo(), pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getDelay(), getIntveral(), pendingIntent);
        }
    }

    private long getIntveral() {
        final long DAY = AlarmManager.INTERVAL_DAY;
        switch (med.getFreqType()) {
            case 1:
                return DAY;
            case 2:
                return DAY * 7;
            case 3:
                return DAY * 30;
            default:
                return DAY;
        }
    }

    private long getDelay() {
        Calendar addedTime = myCalendar;
        switch (med.getFreqType()) {
            case 0:
                addedTime.add(Calendar.HOUR_OF_DAY,med.getFreqNo());
            case 1:
                addedTime.add(Calendar.DAY_OF_MONTH,1);
            case 2:
                addedTime.add(Calendar.WEEK_OF_YEAR, 1);
            case 3:
                addedTime.add(Calendar.MONTH, 1);
        }
        return addedTime.getTimeInMillis();
    }

    private Notification getNotification() {

        Intent openApp = new Intent(this, MainActivity.class);
        openApp.putExtra("selectedMed",med);
        openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);

        PendingIntent pendingOpenApp = PendingIntent.getActivity(this, uniqueInt, openApp, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder( this,
                "default" ) ;
        notification.setContentTitle( "Medication Alert" ) ;
        notification.setContentText("Take " + med.getDrugName() + " at " +
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" +
                Calendar.getInstance().get(Calendar.MINUTE));
        notification.setSmallIcon(android.R.drawable.ic_lock_idle_alarm ) ;
        notification.setContentIntent(pendingOpenApp);
        notification.setAutoCancel( true ) ;
        notification.setChannelId( Vars.Channel_ID ) ;
        return notification.build();
    }
}
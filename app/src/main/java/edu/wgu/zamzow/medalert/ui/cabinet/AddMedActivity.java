package edu.wgu.zamzow.medalert.ui.cabinet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.communicate.Meds;
import edu.wgu.zamzow.medalert.objects.Med;

public class AddMedActivity extends AppCompatActivity {

    private AutoCompleteTextView editMedName;
    private ArrayList<String> medList = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();
    private ArrayList<String> doseList = new ArrayList<>();
    private Thread getListThread, getTypeThread, getDoseThread, getDrugThread, saveThread;
    private ArrayAdapter<String> autoList, autoTypeList, autoDoseList;
    private Spinner spinnerType, spinnerDose;
    private Button btnSave;
    private Med med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);

        editMedName = findViewById(R.id.editDrugName);
        spinnerType = findViewById(R.id.spinnerType);
        spinnerDose = findViewById(R.id.spinnerDose);
        btnSave = findViewById(R.id.btnSave);

        editMedName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Runnable getListRun = () -> {
                    Meds meds = new Meds(getApplicationContext());
                    try {
                        medList = meds.getMedNames(editMedName.getText().toString());
                        runOnUiThread(() -> {
                            autoList = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, medList);
                            editMedName.setAdapter(autoList);
                        });
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                };
                if (editMedName.getText().length() > 2) {
                    getListThread = new Thread(getListRun);
                    getListThread.start();
                }
            }
        });
        editMedName.setOnItemClickListener((adapterView, view, i, l) -> {
            loadType();
        });
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadDose();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave.setOnClickListener(view -> getDrug());
    }

    private void loadType() {

        Runnable getTypeRun = () -> {
            Meds meds = new Meds(getApplicationContext());
            try {
                typeList = meds.getTypes(editMedName.getText().toString());
                runOnUiThread(() -> {
                    autoTypeList = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, typeList);
                    spinnerType.setAdapter(autoTypeList);
                    spinnerType.setVisibility(View.VISIBLE);
                    loadDose();
                });
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        };
        getTypeThread = new Thread(getTypeRun);
        getTypeThread.start();
    }

    private void loadDose() {

        Runnable getDoseRun = () -> {
            Meds meds = new Meds(getApplicationContext());
            try {
                doseList = meds.getDose(editMedName.getText().toString(), spinnerType.getSelectedItem().toString());
                runOnUiThread(() -> {
                    autoDoseList = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, doseList);
                    spinnerDose.setAdapter(autoDoseList);
                    spinnerDose.setVisibility(View.VISIBLE);
                });
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        };
        getDoseThread = new Thread(getDoseRun);
        getDoseThread.start();
    }

    private void getDrug() {
        med = new Med();
        Runnable getDrugRun = () -> {
            Meds meds = new Meds(getApplicationContext());
            try {
                med = meds.getExactDrug(editMedName.getText().toString(), spinnerType.getSelectedItem().toString(), spinnerDose.getSelectedItem().toString());
                saveDrug();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        };
        getDrugThread = new Thread(getDrugRun);
        getDrugThread.start();
    }

    private void saveDrug() {

        Runnable saveDrugRun = () -> {
            Meds meds = new Meds(getApplicationContext());
            meds.CreateDrug(med);
            runOnUiThread(() -> {
                finish();
            });
        };
        saveThread = new Thread(saveDrugRun);
        saveThread.start();
    }
}
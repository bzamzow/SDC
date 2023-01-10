package edu.wgu.zamzow.medalert.ui.cabinet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import edu.wgu.zamzow.medalert.R;
import edu.wgu.zamzow.medalert.communicate.Meds;

public class AddMedActivity extends AppCompatActivity {

    private AutoCompleteTextView editMedName;
    private ArrayList<String> medList = new ArrayList<>();
    private Thread getListThread;
    private ArrayAdapter<String> autoList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);

        editMedName = findViewById(R.id.editDrugName);

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                if (editMedName.getText().length() > 2) {
                    getListThread = new Thread(getListRun);
                    getListThread.start();
                }
            }
        });
        editMedName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
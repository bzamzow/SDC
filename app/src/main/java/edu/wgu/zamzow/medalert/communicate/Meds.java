package edu.wgu.zamzow.medalert.communicate;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.HttpsURLConnection;

import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.objects.User;
import edu.wgu.zamzow.medalert.utils.DateHelper;
import edu.wgu.zamzow.medalert.utils.Vars;

public class Meds extends ServerComm{

    private final Context context;
    private boolean didCreate;

    public Meds(Context context) {
        super(context);
        this.context = context;

    }

    public ArrayList<String> getMedNames(String medName) throws IOException, JSONException {
        String urlString = URL + PRODUCT + EQUALS + medName;
        ArrayList<String> medList = new ArrayList<>();

        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
        }
        if (!sb.toString().trim().equals("{\"response\":[]}")) {
            JSONObject obj = new JSONObject(sb.toString());
            JSONArray jsonArray = obj.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                medList.add(jsonObject.get("DrugName").toString());
            }
        }
        return medList;
    }

    public ArrayList<String> getTypes(String medName) throws IOException, JSONException {
        String urlString = URL + GET_TYPE + AND + DRUG + EQUALS + medName;
        ArrayList<String> typeList = new ArrayList<>();

        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
        }
        if (!sb.toString().trim().equals("{\"response\":[]}")) {
            JSONObject obj = new JSONObject(sb.toString());
            JSONArray jsonArray = obj.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                typeList.add(jsonObject.get("Form").toString());
            }
        }
        return typeList;
    }

    public ArrayList<String> getDose(String medName, String type) throws IOException, JSONException {
        String urlString = URL + GET_DOSE + AND + DRUG + EQUALS + medName + AND + FORM + EQUALS + type.replace(";","%3B");
        ArrayList<String> doseList = new ArrayList<>();

        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
        }
        if (!sb.toString().trim().equals("{\"response\":[]}")) {
            JSONObject obj = new JSONObject(sb.toString());
            JSONArray jsonArray = obj.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                doseList.add(jsonObject.get("Strength").toString());
            }
        }
        return doseList;
    }

    public Med getExactDrug(String medName, String type, String dose)
            throws IOException, JSONException {
        String urlString = URL + GET_EXACT_DRUG + AND + DRUG + EQUALS + medName + AND + FORM + EQUALS +
                type.replace(";","%3B") + AND + DOSE + EQUALS + dose.replace(";","%3B");
        System.out.println(urlString);
        Med med = new Med();

        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
        }
        if (!sb.toString().trim().equals("{\"response\":[]}")) {
            JSONObject obj = new JSONObject(sb.toString());
            JSONArray jsonArray = obj.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                med.setApplNo(jsonObject.getInt("ApplNo"));
                med.setProdNo(jsonObject.getInt("ProductNo"));
                med.setDrugName(jsonObject.getString("DrugName"));
                med.setForm(jsonObject.getString("Form"));
                med.setStrength(jsonObject.getString("Strength"));
            }
        }
        return med;
    }

    public ArrayList<Med> getUserBasicDrugs()
            throws IOException, JSONException {
        String urlString = URL + GET_USER_BASIC_DRUG + AND + UID + EQUALS +
                Vars.userID;
        System.out.println(urlString);
        ArrayList<Med> meds = new ArrayList<>();

        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
        }
        if (!sb.toString().trim().equals("{\"response\":[]}")) {
            JSONObject obj = new JSONObject(sb.toString());
            JSONArray jsonArray = obj.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {
                Med med = new Med();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                med.setApplNo(jsonObject.getInt("ApplNo"));
                med.setProdNo(jsonObject.getInt("ProductNo"));
                med.setDrugName(jsonObject.getString("DrugName"));
                med.setForm(jsonObject.getString("Form"));
                med.setStrength(jsonObject.getString("Strength"));
                meds.add(med);
            }
        }
        return meds;
    }

    public ArrayList<Med> getUserDrugs()
            throws IOException, JSONException {
        String urlString = URL + GET_USER_DRUG + AND + UID + EQUALS +
                Vars.userID;
        System.out.println(urlString);
        ArrayList<Med> meds = new ArrayList<>();

        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
        }
        if (!sb.toString().trim().equals("{\"response\":[]}")) {
            JSONObject obj = new JSONObject(sb.toString());
            JSONArray jsonArray = obj.getJSONArray("response");
            for (int i = 0; i < jsonArray.length(); i++) {
                Med med = new Med();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                med.setApplNo(jsonObject.getInt("ApplNo"));
                med.setProdNo(jsonObject.getInt("ProductNo"));
                med.setDrugName(jsonObject.getString("DrugName"));
                med.setForm(jsonObject.getString("Form"));
                med.setStrength(removeExcess(jsonObject.getString("Strength")));
                med.setFreqNo(jsonObject.getInt("freqNo"));
                med.setFreqType(jsonObject.getInt("freqType"));
                med.setStartDate(DateHelper.getDate(jsonObject.getString("startDate")));
                med.setStartTime(Time.valueOf(jsonObject.getString("startTime")));
                med.setId(jsonObject.getInt("id"));
                meds.add(med);
            }
        }
        return meds;
    }



    private String removeExcess(String string) {
        String[] parts = string.split("\\*");
        return parts[0];
    }

    public boolean CreateDrug(Med med) {
        didCreate = false;
        String createDrug = URL + CREATE_DRUG + AND + "ApplNo" + EQUALS + med.getApplNo() + AND
                + "ProductNo" + EQUALS + med.getProdNo() + AND + "userID" + EQUALS + Vars.userID;
        System.out.println(createDrug);
        StringRequest stringReq = new StringRequest(Request.Method.POST, createDrug, response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.toString().contains("successfully")) {
                    didCreate = true;
                } else {
                    didCreate = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                didCreate = false;
            }
        },
                error -> Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show());

        getInstance(context).addToRequestQueue(stringReq);
        return didCreate;
    }

    public boolean UpdateDrug(Med med) {
        didCreate = false;
        String createDrug = URL + UPDATE_DRUG + AND + "freqType" + EQUALS + med.getFreqType() + AND
                + "freqNo" + EQUALS + med.getFreqNo() + AND + "startDate" +
                EQUALS + med.getStartDate() + AND + "startTime" + EQUALS + med.getStartTime() + AND
                + "id" + EQUALS + med.getId();
        System.out.println(createDrug);
        StringRequest stringReq = new StringRequest(Request.Method.POST, createDrug, response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.toString().contains("successfully")) {
                    didCreate = true;
                } else {
                    didCreate = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                didCreate = false;
            }
        },
                error -> Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show());

        getInstance(context).addToRequestQueue(stringReq);
        return didCreate;
    }
}

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
import java.sql.Time;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.objects.Report;
import edu.wgu.zamzow.medalert.utils.DateHelper;
import edu.wgu.zamzow.medalert.utils.Vars;

public class Reports extends ServerComm{

    private final Context context;

    public Reports(Context context) {
        super(context);
        this.context = context;

    }

    public Report getAll() throws IOException, JSONException {
        String urlTaken = URL + "getAllTaken" + AND + "userID" + EQUALS + Vars.userID;
        String urlMissed = URL + "getAllMissed" + AND + "userID" + EQUALS + Vars.userID;

        int taken = getAllData(urlTaken);
        int missed = getAllData(urlMissed);

        Report report = new Report();
        report.setNumTaken(taken);
        report.setNumMissed(missed);
        report.setNumMeds(taken + missed);
        return report;
    }

    public Report getAllMed(Med med) throws IOException, JSONException {
        String urlTaken = URL + "getTakenMed" + AND + "userID" + EQUALS + Vars.userID + AND +
                "ApplNo" + EQUALS + med.getApplNo() + AND + "ProductNo" + EQUALS + med.getProdNo();
        String urlMissed = URL + "getMissedMed" + AND + "userID" + EQUALS + Vars.userID + AND  +
                "ApplNo" + EQUALS + med.getApplNo() + AND + "ProductNo" + EQUALS + med.getProdNo();

        int taken = getAllData(urlTaken);
        int missed = getAllData(urlMissed);

        Report report = new Report();
        report.setNumTaken(taken);
        report.setNumMissed(missed);
        report.setNumMeds(taken + missed);
        return report;
    }

    private int getAllData(String URL) throws IOException, JSONException {
        URL url = new URL(URL);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
        }

        int count = 0;
        if (!sb.toString().trim().equals("{\"response\":[]}")) {
            JSONObject obj = new JSONObject(sb.toString());
            JSONArray jsonArray = obj.getJSONArray("response");
            count = jsonArray.length();
        }
        return count;
    }
}

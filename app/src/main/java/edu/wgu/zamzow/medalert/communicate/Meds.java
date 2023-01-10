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
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.HttpsURLConnection;

import edu.wgu.zamzow.medalert.objects.Med;
import edu.wgu.zamzow.medalert.objects.User;
import edu.wgu.zamzow.medalert.utils.Vars;

public class Meds extends ServerComm{

    private final Context context;

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
}

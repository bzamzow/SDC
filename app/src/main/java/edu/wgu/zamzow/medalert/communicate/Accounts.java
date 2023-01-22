package edu.wgu.zamzow.medalert.communicate;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.HttpsURLConnection;

import edu.wgu.zamzow.medalert.objects.User;
import edu.wgu.zamzow.medalert.utils.Vars;

public class Accounts extends ServerComm{

    private final Context context;

    public Accounts(Context context) {
        super(context);
        this.context = context;

    }

    public AtomicBoolean CreateAccount(User user) {
        AtomicBoolean didCreate = new AtomicBoolean(false);
        String createUser = URL + CREATE_USER + AND + USER_NAME + EQUALS + user.getUserName() + AND
                + FIRST_NAME + EQUALS + user.getFirstName() + AND + LAST_NAME + EQUALS +
                user.getLastName() + AND + EMAIL + EQUALS + user.getEmail() + AND + PASSWORD
                + EQUALS + user.getPassword();
        StringRequest stringReq = new StringRequest(Request.Method.POST, createUser, response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    didCreate.set(true);
                } else {
                    didCreate.set(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                didCreate.set(false);
            }
        },
                error -> Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show());

        getInstance(context).addToRequestQueue(stringReq);
        return didCreate;
    }

    public boolean doLogin(String userName, String passWord) throws IOException, JSONException {
        String loginString = URL + LOGIN + AND + UN + EQUALS + userName + AND
                + PW + EQUALS + passWord;

        URL url = new URL(loginString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String json;
        while((json = bufferedReader.readLine()) != null) {
            sb.append(json).append("\n");
        }
        boolean loginGood;
        if (!sb.toString().trim().equals("{\"response\":[]}")) {
            JSONObject obj = new JSONObject(sb.toString());
            JSONArray jsonArray = obj.getJSONArray("response");
            User user = new User();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                user.setEmail(jsonObject.get("email").toString());
                user.setLastName(jsonObject.get("lastName").toString());
                user.setFirstName(jsonObject.get("firstName").toString());
                user.setUserName(jsonObject.get("userName").toString());
                user.setUserID(jsonObject.getInt("userID"));
            }
            if (user.getUserName().equals(userName)) {
                loginGood = true;
                Vars.user = user;
                Vars.userID = user.getUserID();
            } else {
                loginGood = false;
            }
        } else {
            loginGood = false;
        }
        return loginGood;
    }
}

package edu.wgu.zamzow.medalert.communicate;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ServerComm {

    protected final String URL = "https://jmzsoft.com/medalert.php?";
    protected final String CREATE_USER = "createUser";
    protected final String GET_PRODUCTS = "getProducts";
    protected final String USER_NAME = "userName";
    protected final String UN = "un";
    protected final String FIRST_NAME = "firstName";
    protected final String LAST_NAME = "lastName";
    protected final String EMAIL = "email";
    protected final String GET_USERS = "getUsers";
    protected final String GET_DRUG_NAMES = "getDrugNames";
    protected final String PW = "pw";
    protected final String PASSWORD = "password";
    protected final String PRODUCT = "prod";
    protected final String EQUALS = "=";
    protected final String AND = "&";
    protected final String LOGIN = "login";

    private static ServerComm mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    protected ServerComm(Context context) {
        ServerComm.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ServerComm getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ServerComm(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}

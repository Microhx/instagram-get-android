package com.dolphpire.api.action.user.businessAccount.configure;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dolphpire.api.initializer.DolphPireApp;
import com.dolphpire.api.interfaces.ApiCallback;
import com.dolphpire.api.interfaces.FailureCallback;
import com.dolphpire.api.interfaces.DPireOnCompleteCallback;
import com.dolphpire.api.links.EndPoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExecuteAction {

    //class model
    private String category;
    private DPireOnCompleteCallback.OnComplete onComplete;
    private FailureCallback.OnFailureListener onFailureListener;
    private ApiCallback.ApiKeyError mApiKeyError;

    ExecuteAction() {

    }

    void setCategory(int category) {
        this.category = String.valueOf(category);
    }

    public void execute() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.USER_CONFIGURE_BUSINESS_ACCOUNT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "response: " + response);
                try {
                    JSONObject responseObj = new JSONObject(response);
                    // check for error flag
                    if (!responseObj.getBoolean("error")) {

                        if (onComplete != null) {
                            onComplete.onCompleted();
                        }

                    } else {
                        JSONObject errorData = responseObj.getJSONObject("errorData");
                        if (errorData.getInt("errorType") == 100) {
                            if (mApiKeyError != null) {
                                mApiKeyError.badApi();
                            }
                        } else {
                            if (onFailureListener != null) {
                                Exception exception = new Exception(errorData.getInt("errorType") + ", " + errorData.getInt("errorMessage"));
                                onFailureListener.onFailure(exception);
                            }
                        }
                    }

                } catch (JSONException ignored) {
                    //empty method
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", DolphPireApp.getInstance().getApiKey());
                params.put("package_name", DolphPireApp.getInstance().getPackage());
                params.put("my_uid", String.valueOf(DolphPireApp.getInstance().getUUID()));

                params.put("category", category);
                return params;
            }
        };

        //Adding request to request queue
        DolphPireApp.getInstance().addToRequestQueue(strReq);

    }

    public ExecuteAction addOnCompleteListener(DPireOnCompleteCallback.OnComplete onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public ExecuteAction addOnFailureListener(FailureCallback.OnFailureListener onFailureListener) {
        this.onFailureListener = onFailureListener;
        return this;
    }

    public ExecuteAction addOnFailedListener(ApiCallback.ApiKeyError mApiKeyError) {
        this.mApiKeyError = mApiKeyError;
        return this;
    }

}

package uwcj.kr.tensorflow;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.PriorityQueue;


/**
 * Created by zoonoo on 2016-01-12.
 */
public class APIConnector {
    private static String log = "APIConnector";
    static String server_url = "http://110.76.95.149:20000";
    public static void uploadJsonToServer(final JSONObject obj, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(server_url + "/image_send");
                try {
                    StringEntity postingString = new StringEntity(obj.toString());
                    post.setEntity(postingString);
                    post.setHeader("Content-type", "application/json");
                    HttpResponse response = client.execute(post);

                    Message msg = handler.obtainMessage();
                    String responseString = new BasicResponseHandler().handleResponse(response);
                    msg.obj = responseString;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    Log.d(log, e.toString());
                }
            }
        };
        thread.start();
    }
    public Boolean uploadImaeToServer(ContentValues values) {
        String server_url = "http://120.120.120.120";
        HttpEntity httpEntity = null;

        /*
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpPost(server_url);

            httpPost.setEntity(new UrlEncodedFormEntity(values));
            HttpResponse httpResponse = httpClient.execute(httpPost);

        */

        return true;

    }
}

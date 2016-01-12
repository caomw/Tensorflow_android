package uwcj.kr.tensorflow;


import android.content.ContentValues;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.PriorityQueue;


/**
 * Created by zoonoo on 2016-01-12.
 */
public class APIConnector {
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

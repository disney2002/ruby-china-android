package com.testerhome.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.basecamp.turbolinks.TurbolinksSession;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jayway.jsonpath.JsonPath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ShoucangActivity extends AppCompatActivity {

    SSLContext sslContext = null;
    String json = "";
    ListView shoucangListView;
    String loginuser;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoucang);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.shoucang_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);


        Log.i("xsz", "进入列表页启动");
        shoucangListView = (ListView) this.findViewById(R.id.shoucang_listview);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        loginuser = bundle.getString("loginuser");
        Log.i("xsz","登陆用户名===="+loginuser);

        //-------------请求okhttps
        new Thread() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = httpclient();
                Request request = new Request.Builder().url("https://testerhome.com/api/v3/users/"+loginuser+"/favorites.json").build();
                try {

                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        json = response.body().string();
                        shoucangListView.post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject jo = new JSONObject(json);
                                    JSONArray topics = jo.getJSONArray("topics");

                                    //生成动态数组，并且转载数据
                                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                    for (int i = 0; i < topics.length(); i++) {
                                        JSONObject topic = topics.getJSONObject(i);
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        map.put("ItemImg","https://testerhome.com/"+topic.getJSONObject("user").getString("avatar_url"));
                                        map.put("ItemTitleId", topic.getString("id"));
                                        map.put("ItemTitle", topic.getString("title"));
                                        map.put("ItemText", topic.getJSONObject("user").getString("name"));
                                        map.put("ItemRepliesCount", topic.getString("replies_count"));

                                        mylist.add(map);
                                    }

                                    //生成适配器，数组===》ListItem
                                    SimpleAdapter mSchedule = new SimpleAdapter(ShoucangActivity.this, //没什么解释
                                            mylist,//数据来源
                                            R.layout.shoucang_list,//ListItem的XML实现

                                            //动态数组与ListItem对应的子项
                                            new String[]{"ItemImg","ItemTitleId","ItemTitle", "ItemText","ItemRepliesCount","ItemRepliesCount"},

                                            //ListItem的XML文件里面的两个TextView ID
                                            new int[]{R.id.ItemImg,R.id.ItemTitleId,R.id.ItemTitle, R.id.ItemText,R.id.ItemRepliesCount});
                                    //添加并且显示
                                    shoucangListView.setAdapter(mSchedule);

                                    //添加点击事件
                                    shoucangListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            //获得选中项的HashMap对象
                                            HashMap<String,String> map=(HashMap<String,String>)shoucangListView.getItemAtPosition(position);
                                            String topicid =map.get("ItemTitleId");
                                            //通过topicid打开帖子详情页面
                                            //https://testerhome.com/topics/6033
                                            Intent intent = new Intent(ShoucangActivity.this, TopicActivity.class);
                                            String url = "https://testerhome.com/topics/"+topicid;
                                            intent.putExtra("intentUrl", url);
                                            ShoucangActivity.this.startActivity(intent);
                                        }
                                    });


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void overlockCard() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(
                            X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        X509Certificate[] x509Certificates = new X509Certificate[0];
                        return x509Certificates;
                    }
                }};
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private OkHttpClient httpclient() {
        //忽略所有证书
        overlockCard();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).sslSocketFactory(sslContext.getSocketFactory()).hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();

        return okHttpClient;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Shoucang Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}

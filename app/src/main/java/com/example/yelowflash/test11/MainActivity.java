package com.example.yelowflash.test11;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String MYURL = "http://54.254.204.73/api/property/feeds";
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        pager = (ViewPager) findViewById(R.id.pager);
        if (AppHelper.isConnectingToInternet(MainActivity.this)) {
            new GetAllData().execute(MYURL);
        } else {
            Toast.makeText(MainActivity.this, "Please Connect to internet and try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class GetAllData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please Wait");
            dialog.setMessage("Fetching Data");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                // Log.i("res", s);
                JSONObject respnseJoson = new JSONObject(s);
                if (respnseJoson.getString("success").equals("1")) {
                    JSONArray feedsArray = respnseJoson.getJSONArray("feeds");
                    ArrayList<FeedFragment> list = new ArrayList<>();
                    for (int i = 0; i < feedsArray.length(); i++) {
                        JSONObject feedObject = feedsArray.getJSONObject(i);
                        FeedFragment fragment = new FeedFragment();
                        Bundle b = new Bundle();
                        b.putString("property_name", feedObject.getString("property_name"));
                        b.putString("image", feedObject.getString("image"));
                        b.putString("address", feedObject.getString("address"));
                        b.putString("comment_count", feedObject.getString("comment_count"));
                        b.putString("developer_name", feedObject.getJSONArray("developer_name").getString(0));
                        b.putString("sale_date", feedObject.getString("sale_date"));
                        b.putString("favor_count", feedObject.getString("favor_count"));
                        fragment.setArguments(b);
                        list.add(fragment);
                    }
                    PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), list);
                    pager.setAdapter(adapter);
                }
            } catch (NullPointerException e) {
                Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormEncodingBuilder().add("lang", "0").build();
            Request request = new Request.Builder().url(params[0]).post(body).build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (SocketTimeoutException e) {
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {
        ArrayList<FeedFragment> list;

        public PagerAdapter(FragmentManager fm, ArrayList<FeedFragment> myFragmentArrayList) {
            super(fm);
            list = myFragmentArrayList;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}

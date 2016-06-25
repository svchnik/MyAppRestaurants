package com.example.nik.myapprestaurants;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class FoursquareActivity extends ListActivity{

    ArrayList<FoursquareVenue> venuesList;

    final String CLIENT_ID = "IN2NUXFFAITCX2IUOFPOPAQIMBMGHLXCY0QX1I2K0KYK5EW0";
    final String CLIENT_SECRET = "FWHO2X5YIZABMR2AYCXK0ZULJDAIK2FT2MZCU1WACV4M5OTM";

    final String latitude = "40.7463";
    final String longtitude = "-73.9852";

    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foursquare_main);

        new MyFourquare().execute();
    }

    private class MyFourquare extends AsyncTask<View, Void, String> {

        String temp;

        @Override
        protected String doInBackground(View... urls) {
            temp = makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20130815&ll=40.7463,-73.9852");
            return "";
        }

        @Override
        protected void onPreExecute() {
            // progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (temp == null) {
            } else {
                venuesList = (ArrayList<FoursquareVenue>) parseFoursquare(temp);

                List<String> listTitle = new ArrayList<String>();

                for (int i = 0; i < venuesList.size(); i++) {
                    listTitle.add(i, venuesList.get(i).getName() + ", " + venuesList.get(i).getCategory() + "" + venuesList.get(i).getCity());
                }
                myAdapter = new ArrayAdapter<String>(FoursquareActivity.this, R.layout.row_layout, R.id.listText, listTitle);
                setListAdapter(myAdapter);
            }
        }
    }

    public static String makeCall(String url) {

        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {

            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();


            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return replyString.trim();
    }

    private static ArrayList<FoursquareVenue> parseFoursquare(final String response) {

        ArrayList<FoursquareVenue> temp = new ArrayList<FoursquareVenue>();
        try {

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        FoursquareVenue poi = new FoursquareVenue();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).getString("name"));

                            if (jsonArray.getJSONObject(i).has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("city")) {
                                        poi.setCity(jsonArray.getJSONObject(i).getJSONObject("location").getString("city"));
                                    }
                                    if (jsonArray.getJSONObject(i).has("categories")) {
                                        if (jsonArray.getJSONObject(i).getJSONArray("categories").length() > 0) {
                                            if (jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).has("icon")) {
                                                poi.setCategory(jsonArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getString("name"));
                                            }
                                        }
                                    }
                                    temp.add(poi);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<FoursquareVenue>();
        }
        return temp;

    }
}

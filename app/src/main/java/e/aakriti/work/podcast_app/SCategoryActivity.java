package e.aakriti.work.podcast_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import e.aakriti.work.adapter.SpecificCategoryAdapter;
import e.aakriti.work.common.RestApi;
import e.aakriti.work.common.SharedData;
import e.aakriti.work.common.Utility;
import e.aakriti.work.objects.SpecificCategoryObject;

/**
 * Created by uadmin on 4/1/16.
 */
public class SCategoryActivity extends AppCompatActivity {
    static SpecificCategoryAdapter listAdapter;
    GridView categoryGreed;
    String parentCatId;
    String parentCatName;
    ArrayList<Bitmap> catArrayBitmap;
    Intent intentFromCategory;
    private static String url = null;
    TextView titleText;
    ImageView backButton;
    public static String userId = null;


    SharedData sharedData;
    Utility utility;
    static List<SpecificCategoryObject> allCategories;
    TextView headerText;
    ImageView headerBackImage;
    View header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scategory_layout);

        intentFromCategory = getIntent();
        parentCatId = intentFromCategory.getStringExtra("catId");
        parentCatName = intentFromCategory.getStringExtra("catName");
        userId = intentFromCategory.getStringExtra("userId");
        categoryGreed = (GridView) findViewById(R.id.categoryGrid);

        header = (View)findViewById(R.id.layout_header);
        headerText = (TextView)header.findViewById(R.id.titleTxt);
        headerBackImage = (ImageView) header.findViewById(R.id.menuImg);
        headerBackImage.setImageResource(R.drawable.back);
        headerText.setText("Favourites");
        utility = new Utility(SCategoryActivity.this);



        headerText.setText(parentCatName);
        sharedData = new SharedData(SCategoryActivity.this);

        headerBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });



        url = "http://www.whooshkaa.com/index.php/api/getCategoryContents/"+
                "cat_id/"+parentCatId+"/user/7";

        new DoTask().execute(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class DoTask extends AsyncTask<String, Void, String>{
        ProgressDialog prog;
        private String result = null, errorMessage = "",response = "";
        private int errorCode = 0;

        @Override
        protected void onPreExecute() {
            prog = new ProgressDialog(SCategoryActivity.this);
            prog.setMessage("Loading...");
            prog.show();
        }

        @Override
        protected String doInBackground(final String... params) {
            try {
                if (utility.isNetworkAvailable()) {
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String uri = params[0];
                                result = RestApi.getDataFromURLWithoutParam(uri);

                                if (Utility.isNotNull(result)) {
                                    final JSONObject objRes = new JSONObject(result);
                                    response = objRes.optString("response");

                                    if (!response.equalsIgnoreCase("")) {
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                // TODO Auto-generated method
                                                // stub

                                                try {
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    allCategories = new ArrayList<SpecificCategoryObject>(jsonArray.length());
                                                    for (int i =0 ;i<jsonArray.length();i++)
                                                    {
                                                        JSONObject obj = jsonArray.getJSONObject(i);
                                                        SpecificCategoryObject cat = new SpecificCategoryObject(obj);
                                                        allCategories.add(cat);
                                                    }
                                                    Log.e("allQue", ""+allCategories.size());
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                // TODO Auto-generated method
                                                // stub

                                                errorMessage = objRes.optString("msg");
                                                Toast.makeText(SCategoryActivity.this, "" + errorMessage, Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        });
                                    }
                                }
                            } catch (ConnectException e) {
                                Log.e("", "" + e.toString());
                            } catch (Exception e) {
                                Log.e("", "" + e.toString());
                            }
                        }
                    });
                    th.start();
                    th.join();
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Toast.makeText(SCategoryActivity.this, "Please check your Internet connection",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String al) {
            if (prog.isShowing())
                prog.dismiss();
            listAdapter = new SpecificCategoryAdapter(SCategoryActivity.this, allCategories, userId);

            // setting list adapter
            categoryGreed.setAdapter(listAdapter);
            categoryGreed.setColumnWidth(100);
            categoryGreed.setNumColumns(3);

            listAdapter.notifyDataSetChanged();
            super.onPostExecute(result);

            categoryGreed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //SpecificCategoryObject specificCategoryObject = new SpecificCategoryObject();
                    //Intent intentFromSpecificCategory = new Intent (SCategoryActivity.this, EpisodesPage.class);
                    //intentFromSpecificCategory.putExtra("SHOWID", )
                    //intentFromSpecificCategory.putExtra("");
                }
            });

        }
    }
}
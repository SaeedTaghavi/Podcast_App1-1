package e.aakriti.work.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;

import e.aakriti.work.adapter.Explore_showsAdapter;
import e.aakriti.work.objects.Explore_shows;
import e.aakriti.work.podcast_app.MainActivity;

public class getAllExploreShowsWS extends AsyncTask<Void, Void, String> {

	private ProgressDialog mLoader;
	private String result = null, errorMessage = "",response = "";
	private int errorCode = 0;
	GridView list;
	Context context;
	int numburOfScroll;
	ArrayList<Explore_shows> exp;
	//ArrayList<Categories> allCategories;
	Explore_showsAdapter adapter;
	Utility utility;
	LinearLayout footerLoader;
	public static String status = null;

	public getAllExploreShowsWS(int numburOfScroll,LinearLayout footerLoader, ArrayList<Explore_shows> exp, GridView gridView_shows,Context c,Explore_showsAdapter showsadapter) {
		// TODO Auto-generated constructor stub
		this.list = gridView_shows;
		this.context = c;
		this.adapter = showsadapter;
		utility = new Utility(context);
		this.footerLoader = footerLoader;
		this.exp = exp;
		this.numburOfScroll = numburOfScroll;
		adapter = new Explore_showsAdapter(context);
	}



	@Override
	protected void onPreExecute() {
		mLoader = new ProgressDialog(context);
		mLoader.setMessage("Loading");
		mLoader.setCancelable(false);
		if(numburOfScroll <= 0){
			footerLoader.setVisibility(View.GONE);
			mLoader.show();
		} else {
			footerLoader.setVisibility(View.VISIBLE);
		}

		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		if (mLoader.isShowing())
			mLoader.dismiss();
		if(footerLoader.getVisibility() == View.VISIBLE){
			footerLoader.setVisibility(View.GONE);
		}
		if(status.equals("SUCCESS")){
			list.setAdapter(adapter);
		}


		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			if (utility.isNetworkAvailable()) {
				Thread th = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							String uri = RestApi.createURI(RestApi.GetAllExplore_episodes_WS)+"/type/show/user/7/page/"+numburOfScroll;
							//String uri = "http://www.whooshkaa.com/index.php?r=api/LoginDevice&user_name="
							//	+ userName + "&password=" + passWord;
							result = RestApi.getDataFromURLWithoutParam(uri);

							if (Utility.isNotNull(result)) {
								final JSONObject objRes = new JSONObject(result);
								response = objRes.optString("data");

								if (!response.equalsIgnoreCase("")) {
									((Activity) context).runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method
											// stub

											try {
												status = objRes.getString("status");
												JSONArray jsonArray = new JSONArray(response);
												MainActivity.explore_shows = new ArrayList<Explore_shows>(jsonArray.length());
												for (int i =0 ;i<jsonArray.length();i++)
												{
													JSONObject obj = jsonArray.getJSONObject(i);
													Explore_shows show = new Explore_shows(obj);
													exp.add(show);
													MainActivity.explore_shows.add(show);
												}
												Log.e("show", ""+MainActivity.explore_shows.size());
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												Log.e("ece", ""+e.toString());
												e.printStackTrace();
											}

										}
									});
								} else {
									((Activity) context).runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method
											// stub

											errorMessage = objRes.optString("msg");
											Toast.makeText(context, "" + errorMessage, Toast.LENGTH_LONG)
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
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(context, "Please check your Internet connection",
								Toast.LENGTH_LONG).show();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
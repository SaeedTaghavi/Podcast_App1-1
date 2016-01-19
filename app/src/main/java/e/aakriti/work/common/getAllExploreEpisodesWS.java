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

import e.aakriti.work.adapter.Explore_Episodesadapter;
import e.aakriti.work.objects.Explore_episodes;
import e.aakriti.work.podcast_app.MainActivity;

public class getAllExploreEpisodesWS extends AsyncTask<Void, Void, String> {
	LinearLayout footerLoader;
	private ProgressDialog mLoader;
	private String result = null, errorMessage = "",response = "";
	private int errorCode = 0;
	int numburOfScroll;
	public static  String status = null;
	GridView list;
	public static int allpage = 0;
	Context context;
	ArrayList<Explore_episodes> exp;
	//ArrayList<Categories> allCategories;
	Explore_Episodesadapter adapter;
	Utility utility;
	public  static  Explore_episodes episode = null;

	public getAllExploreEpisodesWS(int numburOfScroll,LinearLayout footerLoader, ArrayList<Explore_episodes> exp, GridView gridView_episodes,Context c,Explore_Episodesadapter episodesadapter) {
		// TODO Auto-generated constructor stub
		this.list = gridView_episodes;
		this.context = c;
		this.numburOfScroll = numburOfScroll;
		//this.allCategories = (ArrayList<Categories>) MainActivity.allCategories;
		this.adapter = episodesadapter;
		utility = new Utility(context);
		this.footerLoader = footerLoader;
		this.exp = exp;
		adapter = new Explore_Episodesadapter(context);
	}



	@Override
	protected void onPreExecute() {
		mLoader = new ProgressDialog(context);
		mLoader.setMessage("Loading");
		mLoader.setCancelable(false);
		if(numburOfScroll == 0){
			footerLoader.setVisibility(View.GONE);
			mLoader.show();
		}else{
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
			//list.invalidate();
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
							String uri = RestApi.createURI(RestApi.GetAllExplore_episodes_WS)+"/type/episode/user/7/page/"+numburOfScroll;
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
												allpage = objRes.getInt("allpage");
												JSONArray jsonArray = new JSONArray(response);
												MainActivity.explore_episodes = new ArrayList<Explore_episodes>(jsonArray.length());
												for (int i =0 ;i<jsonArray.length();i++)
												{
													JSONObject obj = jsonArray.getJSONObject(i);
													Explore_episodes episode = new Explore_episodes(obj);
													//episode = new Explore_episodes(obj);
													exp.add(episode);
													MainActivity.explore_episodes.add(episode);
												}
												Log.e("episode", ""+MainActivity.explore_episodes.size());
											} catch (JSONException e) {
												// TODO Auto-generated catch block
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
		return status;
	}
}
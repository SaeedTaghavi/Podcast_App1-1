package com.projectemplate.musicpro.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.projectemplate.musicpro.BaseFragment;
import com.projectemplate.musicpro.activity.DownloadUpdateActivity;
import com.projectemplate.musicpro.config.GlobalValue;

import org.json.JSONObject;

import java.io.File;

import e.aakriti.work.common.SharedData;
import e.aakriti.work.common.Utility;
import e.aakriti.work.imageloader.ImageLoader;
import e.aakriti.work.objects.Song;
import e.aakriti.work.podcast_app.R;

public class PlayerThumbFragment extends BaseFragment {
	// private ImageView imgSong;
	private TextView lblNameSong, lblArtist;
	private String rootFolder;
	private ImageView imgSong;
	private LinearLayout llInfo;
	private ImageView btnShare;
	private ImageView btnfav;
	private ImageView btncomment;
	private ImageView btnplaylist;
	private ImageView btnDownload;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_player_thumb, container, false);
		initUI(view);
//        listAq = new AQuery(getActivity());
		return view;
	}

	private void initUI(View view) {
		// imgSong = (ImageView) view.findViewById(R.id.imgSong);
		lblNameSong = (TextView) view.findViewById(R.id.lblNameSong);
		lblArtist = (TextView) view.findViewById(R.id.lblArtist);
		imgSong = (ImageView) view.findViewById(R.id.imgSong);
		llInfo = (LinearLayout) view.findViewById(R.id.llInfo);
		btnShare = (ImageView) view.findViewById(R.id.btnShare);
		btnfav = (ImageView) view.findViewById(R.id.btnfav);
		btncomment = (ImageView) view.findViewById(R.id.btncomment);
		btnplaylist = (ImageView) view.findViewById(R.id.btnplaylist);
		btnDownload = (ImageView) view.findViewById(R.id.btnDownload);
		lblNameSong.setSelected(true);
		lblArtist.setSelected(true);
		view.findViewById(R.id.btnDownload).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickDownload();
			}
		});
		view.findViewById(R.id.btnShare).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickShare();
			}
		});

		if (getArguments()!=null){
			llInfo.setVisibility(View.GONE);
		}
		rootFolder = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/";
		File folder = new File(rootFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		btnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickShare();
			}
		});
		btnfav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickFavourite();
			}
		});
		btncomment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickComment();
			}
		});
		btnplaylist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickplayalist();
			}
		});
		btnDownload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickDownload();
			}
		});
	}

	private void onClickplayalist() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = new PlaylistFragment();
		newFragment.show(ft, "dialog");

	}

	private void onClickComment() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		final EditText edittext = new EditText(getActivity());
		alert.setMessage("Post Comment");
		alert.setTitle("Post Comment");

		alert.setView(edittext);

		alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//What ever you want to do with the value
				Editable YouEditTextValue = edittext.getText();
				//OR
				String commentvalue = edittext.getText().toString();
				if (commentvalue.length() > 0) {
					Post_comment(commentvalue);
				} else {
					Toast.makeText(getActivity(), "Please, Enter Comment.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		alert.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// what ever you want to do with No option.
				dialog.dismiss();
			}
		});

		alert.show();
	}

	private void Post_comment(String commentvalue) {
		String podcaster_id = "271";
		SharedData sharedData = new SharedData(getActivity());
		String listner_id = sharedData.getString("ListnerId", "26");
//        String url = "http://www.whooshkaa.com/index.php/api/EpisodesByPodcastId/podcast_id/" + podcaster_id + "/user/" + listner_id;
		String url = "http://www.whooshkaa.com/index.php/api/Comment/podcast_id/" + podcaster_id + "/media_id/129/cmt/"+commentvalue+"/list_id/26";
		final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "", false, false);
		Log.d("url", url);
		Volley.newRequestQueue(getActivity()).add(new StringRequest(url, new Response.Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				try {
					if (Utility.isNotNull(arg0)) {

						if (arg0.contains("\"msg\":\"success\"")) {
							final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

							alert.setMessage("Comment posted successfully");
							alert.setTitle("Comment");


							alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									//What ever you want to do with the value
									dialog.dismiss();
								}
							});
							alert.show();

						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Toast.makeText(getActivity(), arg0.getMessage(), Toast.LENGTH_LONG).show();
			}
		}));
	}

	private void onClickFavourite() {
//        String url = "http://www.whooshkaa.com/index.php/api/like/listener/26/media/129/podcast/163
		SharedData sharedData = new SharedData(getActivity());
		String listner_id = sharedData.getString("ListnerId", "26");
		String media_id = "129";
		String podcast_id="163";
		String url = "http://www.whooshkaa.com/index.php/api/like/listener/" + listner_id + "/media/"+media_id+"/podcast/"+podcast_id;
		final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "", false, false);
		Log.d("url", url);
		Volley.newRequestQueue(getActivity()).add(new StringRequest(url, new Response.Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				try {
					if (Utility.isNotNull(arg0)) {

						JSONObject jsonObject = new JSONObject(arg0);
						String like  = jsonObject.opt("msg").toString();
						if (like.equals("liked")) {

							btnfav.setImageResource(R.drawable.heart_red);
						}else {
							btnfav.setImageResource(R.drawable.heart);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Toast.makeText(getActivity(), arg0.getMessage(), Toast.LENGTH_LONG).show();
			}
		}));
	}

	public void refreshData() {
		if (lblNameSong != null && lblArtist != null) {
			lblNameSong.setText(GlobalValue.getCurrentSong().name);
			lblArtist.setText(GlobalValue.getCurrentSong().singerName);
			new ImageLoader(getActivity()).DisplayImage(GlobalValue.getCurrentSong().image, imgSong);
//			AQuery aq = listAq.recycle(getView());
//			aq.id(R.id.imgSong).image(GlobalValue.getCurrentSong().getImage(), true, true, 0, R.drawable.ic_music_node_custom,
//					GlobalValue.ic_music_node_custom, AQuery.FADE_IN_NETWORK, 0);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					refreshData();
				}
			}, 500);
		}
	}

	private void onClickDownload() {
		Song currentSong = GlobalValue.getCurrentSong();
		File file = new File(rootFolder, currentSong.name + " - " + currentSong.singerName + ".mp3");
		if (file.exists()) {
			Toast.makeText(getActivity(), R.string.songExisted, Toast.LENGTH_SHORT).show();
		} else {
			Intent intent = new Intent(getActivity(), DownloadUpdateActivity.class);
			intent.putExtra("url_song", currentSong.link);
			intent.putExtra("file_name", currentSong.name + " - " + currentSong.singerName + ".mp3");
			startActivity(intent);
		}
	}

	private void onClickShare() {
		Song currentSong = GlobalValue.getCurrentSong();
		String shareBody = currentSong.link;
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
				currentSong.name + " - " + currentSong.singerName);
		sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share)));
	}
}

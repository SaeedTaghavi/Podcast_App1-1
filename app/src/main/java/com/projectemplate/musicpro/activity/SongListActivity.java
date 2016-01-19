package com.projectemplate.musicpro.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.projectemplate.musicpro.config.GlobalValue;
import com.projectemplate.musicpro.database.DatabaseUtility;
import com.projectemplate.musicpro.fragment.CarPlayerFragment;
import com.projectemplate.musicpro.fragment.PlayerFragment;
import com.projectemplate.musicpro.object.Playlist;
import com.projectemplate.musicpro.service.MusicService;
import com.projectemplate.musicpro.service.MusicService.ServiceBinder;
import com.projectemplate.musicpro.service.PlayerListener;
import com.projectemplate.musicpro.util.LanguageUtil;
import com.projectemplate.musicpro.util.Logger;
import com.projectemplate.musicpro.util.MySharedPreferences;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import e.aakriti.work.common.SharedData;
import e.aakriti.work.common.Utility;
import e.aakriti.work.objects.GetListVo;
import e.aakriti.work.objects.PopularShows;
import e.aakriti.work.objects.Song;
import e.aakriti.work.podcast_app.R;

public class SongListActivity extends AppCompatActivity implements OnClickListener {
	public static final int TOP_CHART = 0;
	public static final int NOMINATIONS = 1;
	public static final int CATEGORY_MUSIC = 2;
	public static final int PLAYLIST = 3;
	public static final int SEARCH = 4;
	public static final int GOOD_APP = 5;
	public static final int CARMODE = 6;
	public static final int EXIT_APP = 7;

	public static final int LIST_SONG_FRAGMENT = 0;
	public static final int CATEGORY_MUSIC_FRAGMENT = 1;
	public static final int PLAYLIST_FRAGMENT = 2;
	public static final int SEARCH_FRAGMENT = 3;
	public static final int SETTING_FRAGMENT = 4;
	public static final int PLAYER_FRAGMENT = 5;
	public static final int CARPLAYER_FRAGMENT = 6;

	public static final int FROM_LIST_SONG = 0;
	public static final int FROM_NOTICATION = 1;
	public static final int FROM_SEARCH = 2;
	public static final int FROM_OTHER = 3;

	public static final int NOTIFICATION_ID = 231109;

	private static FragmentManager fm;
	private static Fragment[] arrayFragments;
//    public static SlidingMenu menu;
//	public ModelManager modelManager;

	private ImageView btnPreviousFooter;
	private static ImageView btnPlayFooter;
	private ImageView btnNextFooter;
	// private ImageView imgSongFooter;
	private static View layoutPlayerFooter;
	private TextView lblSongNameFooter, lblArtistFooter;

//    private TextView lblTopChart, lblNominations, lblCategoryMusic, lblPlaylist, lblSearch, lblGoodApp, lblAbout,
//            lblExitApp;

	// private LinearLayout llAdview;
	// private InterstitialAd interstitialAd;
	// private AdView adView;
	// AdRequest interstitialRequest;
	// AdRequest adRequest;

	// private static final String BANNER_AD_ID =
	// "ca-app-pub-9687197024195006/5976545579";
	// private static final String INTERSTITIAL_AD_ID =
	// "ca-app-pub-4954528972146554/1415422024";
	private static final String BANNER_AD_ID = "YOUR_BANNER_AD_ID";
	private static final String INTERSTITIAL_AD_ID = "YOUR_INTERSTITIAL_ID";

	private boolean doubleBackToExitPressedOnce;

	public static int currentFragment;
	public static int currentMusicType;
	public static int toMusicPlayer;
	public static Playlist currentPlaylist;

	public String nextPageNomination;
	public String nextPageTopWeek;

	public List<Song> listNominations;
	public List<Song> listTopWeek;

	public static DatabaseUtility databaseUtility;

	public static MusicService mService;
	private Intent intentService;
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ServiceBinder binder = (ServiceBinder) service;
			mService = binder.getService();
			if (GlobalValue.listSongPlay != null) {

				mService.setListSongs(GlobalValue.listSongPlay);
			}
			mService.setListener(new PlayerListener() {
				@Override
				public void onSeekChanged(String lengthTime, String currentTime, int progress) {
					((PlayerFragment) arrayFragments[PLAYER_FRAGMENT]).seekChanged(lengthTime, currentTime, progress);
					try {

						((CarPlayerFragment) arrayFragments[CARPLAYER_FRAGMENT]).seekChanged(lengthTime, currentTime, progress);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onChangeSong(int indexSong) {
					((PlayerFragment) arrayFragments[PLAYER_FRAGMENT]).changeSong(indexSong);
					try {

						((CarPlayerFragment) arrayFragments[CARPLAYER_FRAGMENT]).changeSong(indexSong);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (GlobalValue.getCurrentSong() != null) {
						lblSongNameFooter.setText(GlobalValue.getCurrentSong().name);
						lblArtistFooter.setText(GlobalValue.getCurrentSong().singerName);
					}
				}
			});
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};
	public static PopularShows selected_song_vo;
	public static Context context;
	private ImageView menuImg;
	private ImageView moreImg;
	private ImageView searchImg;
	private Runnable r;
	private Handler h;

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
//    private GoogleApiClient client;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		databaseUtility = new DatabaseUtility(this);
		setContentView(R.layout.activity_main);
		LanguageUtil.setLocale(new MySharedPreferences(this).getLanguage(), this);
		if (getIntent() != null) {
			selected_song_vo = (PopularShows) getIntent().getSerializableExtra("selected_song");
//            initList(selected_song_vo.getPodcaster_id());
		}
		initList("26");
		context = this;
		GlobalValue.constructor(this);


		initService();

		initUI();
		initControl();
		initFragment();

	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			unbindService(mConnection);
		} catch (Exception e) {
			e.printStackTrace();
			cancelNotification(context);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		bindService(intentService, mConnection, Context.BIND_AUTO_CREATE);
		setVisibilityFooter();
	}

	public static void setVisibilityFooter() {
		try {
			if (mService.isPause() || mService.isPlay()) {
				layoutPlayerFooter.setVisibility(View.VISIBLE);
			} else {
				layoutPlayerFooter.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			layoutPlayerFooter.setVisibility(View.GONE);
		}
	}

	private void initService() {
		intentService = new Intent(this, MusicService.class);
		startService(intentService);
		bindService(intentService, mConnection, Context.BIND_AUTO_CREATE);
	}

	private void initUI() {
		btnPreviousFooter = (ImageView) findViewById(R.id.btnPreviousFooter);
		btnPlayFooter = (ImageView) findViewById(R.id.btnPlayFooter);
		btnNextFooter = (ImageView) findViewById(R.id.btnNextFooter);
		// imgSongFooter = (ImageView) findViewById(R.id.imgSongFooter);
		layoutPlayerFooter = (LinearLayout) findViewById(R.id.layoutPlayerFooter);
		lblSongNameFooter = (TextView) findViewById(R.id.lblSongNameFooter);
		lblArtistFooter = (TextView) findViewById(R.id.lblArtistFooter);
//

		menuImg = (ImageView) findViewById(R.id.menuImg);
		moreImg = (ImageView) findViewById(R.id.moreImg);
		searchImg = (ImageView) findViewById(R.id.searchImg);
		menuImg.setVisibility(View.GONE);
		searchImg.setVisibility(View.GONE);
		moreImg.setVisibility(View.GONE);

		moreImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				PopupMenu popup = new PopupMenu(SongListActivity.this, v);
				try {
					Field[] fields = popup.getClass().getDeclaredFields();
					for (Field field : fields) {
						if ("mPopup".equals(field.getName())) {
							field.setAccessible(true);
							Object menuPopupHelper = field.get(popup);
							Class<?> classPopupHelper = Class.forName(menuPopupHelper
									.getClass().getName());
							Method setForceIcons = classPopupHelper.getMethod(
									"setForceShowIcon", boolean.class);
							setForceIcons.invoke(menuPopupHelper, true);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.menu, popup.getMenu());
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
							case R.id.car:
								carmode();
								return true;
							case R.id.sleeper:
								sleepermode(v);
								return true;
							default:
								return false;
						}
					}
				});

				popup.show();
			}
		});
		// initBannerAd();
		// initInterstitialAd();

		if (GlobalValue.getCurrentSong() != null) {

			lblSongNameFooter.setText(GlobalValue.getCurrentSong().name);
			lblArtistFooter.setText(GlobalValue.getCurrentSong().singerName);
		}

	}


	private void sleepermode(View v) {
		PopupMenu popup = new PopupMenu(SongListActivity.this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.context_menu, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.fifteen:

						setsleeptime(15);
						return true;
					case R.id.thirty:

						setsleeptime(30);
						return true;
					case R.id.fourtyfive:
						setsleeptime(45);

						return true;
					case R.id.sixty:

						setsleeptime(60);
						return true;

					default:
						return false;
				}
			}
		});

		popup.show();
	}

	private void setsleeptime(final int i) {
		if (h != null) {
			h.removeCallbacks(r);
		}
		h = new Handler();
		r = new Runnable() {
			@Override
			public void run() {
				SongListActivity.mService.pauseMusic();
				SongListActivity.setButtonPlay();

			}
		};
		h.postDelayed(r, i * 1000 * 60);
		Toast.makeText(SongListActivity.this, "Whooshka player will sleeping before " + i + " minutes.", Toast.LENGTH_LONG).show();

	}

	private void carmode() {
		setSelect(CARMODE);


	}

	private void initControl() {
		btnPreviousFooter.setOnClickListener(this);
		btnPlayFooter.setOnClickListener(this);
		btnNextFooter.setOnClickListener(this);
		layoutPlayerFooter.setOnClickListener(this);
//
		lblSongNameFooter.setSelected(true);
		lblArtistFooter.setSelected(true);
	}


	private void initFragment() {
		fm = getSupportFragmentManager();
		arrayFragments = new Fragment[7];


		arrayFragments = new Fragment[7];
		arrayFragments[LIST_SONG_FRAGMENT] = fm.findFragmentById(R.id.fragmentListSongs);
		arrayFragments[CATEGORY_MUSIC_FRAGMENT] = fm.findFragmentById(R.id.fragmentCategoryMusic);
		arrayFragments[PLAYLIST_FRAGMENT] = fm.findFragmentById(R.id.fragmentPlaylist);
		arrayFragments[SEARCH_FRAGMENT] = fm.findFragmentById(R.id.fragmentSearch);
		arrayFragments[SETTING_FRAGMENT] = fm.findFragmentById(R.id.fragmentSetting);
		arrayFragments[PLAYER_FRAGMENT] = fm.findFragmentById(R.id.fragmentPlayer);
		arrayFragments[CARPLAYER_FRAGMENT] = fm.findFragmentById(R.id.fragmentCarPlayer);

		FragmentTransaction transaction = fm.beginTransaction();
		for (Fragment fragment : arrayFragments) {
			transaction.hide(fragment);
		}
		transaction.commit();
	}

	private void showFragment(int fragmentIndex) {
		currentFragment = fragmentIndex;
		FragmentTransaction transaction = fm.beginTransaction();
		for (Fragment fragment : arrayFragments) {
			transaction.hide(fragment);
		}
		transaction.show(arrayFragments[fragmentIndex]);
		if (fragmentIndex == PLAYER_FRAGMENT) {
			moreImg.setVisibility(View.VISIBLE);
		}else {
			moreImg.setVisibility(View.GONE);
		}
		transaction.commit();
		Logger.e(fragmentIndex);
	}

	private void initList(String podcaster_id) {
		listNominations = new ArrayList<Song>();
		listTopWeek = new ArrayList<Song>();
		getdata_ws(podcaster_id);
	}


	private void getdata_ws(String podcaster_id) {
		// TODO Auto-generated method stub
//        utility = new Utility(MainActivity.this);
//        podcaster_id = "271";
		SharedData sharedData = new SharedData(SongListActivity.this);
		String listner_id = sharedData.getString("ListnerId", "2");
		String url = "https://www.whooshkaa.com/index.php/api/EpisodesByPodcastId/podcast_id/" + podcaster_id + "/user/" + listner_id;
		final ProgressDialog dialog = ProgressDialog.show(SongListActivity.this, "", "", false, false);
		Log.d("url", url);
		Volley.newRequestQueue(SongListActivity.this).add(new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				try {
					if (Utility.isNotNull(arg0)) {
						Log.d("response", arg0);
						GetListVo GetListVo = new Gson().fromJson(arg0, GetListVo.class);
						if (GetListVo != null) {
							listTopWeek.addAll(GetListVo.data);
							GlobalValue.listSongPlay.addAll(GetListVo.data);
							try {
								if (getIntent().getExtras().get("notification") != null) {
									toMusicPlayer = SongListActivity.FROM_NOTICATION;
									showFragment(PLAYER_FRAGMENT);
								} else {
									setSelect(TOP_CHART);
								}
							} catch (Exception e) {
								setSelect(TOP_CHART);
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Toast.makeText(SongListActivity.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
			}
		}));

	}


	public static void gotoFragment(int fragment) {
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
		transaction.show(arrayFragments[fragment]);
		transaction.hide(arrayFragments[currentFragment]);
		transaction.commit();
		currentFragment = fragment;
	}

	public void backFragment(int fragment) {
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
		transaction.show(arrayFragments[fragment]);
		transaction.hide(arrayFragments[currentFragment]);
		transaction.commit();
		currentFragment = fragment;
	}

	private void setSelect(int select) {
		GlobalValue.currentMenu = select;
		switch (select) {
			case TOP_CHART:

				showFragment(LIST_SONG_FRAGMENT);
				break;

			case NOMINATIONS:

				showFragment(LIST_SONG_FRAGMENT);
				break;

			case CATEGORY_MUSIC:

				showFragment(CATEGORY_MUSIC_FRAGMENT);
				break;

			case PLAYLIST:

				showFragment(PLAYLIST_FRAGMENT);
				break;

			case SEARCH:

				showFragment(SEARCH_FRAGMENT);
				break;

			case CARMODE:
				SongListActivity.mService.setPause(true);
				showFragment(CARPLAYER_FRAGMENT);
				moreImg.setVisibility(View.GONE);
				break;

			case GOOD_APP:
			case EXIT_APP:
				return;
		}
//        menu.showContent();
	}


	public static void setButtonPlay() {
		if (mService.isPause()) {
			btnPlayFooter.setImageResource(R.drawable.bg_btn_play_small);
		} else {
			btnPlayFooter.setImageResource(R.drawable.bg_btn_pause_small);
		}
		((PlayerFragment) arrayFragments[PLAYER_FRAGMENT]).setButtonPlay();
		try {

			((CarPlayerFragment) arrayFragments[CARPLAYER_FRAGMENT]).setButtonPlay();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void cancelNotification(Context context) {
		NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nMgr.cancel(NOTIFICATION_ID);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnPreviousFooter:
				onClickPreviousFooter();
				break;

			case R.id.btnPlayFooter:
				onClickPlayFooter();
				break;

			case R.id.btnNextFooter:
				onClickNextFooter();
				break;

			case R.id.layoutPlayerFooter:
				onClickPlayerFooter();
				break;

//
		}
	}

	private void onClickPreviousFooter() {
		mService.backSongByOnClick();
	}

	private void onClickPlayFooter() {
		mService.playOrPauseMusic();
		setButtonPlay();
	}

	private void onClickNextFooter() {
		mService.nextSongByOnClick();
	}

	private void onClickPlayerFooter() {
		toMusicPlayer = FROM_OTHER;
		gotoFragment(PLAYER_FRAGMENT);
	}

//

	@Override
	public void onBackPressed() {
		switch (currentFragment) {
			case PLAYER_FRAGMENT:
				if (toMusicPlayer == FROM_SEARCH) {
					backFragment(SEARCH_FRAGMENT);
				} else {
					backFragment(LIST_SONG_FRAGMENT);
					setVisibilityFooter();
				}
				break;

			case LIST_SONG_FRAGMENT:
				if (GlobalValue.currentMenu == CATEGORY_MUSIC) {
					backFragment(CATEGORY_MUSIC_FRAGMENT);
				} else if (GlobalValue.currentMenu == PLAYLIST) {
					backFragment(PLAYLIST_FRAGMENT);
				} else {
					// quitApp();
					super.onBackPressed();
				}
				break;
			case CARPLAYER_FRAGMENT:

				showFragment(PLAYER_FRAGMENT);
				moreImg.setVisibility(View.VISIBLE);

				break;
			default:
				// quitApp();
				super.onBackPressed();
				break;
		}
//        }
	}

	private void quitApp() {
		// displayInterstitialAd();
		if (doubleBackToExitPressedOnce) {
			finish();
			stopService(intentService);
			cancelNotification(context);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, R.string.doubleBackToExit, Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

	private void showQuitDialog() {
		new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(R.string.msgQuitApp)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						stopService(intentService);
						cancelNotification(context);
						finish();
					}
				}).setNegativeButton(android.R.string.cancel, null).create().show();
	}


}

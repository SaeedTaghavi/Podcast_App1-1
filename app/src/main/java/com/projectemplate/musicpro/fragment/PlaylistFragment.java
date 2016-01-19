package com.projectemplate.musicpro.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.projectemplate.musicpro.activity.SongListActivity;
import com.projectemplate.musicpro.adapter.PlaylistAdapter;
import com.projectemplate.musicpro.config.GlobalValue;
import com.projectemplate.musicpro.config.WebserviceConfig;
import com.projectemplate.musicpro.object.Playlist;

import java.util.ArrayList;
import java.util.List;

import e.aakriti.work.objects.Song;
import e.aakriti.work.podcast_app.R;

public class PlaylistFragment extends android.support.v4.app.DialogFragment {
	private ListView lsvPlaylist;
	private List<Playlist> listPlaylists;
	private PlaylistAdapter playlistAdapter;
	private List<Song> listSongs;
	private String[] arraySongName;
	private String playlistName;
	private int currentUrl;
	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_playlist, container,
				false);
		view.findViewById(R.id.layoutCreatNewPlaylist).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						showDialogCreatNewPlaylist();
					}
				});

		listSongs = new ArrayList<Song>();

		lsvPlaylist = (ListView) view.findViewById(R.id.lsvPlaylist);
		listPlaylists = SongListActivity.databaseUtility.getAllPlaylist();
		if (listPlaylists != null && listPlaylists.size() > 0) {

			playlistAdapter = new PlaylistAdapter(getActivity(), listPlaylists);
			lsvPlaylist.setAdapter(playlistAdapter);
		}
		lsvPlaylist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
									long l) {

				SongListActivity.currentPlaylist = listPlaylists.get(position);
				SongListActivity.gotoFragment(SongListActivity.LIST_SONG_FRAGMENT);
			}
		});
//		initUIBase(view);
//		setButtonMenu(view);
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
//			getMainActivity().menu
//					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//			getMainActivity().setVisibilityFooter();
		}
	}


	private void showLoadingDialog() {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(getActivity(),
					getString(R.string.app_name),
					getString(R.string.getListSongs), true);
		} else {
			progressDialog.show();
		}
	}

	private void hideLoadingDialog() {
		if (progressDialog != null) {
			progressDialog.hide();
		}
	}

	private void showDialogCreatNewPlaylist() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.dialog_new_playlist,
				null);
		builder.setView(dialogView)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								EditText txtPlaylistName = (EditText) dialogView
										.findViewById(R.id.txtPlaylistName);
								playlistName = txtPlaylistName.getText()
										.toString();
								currentUrl = 0;
//								showLoadingDialog();
//								getSongToSearch(getUrl());
								Playlist playlist = new Playlist();
								int temp = 0;
								try {
									temp = Integer.parseInt(listPlaylists
											.get(listPlaylists.size() - 1)
											.getId());
								} catch (Exception e) {
								}
								playlist.setId((temp + 1) + "");
								playlist.setName(playlistName);
								Song currentSong = GlobalValue.getCurrentSong();


								playlist.addSong(currentSong);

								listPlaylists.add(playlist);
								SongListActivity.databaseUtility
										.insertPlaylist(playlist);
								playlistAdapter = new PlaylistAdapter(getActivity(), listPlaylists);
								lsvPlaylist.setAdapter(playlistAdapter);
							}
						}).setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}

	private void showListsongSelect() {
		final List<Integer> mSelectedItems = new ArrayList<Integer>();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.chooseSong)
				.setMultiChoiceItems(arraySongName, null,
						new DialogInterface.OnMultiChoiceClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which, boolean isChecked) {
								if (isChecked) {
									mSelectedItems.add(which);
								} else if (mSelectedItems.contains(which)) {
									mSelectedItems.remove(Integer
											.valueOf(which));
								}
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								if (mSelectedItems.size() > 0) {
									Playlist playlist = new Playlist();
									int temp = 0;
									try {
										temp = Integer.parseInt(listPlaylists
												.get(listPlaylists.size() - 1)
												.getId());
									} catch (Exception e) {
									}
									playlist.setId((temp + 1) + "");
									playlist.setName(playlistName);
									for (int integer : mSelectedItems) {
										playlist.addSong(listSongs.get(integer));
									}
									listPlaylists.add(playlist);
									playlistAdapter.notifyDataSetChanged();
									SongListActivity.databaseUtility
											.insertPlaylist(playlist);
								}
							}
						}).setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}

	private String getUrl() {

		if (GlobalValue.listCategoryMusics.size() - 1 < currentUrl) {
			return null;
		}
		return (WebserviceConfig.URL_MUSIC_WITH_TYPE
				+ GlobalValue.listCategoryMusics.get(currentUrl).getId() + ".xml");
	}

//	private void getSongToSearch(String url) {
//		getMainActivity().modelManager.getListSongs(url,
//				new ModelManagerListener() {
//					@SuppressWarnings("unchecked")
//					@Override
//					public void onSuccess(Object object) {
//						Object[] objects = (Object[]) object;
//						String nextPage = String.valueOf(objects[0]);
//						getMainActivity().listNominations
//								.addAll((List<Song>) objects[1]);
//						for (Song song : (List<Song>) objects[1]) {
//							addSongToListResult(song);
//						}
//
//						if (StringUtil.checkEndNextPage(nextPage)) {
//							currentUrl++;
//							if (currentUrl < GlobalValue.listCategoryMusics
//									.size()) {
//								getSongToSearch(getUrl());
//							} else {
//								arraySongName = new String[listSongs.size()];
//								for (int i = 0; i < arraySongName.length; i++) {
//									arraySongName[i] = listSongs.get(i)
//											.name
//											+ " - "
//											+ listSongs.get(i).singerName;
//								}
//								showListsongSelect();
//								hideLoadingDialog();
//							}
//						} else {
//							getSongToSearch(WebserviceConfig.URL_NEXT_PAGE
//									+ nextPage + ".xml");
//						}
//					}
//
//					@Override
//					public void onError() {
//						hideLoadingDialog();
//						Toast.makeText(getActivity(), "No song found", Toast.LENGTH_SHORT).show();
//					}
//				});
//	}

	private void addSongToListResult(Song song) {
		for (Song song2 : listSongs) {
			if (song.compare(song2)) {
				return;
			}
		}
		listSongs.add(song);
	}
}

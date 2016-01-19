package com.projectemplate.musicpro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectemplate.musicpro.config.GlobalValue;

import java.util.List;

import e.aakriti.work.imageloader.ImageLoader;
import e.aakriti.work.objects.Song;
import e.aakriti.work.podcast_app.R;

public class SongPlayingAdapter extends BaseAdapter {
	private final Context context;
	private List<Song> listSongs;
	private LayoutInflater layoutInflater;
	private int index;

	public SongPlayingAdapter(Context context, List<Song> listSongPlay) {
		this.listSongs = listSongPlay;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		index = -1;
		this.context = context;
	}

	public int getCount() {
		return listSongs.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_song_playing, null);
		}

		View layoutSong = convertView.findViewById(R.id.layoutSong);
		ImageView lbl_song_thumb = (ImageView) convertView.findViewById(R.id.lbl_song_thumb);
		TextView lblName = (TextView) convertView.findViewById(R.id.lblName);
		TextView lblArtist = (TextView) convertView.findViewById(R.id.lblArtist);

		Song item = listSongs.get(position);
		if (item != null) {
			lblName.setText(item.name);
			lblArtist.setText(item.singerName);
			new ImageLoader(context).DisplayImage(GlobalValue.getCurrentSong().image, lbl_song_thumb);
		}

		if (position == index) {
			layoutSong.setBackgroundResource(R.drawable.bg_current_song);
		} else {
			layoutSong.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}
}

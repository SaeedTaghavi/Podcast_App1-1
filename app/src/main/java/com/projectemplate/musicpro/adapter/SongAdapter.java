package com.projectemplate.musicpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import e.aakriti.work.imageloader.ImageLoader;
import e.aakriti.work.objects.Song;
import e.aakriti.work.podcast_app.R;

public class SongAdapter extends BaseAdapter {
	private List<Song> listSongs;
	private LayoutInflater layoutInflater;

	public SongAdapter(Context context, List<Song> listSongs2) {
		this.listSongs = listSongs2;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_song, null);
		}

		View layoutSong = convertView.findViewById(R.id.layoutSong);
		ImageView item_song_thumb = (ImageView) convertView.findViewById(R.id.item_song_thumb);
		TextView lblName = (TextView) convertView.findViewById(R.id.lblName);
		TextView lblArtist = (TextView) convertView.findViewById(R.id.lblArtist);

		Song item = listSongs.get(position);
		if (item != null) {
			lblName.setText(item.name);
			lblArtist.setText(item.singerName);
			new ImageLoader(convertView.getContext()).DisplayImage(item.image, item_song_thumb);
		}

//		if (position % 2 == 0) {
//			layoutSong.setBackgroundResource(R.drawable.bg_item_song);
//		} else {
//			layoutSong.setBackgroundColor(Color.TRANSPARENT);
//		}
		return convertView;
	}
}

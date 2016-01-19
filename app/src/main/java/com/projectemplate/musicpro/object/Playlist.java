package com.projectemplate.musicpro.object;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import e.aakriti.work.objects.Song;

public class Playlist {
	private String id;
	private String name;
	private List<Song> listSongs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Song> getListSongs() {
		if (listSongs == null) {
			return new ArrayList<Song>();
		}
		return listSongs;
	}

	public void setListSongs(List<Song> listSongs) {
		this.listSongs = listSongs;
	}

	public void setListSongs(String jsonSong) {
		if (listSongs == null) {
			listSongs = new ArrayList<Song>();
		}
		try {
			JSONArray array = new JSONArray(jsonSong);
			for (int i = 0; i < array.length(); i++) {
				listSongs.add(new Song(array.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void addSong(Song GetListVo) {
		if (listSongs == null) {
			listSongs = new ArrayList<Song>();
		}
		listSongs.add(GetListVo);
	}

	public String getJsonArraySong() {
		JSONArray array = new JSONArray();
		for (Song GetListVo : listSongs) {
			array.put(GetListVo.getJsonObject());
		}
		return array.toString();
	}
}

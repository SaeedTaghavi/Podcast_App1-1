package com.projectemplate.musicpro.object;

import java.util.ArrayList;
import java.util.List;

import e.aakriti.work.objects.Song;


public class CategoryMusic {
	private String id;
	private String title;
	private String image;
	private List<Song> listSongs;
	private String nextPage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public void addListSongs(List<Song> listSongs) {
		this.listSongs.addAll(listSongs);
	}

	public void addSong(Song GetListVo) {
		if (listSongs == null) {
			listSongs = new ArrayList<Song>();
		}
		listSongs.add(GetListVo);
	}

	public void clearSong() {
		if (listSongs == null) {
			listSongs = new ArrayList<Song>();
		} else {
			listSongs.clear();
		}
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
}

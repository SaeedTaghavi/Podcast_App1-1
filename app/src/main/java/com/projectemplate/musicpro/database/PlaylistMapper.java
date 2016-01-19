package com.projectemplate.musicpro.database;

import android.database.Cursor;

import com.projectemplate.musicpro.config.DatabaseConfig;
import com.projectemplate.musicpro.object.Playlist;

/**
 * Created by nimblechapps1 on 26/12/15.
 */
public class PlaylistMapper implements IRowMapper<Playlist> {
	@Override
	public Playlist mapRow(Cursor row, int rowNum) {
		Playlist song = new Playlist();
		song.setId(CursorParseUtility.getString(row, DatabaseConfig.KEY_ID));
		song.setName(CursorParseUtility.getString(row, DatabaseConfig.KEY_NAME));
		song.setListSongs(CursorParseUtility.getString(row, DatabaseConfig.KEY_LIST_SONG));
		return song;
	}
}

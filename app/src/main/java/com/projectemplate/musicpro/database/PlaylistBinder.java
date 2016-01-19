package com.projectemplate.musicpro.database;

import android.database.sqlite.SQLiteStatement;

import com.projectemplate.musicpro.object.Playlist;

/**
 * Created by nimblechapps1 on 26/12/15.
 */
public class PlaylistBinder implements ParameterBinder {
    public void bind(SQLiteStatement statement, Object object) {
        Playlist playlist = (Playlist) object;
        statement.bindString(1, playlist.getId());
        statement.bindString(2, playlist.getName());
        statement.bindString(3, playlist.getJsonArraySong());
    }
}

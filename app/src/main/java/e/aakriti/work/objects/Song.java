package e.aakriti.work.objects;

import com.projectemplate.musicpro.util.Logger;
import com.projectemplate.musicpro.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Song {

    private int position;
    private boolean isSelected;
    public String id;
    public String podcastId;
    public String number;
    public String name;
    public String nameVi;
    public String lyric;
    public String lyricsVi;
    public String sname;
    public String cname;
    public String pname;
    public String link;
    public String singerName;
    public String singerId;
    public String listen;
    public String albumId;
    public String authorId;
    public String categoryId;
    public String createDate;
    public String download;
    public String hot;
    public String _new;
    public String status;
    public String linkApp;
    public String orderNumber;
    public String image;
    public String desc;
    public String duration;
    public Song(JSONObject object) {

        Logger.e(object);
        try {
            id = object.getString("id");
            name = object.getString("name");
            link = object.getString("url");
            image = object.getString("image");
            singerName = object.getString("artist");
            position = object.getInt("position");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean checkNameAndArtist(String keyword) {
        String key = StringUtil.unAccent(keyword.toLowerCase());
        return StringUtil.unAccent(name.toLowerCase()).contains(key)
                || StringUtil.unAccent(singerName.toLowerCase()).contains(key);
    }

    public boolean compare(Song otherSong) {
        return otherSong.id.equals(id) && otherSong.name.equals(name) && otherSong.singerName.equals(singerName);
    }
    public JSONObject getJsonObject() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("id_type", "");
            object.put("name", name);
            object.put("url", link);
            object.put("image", image);
            object.put("artist", singerName);
            object.put("position", position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}

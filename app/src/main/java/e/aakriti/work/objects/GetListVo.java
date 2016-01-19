package e.aakriti.work.objects;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetListVo {


    public String status;
    public Integer allpage;
    public List<Song> data = new ArrayList<Song>();
    public String message;
    public Song song;


    public GetListVo(JSONObject jsonObject) {
        song = new Song(jsonObject);
    }

}




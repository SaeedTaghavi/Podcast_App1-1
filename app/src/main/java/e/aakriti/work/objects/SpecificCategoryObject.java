package e.aakriti.work.objects;

import org.json.JSONObject;

/**
 * Created by uadmin on 6/1/16.
 */
public class SpecificCategoryObject {
    public static final String CATID = "id";
    public static final String CATNAME = "cat_name";
    public static final String CATIMAGE = "show_img";
    public static final String CATNOEPISODES = "no_of_episode";
    public static final String TITLE = "title";
    public static final String CATCOVERIMAGE = "cover_page";
    public static final String DESCRIPTION = "description";


    private String id;
    private String catName;
    private String catImage;
    private Boolean isSelected;
    private String catTitle;
    private String catNoEpisodes;
    private String catCoverImage;
    private String description;

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public SpecificCategoryObject(JSONObject obj) {

        // TODO Auto-generated constructor stub
        setId(obj.optString(CATID));
        setImage(obj.optString(CATIMAGE));
        setName(obj.optString(CATNAME));
        setTitle(obj.optString(TITLE));
        setCatNoEpisodes(obj.optString(CATNOEPISODES));
        setCatcoverimage(obj.optString(CATCOVERIMAGE));
        setDescription(obj.optString(DESCRIPTION));
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return catName;
    }
    public void setName(String catName) {
        this.catName = catName;
    }

    public String getImage() {
        return catImage;
    }
    public void setImage(String catImage) {
        this.catImage = catImage;
    }

    public String getCatNoEpisodes(){
        return catNoEpisodes;
    }
    public void setCatNoEpisodes(String catNoEpisodes) {
        this.catNoEpisodes = catNoEpisodes;
    }

    public String getTitle(){
        return catTitle;
    }
    public void setTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public void setCatcoverimage(String catCoverImage){
        this.catCoverImage = catCoverImage;
    }
    public String getCatcoverImage(){
        return catCoverImage;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }

}
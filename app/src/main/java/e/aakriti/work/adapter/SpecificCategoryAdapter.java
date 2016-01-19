package e.aakriti.work.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import e.aakriti.work.imageloader.ImageLoader;
import e.aakriti.work.objects.SpecificCategoryObject;
import e.aakriti.work.podcast_app.R;

/**
 * Created by uadmin on 6/1/16.
 */
public class SpecificCategoryAdapter extends BaseAdapter{
    private Context context;
    private final List<SpecificCategoryObject> gridValues;
    private LayoutInflater mInflater;

    ViewHolder holder;
    String userId;

    //Constructor to initialize values
    public SpecificCategoryAdapter(Context context, List<SpecificCategoryObject> allCategories, String userId) {


        this.context        = context;
        this.gridValues     = allCategories;
        this.userId = userId;
    }

    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return gridValues.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(final int position, View convertView, ViewGroup parent) {



        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate( R.layout.category_row , null);
            holder = new ViewHolder();

            // set value into textview

            holder.tvCatTitle = (TextView) convertView.findViewById(R.id.catTitle);
            holder.imageView = (ImageView) convertView.findViewById(R.id.catImage);
            holder.tvCatEpi = (TextView) convertView.findViewById(R.id.catEpi);
            holder.tvCatName = (TextView) convertView.findViewById(R.id.catSec);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader image = new ImageLoader(context);
        //ImageLoaderOld imageLoaderOld = new ImageLoaderOld();


        image.DisplayImage(gridValues.get(position).getImage(), holder.imageView);
        //holder.imageView.setImageBitmap(imageLoaderOld.getImage(gridValues.get(position).getImage()));
        holder.tvCatTitle.setText(gridValues.get(position).getTitle());
        holder.tvCatName.setText(gridValues.get(position).getName());
        holder.tvCatEpi.setText("Episodes: "+gridValues.get(position).getCatNoEpisodes());

        /*holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromShow = new Intent(context, EpisodesPage.class);
                intentFromShow.putExtra("showId", gridValues.get(position).getId());
                // intentFromShow.putExtra("showCoverImage", gridValues.get(position).getCatcoverImage());
                intentFromShow.putExtra("showCoverImage", gridValues.get(position).getImage());
                intentFromShow.putExtra("showTitle", gridValues.get(position).getTitle());
                intentFromShow.putExtra("catName", gridValues.get(position).getName());
                intentFromShow.putExtra("showDescription", gridValues.get(position).getDescription());
                intentFromShow.putExtra("noOfEpisodes", gridValues.get(position).getCatNoEpisodes());
                intentFromShow.putExtra("userId", userId);
                context.startActivity(intentFromShow);
            }
        });*/

        return convertView;
    }


    class ViewHolder {
        public TextView tvCatTitle, tvCatName,tvCatEpi;
        public ImageView imageView;
    }
}
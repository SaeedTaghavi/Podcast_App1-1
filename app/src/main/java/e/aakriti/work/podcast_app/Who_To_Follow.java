package e.aakriti.work.podcast_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import e.aakriti.work.adapter.Explore_Episodesadapter;
import e.aakriti.work.adapter.Explore_showsAdapter;
import e.aakriti.work.objects.Explore_episodes;
import e.aakriti.work.objects.Explore_shows;

public class Who_To_Follow extends AppCompatActivity {
    TextView headerText;
    ImageView headerBackImage;
    View header;
    static Context context;

    public static ArrayList<Explore_episodes> explore_episodes;
    public static ArrayList<Explore_shows> explore_shows;

    static Explore_Episodesadapter episodesadapter;
    static Explore_showsAdapter showsadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who__to__follow);

        explore_episodes = new ArrayList<Explore_episodes>();
        explore_shows = new ArrayList<Explore_shows>();

        episodesadapter = new Explore_Episodesadapter(context);
        showsadapter = new Explore_showsAdapter(context);

        Fragment fragment = null;
        Bundle args = new Bundle();


        //***************for header title and back button***************
        //**************************************************************

        header = (View)findViewById(R.id.layout_header);
        headerText = (TextView)header.findViewById(R.id.titleTxt);
        headerBackImage = (ImageView) header.findViewById(R.id.menuImg);
        headerBackImage.setImageResource(R.drawable.back);
        headerText.setText("Who To Follow");
        headerBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       /* fragment = new ExploreFragment();
        //args.putInt(ExploreFragment.ARG_SECTION_NUMBER, position + 1);
        //fragment.setArguments(args);

//
        if (explore_episodes.size() == 0 && mViewPager.getCurrentItem() == 2) {
            //new getAllExploreEpisodesWS(ExploreFragment.gridView_episodes, context,
            //episodesadapter).execute();
        } else {
            fragment.gridView_episodes.setAdapter(episodesadapter);
            episodesadapter.notifyDataSetChanged();
        //}

        ExploreFragment.gridView_episodes.setColumnWidth(100);
        ExploreFragment.gridView_episodes.setNumColumns(2);

        /*if (explore_shows.size() == 0 && mViewPager.getCurrentItem() == 2) {
            //new getAllExploreShowsWS(ExploreFragment.gridView_shows, context, showsadapter)
            //.execute();
        } else {
            ExploreFragment.gridView_shows.setAdapter(showsadapter);
            showsadapter.notifyDataSetChanged();
        //}

        ExploreFragment.gridView_shows.setColumnWidth(100);
        ExploreFragment.gridView_shows.setNumColumns(2);

        ExploreFragment.toggleButton.setChecked(true);
        ExploreFragment.gridView_shows.setVisibility(View.VISIBLE);
        ExploreFragment.gridView_episodes.setVisibility(View.GONE);
        ExploreFragment.gridView_shows.bringToFront();
        ExploreFragment.gridView_episodes.invalidate();
        ExploreFragment.gridView_shows.invalidate();*/
    }
    public void onBackPressed()
    {
        finish();
    };

    /*public static class ExploreFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";
        static View view;
        static GridView gridView = null;
        static int numburOfScroll = 0;
        LinearLayout footerLoader;
        private int pageCount = 0;
        boolean isLoading = false;
        static ToggleButton toggleButton;
        static GridView gridView_episodes, gridView_shows;
        Explore_showsAdapter explore_showsAdapter;
        Explore_Episodesadapter explore_episodesadapter;

        public ExploreFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.explore, container, false);
            toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
            gridView_episodes = (GridView) view.findViewById(R.id.gridView_episodes);
            gridView_shows = (GridView) view.findViewById(R.id.gridView_shows);
            footerLoader = (LinearLayout) view.findViewById(R.id.footer);
            explore_showsAdapter = new Explore_showsAdapter(getContext());
            explore_episodesadapter = new Explore_Episodesadapter(getContext());
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        numburOfScroll = 0;
                        toggleButton.setText("IsChecked == Episodes");
                        gridView_episodes.setVisibility(View.GONE);
                        gridView_shows.setVisibility(View.VISIBLE);
                        gridView_shows.bringToFront();
                        new getAllExploreShowsWS(numburOfScroll,footerLoader,explore_shows, gridView_shows,getContext(), explore_showsAdapter).execute();
                        explore_showsAdapter.notifyDataSetChanged();
                        gridView_shows.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                            }
                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                int lastIndexIntScreen = visibleItemCount + firstVisibleItem;
                                if (lastIndexIntScreen >= totalItemCount && !isLoading) {
                                    isLoading = true;
                                    new getAllExploreShowsWS(numburOfScroll,footerLoader,explore_shows, gridView_shows,getContext(), explore_showsAdapter).execute();
                                    explore_showsAdapter.notifyDataSetChanged();
                                    gridView_episodes.invalidate();
                                    numburOfScroll++;
                                } else {
                                    isLoading = false;
                                }
                            }
                        });
                    } else {
                        numburOfScroll = 0;
                        toggleButton.setText("IsChecked == Shows");
                        gridView_shows.setVisibility(View.GONE);
                        gridView_episodes.setVisibility(View.VISIBLE);
                        gridView_episodes.bringToFront();
                        new getAllExploreEpisodesWS(numburOfScroll, footerLoader, explore_episodes, gridView_episodes, getContext(), explore_episodesadapter).execute();
                        explore_episodesadapter.notifyDataSetChanged();
                        gridView_episodes.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {

                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                int lastIndexIntScreen = visibleItemCount + firstVisibleItem;
                                if (lastIndexIntScreen >= totalItemCount && !isLoading) {
                                    isLoading = true;
                                    new getAllExploreEpisodesWS(numburOfScroll, footerLoader, explore_episodes, gridView_episodes, getContext(), explore_episodesadapter).execute();
                                    explore_episodesadapter.notifyDataSetChanged();
                                    numburOfScroll++;
                                } else {
                                    isLoading = false;
                                }

                            }
                        });
                    }
                }
            });


            return view;
        }

    }*/
}

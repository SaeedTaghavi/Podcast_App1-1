package com.projectemplate.musicpro.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.projectemplate.musicpro.BaseFragment;
import com.projectemplate.musicpro.activity.SongListActivity;
import com.projectemplate.musicpro.config.GlobalValue;

import java.io.File;

import e.aakriti.work.imageloader.ImageLoader;
import e.aakriti.work.podcast_app.R;

public class CarPlayerFragment extends BaseFragment implements OnClickListener {
    public static final int LIST_PLAYING = 0;
    public static final int THUMB_PLAYING = 1;

    private ImageView  btnPlaycar;
    private ViewPager viewPagercar;
    private SeekBar seekBarLengthcar;
    private TextView lblTimeCurrentcar, lblTimeLengthcar;
    private View viewIndicatorListcar, viewIndicatorThumbcar;

    private String rootFolder;

    public PlayerListPlayingFragment playerListPlayingFragment;
    public PlayerThumbFragment playerThumbFragment;
    private ImageView btnBackwardcar,btnForwardcar;
    private ImageView player_thumbcar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_player, container, false);
        try {
            initUIBase(view);
            initControl(view);
        } catch (Exception e) {
            e.printStackTrace();
            SongListActivity.cancelNotification(getActivity());
        }
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//			SongListActivity.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            switch (SongListActivity.toMusicPlayer) {
                case SongListActivity.FROM_LIST_SONG:
                case SongListActivity.FROM_SEARCH:
                    if ( GlobalValue.listSongPlay != null) {
                        SongListActivity.mService.setListSongs(GlobalValue.listSongPlay);}
                    try {

                        setCurrentSong(GlobalValue.currentSongPlay);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    try {

                        playerListPlayingFragment.refreshListPlaying();
                        playerThumbFragment.refreshData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setSelectTab(THUMB_PLAYING);
                    viewPagercar.setCurrentItem(THUMB_PLAYING);
                    SongListActivity.setButtonPlay();
                    break;

                case SongListActivity.FROM_NOTICATION:
                    try {

                        playerListPlayingFragment.refreshListPlaying();
                        playerThumbFragment.refreshData();
                    } catch (Exception e) {
                        SongListActivity.cancelNotification(getActivity());e.printStackTrace();
                    }
                    break;

                case SongListActivity.FROM_OTHER:
                    break;
            }
        }
    }

    @Override
    protected void initUIBase(View view) {
        viewIndicatorListcar = view.findViewById(R.id.viewIndicatorListcar);
        viewIndicatorThumbcar = view.findViewById(R.id.viewIndicatorThumbcar);
        player_thumbcar = (ImageView) view.findViewById(R.id.player_thumbcar);
        viewPagercar = (ViewPager) view.findViewById(R.id.viewPagercar);
        btnBackwardcar = (ImageView) view.findViewById(R.id.btnBackwardcar);
        btnForwardcar = (ImageView) view.findViewById(R.id.btnForwardcar);
        seekBarLengthcar = (SeekBar) view.findViewById(R.id.seekBarLengthcar);
        lblTimeCurrentcar = (TextView) view.findViewById(R.id.lblTimeCurrentcar);
        lblTimeLengthcar = (TextView) view.findViewById(R.id.lblTimeLengthcar);
        view.findViewById(R.id.btnrevursecar).setOnClickListener(this);
        view.findViewById(R.id.btnforwordcar).setOnClickListener(this);
        btnPlaycar = (ImageView) view.findViewById(R.id.btnPlaycar);


    }

    private void initControl(View view) {
        rootFolder = Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/";
        File folder = new File(rootFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        btnBackwardcar.setOnClickListener(this);
        btnPlaycar.setOnClickListener(this);
        btnForwardcar.setOnClickListener(this);

        // songAdapter = new SongPlayingAdapter(getActivity(),
        // GlobalValue.listSongPlay);
        // lsvSong.setAdapter(songAdapter);
        // lsvSong.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> av, View v, int position, long
        // l) {
        // if (position != GlobalValue.currentSongPlay) {
        // setCurrentSong(position);
        // }
        // }
        // });

        seekBarLengthcar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SongListActivity.mService.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });

        playerListPlayingFragment = new PlayerListPlayingFragment();
        playerThumbFragment = new PlayerThumbFragment();
        playerThumbFragment.setArguments(new Bundle());
        viewPagercar.setAdapter(new MyFragmentPagerAdapter(getFragmentManager()));
        viewPagercar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setSelectTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        playerListPlayingFragment.refreshListPlaying();
        playerThumbFragment.refreshData();

        setSelectTab(THUMB_PLAYING);
        viewPagercar.setCurrentItem(THUMB_PLAYING);
        SongListActivity.setButtonPlay();
    }

    private void setSelectTab(int tab) {
        if (tab == LIST_PLAYING) {
            viewIndicatorListcar.setBackgroundResource(R.drawable.indicator_cyan);
            viewIndicatorThumbcar.setBackgroundResource(R.drawable.indicator_white);
        } else {
            viewIndicatorListcar.setBackgroundResource(R.drawable.indicator_white);
            viewIndicatorThumbcar.setBackgroundResource(R.drawable.indicator_cyan);
        }
    }

    private void setCurrentSong(int position) {
        playerListPlayingFragment.refreshListPlaying();
        playerThumbFragment.refreshData();
        SongListActivity.mService.startMusic(position);
    }

    public void seekChanged(String lengthTime, String currentTime, int progress) {
        lblTimeLengthcar.setText(lengthTime);
        lblTimeCurrentcar.setText(currentTime);
        seekBarLengthcar.setProgress(progress);
    }

    public void changeSong(int indexSong) {
        lblTimeCurrentcar.setText(getString(R.string.timeStart));
        lblTimeLengthcar.setText(SongListActivity.mService.getLengSong());
        playerListPlayingFragment.refreshListPlaying();
        playerThumbFragment.refreshData();
        new ImageLoader(getActivity()).DisplayImage(GlobalValue.getCurrentSong().image, player_thumbcar);
    }

    public void setButtonPlay() {
        if (SongListActivity.mService.isPause()) {
            btnPlaycar.setBackgroundResource(R.drawable.play);
        } else {
            btnPlaycar.setBackgroundResource(R.drawable.pause);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnrevursecar:
                onClickrevurse();
                break;
            case R.id.btnforwordcar:
                onClickforwd();
                break;

            case R.id.btnBackwardcar:
                onClickBackward();
                break;

            case R.id.btnPlaycar:
                onClickPlay();
                break;

            case R.id.btnForwardcar:
                onClickForward();
                break;

        }
    }

    private void onClickforwd() {
        SongListActivity.mService.forwdSong15ByOnClick();
    }

    private void onClickrevurse() {
        SongListActivity.mService.backSong15ByOnClick();
    }


    private void onClickBackward() {
        SongListActivity.mService.backSongByOnClick();
    }

    private void onClickPlay() {
        SongListActivity.mService.playOrPauseMusic();
        SongListActivity.setButtonPlay();
    }

    private void onClickForward() {
        SongListActivity.mService.nextSongByOnClick();
    }

//	private void onClickRepeat() {
//		SongListActivity.mService.setRepeat(btnRepeatcar.isChecked());
//		if (SongListActivity.mService.isRepeat()) {
//			showToast(R.string.enableRepeat);
//		} else {
//			showToast(R.string.offRepeat);
//		}
//	}

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return playerListPlayingFragment;
            }
            return playerThumbFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}

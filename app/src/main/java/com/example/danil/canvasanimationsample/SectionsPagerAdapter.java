package com.example.danil.canvasanimationsample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.danil.canvasanimationsample.labyrinth.LabirinthFragment;
import com.example.danil.canvasanimationsample.path.PathFragment;
import com.example.danil.canvasanimationsample.water.WaterFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return WaterFragment.newInstance();
                case 1:
                    return PathFragment.newInstance();
                case 2:
                    return LabirinthFragment.newInstance();
            }
            return WaterFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Water";
                case 1:
                    return "Path";
                case 2:
                    return "Labirinth";
            }
            return null;
        }
    }
package com.echowaves.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by dmitry on 9/5/14.
 *
 */
public class DetailedImagePagerActivity extends EWFragmentActivity {

    String[] imageNames;
    String[] waveNames;
    int position;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        imageNames = extras.getStringArray("imageNames");
        waveNames = extras.getStringArray("waveNames");
        position = extras.getInt("position");

        setContentView(R.layout.activity_detailed_image_page);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.detailedimagepage_pager);


        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, imageNames, waveNames);

        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
//        if (mPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
        finish();
    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        Context context;
        String[] imagesNames;
        String[] waveNames;

        public ViewPagerAdapter(FragmentManager fm, Context context, String[] imagesNames, String[] waveNames) {
            super(fm);
            this.context = context;
            this.imagesNames = imagesNames;
            this.waveNames = waveNames;
        }

        @Override
        public int getCount() {
            return imagesNames.length;
        }

        @Override
        public Fragment getItem(int position) {
            DetailedImageFragment detailedImageFragment = new DetailedImageFragment();
            Bundle args = new Bundle();
            args.putString("imageName", imagesNames[position]);
            args.putString("waveName", waveNames[position]);
            detailedImageFragment.setArguments(args);
            return detailedImageFragment;
        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            // Remove viewpager_item.xml from ViewPager
//            container.removeView((DetailedImageFragment) object);
//        }
    }


}

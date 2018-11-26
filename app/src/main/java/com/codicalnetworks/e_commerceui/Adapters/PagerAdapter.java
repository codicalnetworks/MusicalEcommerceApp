package com.codicalnetworks.e_commerceui.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codicalnetworks.e_commerceui.Fragments.AllFragment;
import com.codicalnetworks.e_commerceui.Fragments.DrumFragment;
import com.codicalnetworks.e_commerceui.Fragments.GuitarFragment;
import com.codicalnetworks.e_commerceui.Fragments.MicrophonesFragment;
import com.codicalnetworks.e_commerceui.Fragments.MixerFragment;
import com.codicalnetworks.e_commerceui.Fragments.MonitorFragment;
import com.codicalnetworks.e_commerceui.Fragments.PasystemFragment;
import com.codicalnetworks.e_commerceui.Fragments.PianoFragment;
import com.codicalnetworks.e_commerceui.Fragments.SpeakerFragment;
import com.codicalnetworks.e_commerceui.Fragments.WooferFragment;

/**
 * Created by ADETOBA on 10/25/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllFragment allFragment = new AllFragment();
                return allFragment;
            case 1:
                MicrophonesFragment microphonesFragment = new MicrophonesFragment();
                return microphonesFragment;
            case 2:
                GuitarFragment guitarFragment = new GuitarFragment();
                return guitarFragment;
            case 3:
                SpeakerFragment speakerFragment = new SpeakerFragment();
                return speakerFragment;
            case 4:
                MixerFragment mixerFragment = new MixerFragment();
                return mixerFragment;
            case 5:
                WooferFragment wooferFragment = new WooferFragment();
                return wooferFragment;
            case 6:
                PianoFragment pianoFragment = new PianoFragment();
                return pianoFragment;
            case 7:
                PasystemFragment pasystemFragment = new PasystemFragment();
                return pasystemFragment;
            case 8:
                MonitorFragment monitorFragment = new MonitorFragment();
                return monitorFragment;
            case 9:
                DrumFragment drumFragment = new DrumFragment();
                return drumFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "All";
            case 1:
                return "Microphones";
            case 2:
                return "Guitars";
            case 3:
                return "Speakers";
            case 4:
                return "Mixers";
            case 5:
                return "Woofers";
            case 6:
                return "Pianos";
            case 7:
                return "PA system";
            case 8:
                return "Monitor";
            case 9:
                return "Drum";
        }
        return super.getPageTitle(position);
    }
}

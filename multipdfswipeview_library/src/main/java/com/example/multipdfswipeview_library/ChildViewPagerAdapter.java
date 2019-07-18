package com.example.multipdfswipeview_library;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ChildViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<PDFConfig> dataModels;
    private int mPdfType;

    ChildViewPagerAdapter(FragmentManager fm, ArrayList<PDFConfig> dataModels, int mPdfType) {
        super(fm);
        this.dataModels = dataModels;
        this.mPdfType = mPdfType;
    }

    @Override
    public Fragment getItem(int position) {
        return ChildFragment.newInstance(dataModels.get(position),mPdfType);
    }

    @Override
    public int getCount() {
        return dataModels.size();
    }
}
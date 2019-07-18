package com.example.multipdfswipeview_library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

public class PDFMultiSwipeView extends LinearLayout {

    public static int TYPE_ASSET = 0;
    public static int TYPE_OFFLINE = 1;
    public static int TYPE_ONLINE = 2;

    public PDFMultiSwipeView(Context context) {
        super(context);
    }

    public PDFMultiSwipeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PDFMultiSwipeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PDFMultiSwipeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private ArrayList<PDFConfig> config;
    private int mPdfType;
    private ChildViewPagerAdapter verticalPagerAdapter;
    private ViewPager verticalViewPager;
    private AppCompatActivity activity;

    public PDFMultiSwipeView with(AppCompatActivity activity){
        inflate(getContext(), R.layout.activity_pdf, this);
        this.activity = activity;
        verticalViewPager = findViewById(R.id.pdfviewfpager);

        return this;
    }

    public void build(ArrayList<PDFConfig> config, int mPdfType) {
        this.config = config;
        this.mPdfType = mPdfType;
        checkLocalFiles(config);
        setUpViewPager();
    }


    private void checkLocalFiles(ArrayList<PDFConfig> configList) {
        for (int i = 0; i < configList.size(); i++) {
            String fileName = "PDF_" + configList.get(i).getName() + "_" + configList.get(i).getId();
            File file = new File(getContext().getCacheDir(), fileName);

            if (file.exists()) {
                configList.get(i).setPdfLocalpath(file.getAbsolutePath());
            } else {
                configList.get(i).setPdfLocalpath("");
            }
        }
    }

    private void setUpViewPager() {
        verticalPagerAdapter = new ChildViewPagerAdapter(activity.getSupportFragmentManager(), config,mPdfType);
        verticalViewPager = findViewById(R.id.container);
        verticalViewPager.setAdapter(verticalPagerAdapter);


        verticalViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((ToggleVerticalViewPagerScrolling) verticalPagerAdapter.getItem(position)).trigger(position);
            }

            @Override
            public void onPageSelected(int position) {
//                Log.d("PDFView", "onPageSelected " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

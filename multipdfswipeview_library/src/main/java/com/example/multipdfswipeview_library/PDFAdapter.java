package com.example.multipdfswipeview_library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import org.jetbrains.annotations.NotNull;

public class PDFAdapter extends PagerAdapter {

    private int page_count;
    private Context context;
    private IShowPage listener;


    public PDFAdapter(Context context, IShowPage listener, int page_count) {
        this.context = context;
        this.listener = listener;
        this.page_count = page_count;
    }

    @Override
    public int getCount() {
        return page_count;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.each_page, container, false);

        PhotoView imageView = (PhotoView) itemView.findViewById(R.id.photoView);

        imageView.setImageDrawable(listener.showPage(position));

        container.addView(itemView);

        return itemView;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

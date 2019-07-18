package com.example.multipdfswipeview_library;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.*;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.multipdfswipeview_library.PDFMultiSwipeView.*;


public class ChildFragment extends Fragment implements IShowPage, ToggleVerticalViewPagerScrolling {

    private VerticalViewPager pdfviewpager;

    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mCurrentPage;
    private int mPageIndex;
    private int mPdfType;
    private PDFConfig config;

    public static ChildFragment newInstance(PDFConfig path, int mPdfType) {
        ChildFragment fragment = new ChildFragment();
        Bundle args = new Bundle();
        args.putParcelable("path", path);
        args.putInt("pdfType", mPdfType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child, container, false);

        if (getArguments() != null) {
            config = getArguments().getParcelable("path");
            mPdfType = getArguments().getInt("pdfType", 0);
        }

        pdfviewpager = rootView.findViewById(R.id.pdfviewfpager);

        mPageIndex = 0;
        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }

        //render the pdf view
        try {
            if (mPdfType == TYPE_ASSET) {
                File file = getFile(config.getAssestFileName());
                openRenderer(file.getAbsolutePath());
            } else if (mPdfType == TYPE_OFFLINE) {
                openPdfFromLocal();
            } else if (mPdfType == TYPE_ONLINE) {
                initDownload();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(getActivity(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return rootView;
    }

    private void openPdfFromLocal() throws IOException {
        openRenderer(config.getPdfLocalpath());
    }

    private void setUpViewPager() {
        PDFAdapter adapter = new PDFAdapter(getActivity(), this, mPdfRenderer.getPageCount());
        pdfviewpager.setAdapter(adapter);
        pdfviewpager.setCurrentItem(mPageIndex);
    }


    private void openRenderer(String path) throws IOException {
        File file = new File(path);
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }

    }

    @Override
    public void onDestroy() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private void closeRenderer() throws IOException {
        if (null != mCurrentPage)
            mCurrentPage.close();

        if (null != mPdfRenderer)
            mPdfRenderer.close();

        if (null != mFileDescriptor)
            mFileDescriptor.close();
    }

    public Drawable showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index) {
            return null;
        }
        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        Drawable d = new BitmapDrawable(getResources(), bitmap);

        return d;
    }

    @Override
    public void trigger(int page) {
        if (pdfviewpager != null) {
            if (page == 1) {
                pdfviewpager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            } else {
                pdfviewpager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        }
    }


    private void initDownload() throws IOException {
        if (config.getPdfLocalpath() == null || config.getPdfLocalpath().trim().length() == 0) {

            RetrofitInterface retrofitInterface = getRetrofitClient().create(RetrofitInterface.class);

            Call<ResponseBody> request = retrofitInterface.downloadFile(config.getPdfUrl());
            request.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        ResponseBody body = response.body();

                        InputStream iStream = new BufferedInputStream(body.byteStream(), 1024 * 8);

                        String fileName = "PDF_" + config.getName() + "_" + config.getId();
                        File file = new File(Objects.requireNonNull(getContext()).getCacheDir(), fileName);

                        FileOutputStream output = null;
                        output = new FileOutputStream(file);
                        final byte[] buffer = new byte[1024];
                        int size;
                        while ((size = iStream.read(buffer)) != -1) {
                            output.write(buffer, 0, size);
                        }
                        iStream.close();
                        output.close();

                        config.setPdfLocalpath(file.getAbsolutePath());
                        openRenderer(config.getPdfLocalpath());
                        setUpViewPager();

                    } catch (IOException e) {

                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("testing");

                }
            });
        } else {
            openRenderer(config.getPdfLocalpath());
            setUpViewPager();
        }
    }

    @NotNull
    private Retrofit getRetrofitClient() {
        OkHttpClient.Builder builder;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
            builder.addInterceptor(interceptor);

        return new Retrofit.Builder()
                .baseUrl("https://www.google.com/")
                .client(builder.build())
                .build();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 10:
                // Get the Uri of the selected file
                if (resultCode == RESULT_OK) {

                    if (null != data.getData()) {

                        Uri uri = data.getData();
                        File file;

                        if (uri.getScheme().equals("content")) {

                            file = new File(getContext().getCacheDir(), data.getData().getLastPathSegment());

                            try {
                                InputStream iStream = getContext().getContentResolver().openInputStream(uri);
                                FileOutputStream output = null;
                                output = new FileOutputStream(file);
                                final byte[] buffer = new byte[1024];
                                int size;
                                while ((size = iStream.read(buffer)) != -1) {
                                    output.write(buffer, 0, size);
                                }
                                iStream.close();
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            file = new File(uri.getPath());
                        }

                        try {
                            openRenderer(file.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    @NotNull
    private File getFile(String FILENAME) {
        File file = new File(getContext().getCacheDir(), FILENAME);
        if (!file.exists()) {

            try {
                InputStream asset = getContext().getAssets().open(FILENAME);
                FileOutputStream output = null;
                output = new FileOutputStream(file);
                final byte[] buffer = new byte[1024];
                int size;
                while ((size = asset.read(buffer)) != -1) {
                    output.write(buffer, 0, size);
                }
                asset.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return file;
    }

    private void Pickpdfstorage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, 10);
    }
}
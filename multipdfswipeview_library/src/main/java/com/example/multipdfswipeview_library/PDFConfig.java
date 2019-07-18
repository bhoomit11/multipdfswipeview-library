package com.example.multipdfswipeview_library;

import android.os.Parcel;
import android.os.Parcelable;


public class PDFConfig implements Parcelable {

    static final String EXTRA_CONFIG = "PDFConfig";

    private String id;
    private String name;
    private String pdfUrl;
    private String pdfLocalpath;
    private String assestFileName;

    public PDFConfig() {
    }

    private PDFConfig(Parcel in) {
        id = in.readString();
        name = in.readString();
        pdfUrl = in.readString();
        pdfLocalpath = in.readString();
        assestFileName = in.readString();
    }

    public static final Creator<PDFConfig> CREATOR = new Creator<PDFConfig>() {
        @Override
        public PDFConfig createFromParcel(Parcel in) {
            return new PDFConfig(in);
        }

        @Override
        public PDFConfig[] newArray(int size) {
            return new PDFConfig[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    String getPdfLocalpath() {
        return pdfLocalpath;
    }

    void setPdfLocalpath(String pdfLocalpath) {
        this.pdfLocalpath = pdfLocalpath;
    }

    public String getAssestFileName() {
        return assestFileName;
    }

    public void setAssestFileName(String assestFileName) {
        this.assestFileName = assestFileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(pdfUrl);
        dest.writeString(pdfLocalpath);
        dest.writeString(assestFileName);
    }
}

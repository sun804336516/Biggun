package com.biggun.drawbitmapmesh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

/**
 * Created by 孙贤武 on 2016/1/14.
 */
public class BrowserBean implements Comparable<BrowserBean>,Parcelable
{
    private String text;
    private int mapId;
    private boolean isfile;

    public BrowserBean()
    {
    }

    public BrowserBean(String text, int mapId, boolean isfile)
    {
        this.text = text;
        this.mapId = mapId;
        this.isfile = isfile;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setMapId(int mapId)
    {
        this.mapId = mapId;
    }

    public String getText()
    {
        return text;
    }

    public int getMapId()
    {
        return mapId;
    }

    public boolean isfile()
    {
        return isfile;
    }

    public void setIsfile(boolean isfile)
    {
        this.isfile = isfile;
    }

    @Override
    public int compareTo(BrowserBean another)
    {
//        int  i = 0;
//        if((!isfile && another.isfile) ||(isfile&&another.isfile) )
//        {
//            i = text.compareTo(another.text);
//        }else{
//            if(!isfile)
//            {
//                i = 1;
//            }
//            if(!another.isfile)
//            {
//                i = 1;
//            }
//        }

        return text.compareTo(another.text);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.text);
        dest.writeInt(this.mapId);
        dest.writeByte(isfile ? (byte) 1 : (byte) 0);
    }

    protected BrowserBean(Parcel in)
    {
        this.text = in.readString();
        this.mapId = in.readInt();
        this.isfile = in.readByte() != 0;
    }

    public static final Parcelable.Creator<BrowserBean> CREATOR = new Parcelable.Creator<BrowserBean>()
    {
        public BrowserBean createFromParcel(Parcel source)
        {
            return new BrowserBean(source);
        }

        public BrowserBean[] newArray(int size)
        {
            return new BrowserBean[size];
        }
    };

    @Override
    public String toString()
    {
        return "text:"+text+",mapId:"+mapId+",isFile:"+isfile;
    }
}

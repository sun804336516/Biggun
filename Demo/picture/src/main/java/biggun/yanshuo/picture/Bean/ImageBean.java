package biggun.yanshuo.picture.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：Administrator on 2016/6/12 14:20
 * 邮箱：sun91985415@163.com
 */
public class ImageBean implements Parcelable, Comparable<ImageBean>
{
    String name;
    long size;
    String dirName;
    long time;
    boolean islongClick = false;

    public boolean islongClick()
    {
        return islongClick;
    }

    public void setIslongClick(boolean islongClick)
    {
        this.islongClick = islongClick;
    }

    public ImageBean()
    {
    }

    public ImageBean(String name, long size, String dirName, long time)
    {
        this.name = name;
        this.size = size;
        this.dirName = dirName;
        this.time = time;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public String getName()
    {
        return name;
    }

    public long getSize()
    {
        return size;
    }

    public String getDirname()
    {
        return dirName;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public void setDirName(String dirName)
    {
        this.dirName = dirName;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.name);
        dest.writeLong(this.size);
        dest.writeString(this.dirName);
    }

    protected ImageBean(Parcel in)
    {
        this.name = in.readString();
        this.size = in.readLong();
        this.dirName = in.readString();
    }

    public static final Parcelable.Creator<ImageBean> CREATOR = new Parcelable.Creator<ImageBean>()
    {
        @Override
        public ImageBean createFromParcel(Parcel source)
        {
            return new ImageBean(source);
        }

        @Override
        public ImageBean[] newArray(int size)
        {
            return new ImageBean[size];
        }
    };

    @Override
    public int compareTo(ImageBean another)
    {
        if (time > another.getTime()) {
            return -1;
        } else if (time < another.getTime()) {
            return 1;
        } else {
            return 0;
        }
    }
}

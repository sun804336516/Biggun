package biggun.yanshuo.picture.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：孙贤武 on 2016/6/29 14:24
 * 邮箱：sun91985415@163.com
 * LIKE:YANSHUO
 */
public class ImageDirectory2 implements Parcelable,Comparable<ImageDirectory2>
{
    private String name;
    private String parentPath;
    private String representPath;

    private ImageDirectory2()
    {
    }

    public String getName()
    {
        return name;
    }

    public String getParentPath()
    {
        return parentPath;
    }

    public String getRepresentPath()
    {
        return representPath;
    }

    @Override
    public int compareTo(ImageDirectory2 another)
    {
        return this.getParentPath().compareTo(another.getParentPath());
    }

    public static final class Builder
    {
        ImageDirectory2 mImageDirectory2;
        public Builder()
        {
            mImageDirectory2 = new ImageDirectory2();
        }

        public Builder setName(String name)
        {
            mImageDirectory2.name = name;
            return this;
        }

        public Builder setParentPath(String parentPath)
        {
            mImageDirectory2.parentPath = parentPath;
            return this;
        }

        public Builder setRepresentPath(String representPath)
        {
            mImageDirectory2.representPath = representPath;
            return this;
        }
        public ImageDirectory2 builde()
        {
            return mImageDirectory2;
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof ImageDirectory2)
        {
            return this.getParentPath().equals(((ImageDirectory2) o).getParentPath());
        }
        return false;
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
        dest.writeString(this.parentPath);
        dest.writeString(this.representPath);
    }

    protected ImageDirectory2(Parcel in)
    {
        this.name = in.readString();
        this.parentPath = in.readString();
        this.representPath = in.readString();
    }

    public static final Parcelable.Creator<ImageDirectory2> CREATOR = new Parcelable.Creator<ImageDirectory2>()
    {
        @Override
        public ImageDirectory2 createFromParcel(Parcel source)
        {
            return new ImageDirectory2(source);
        }

        @Override
        public ImageDirectory2[] newArray(int size)
        {
            return new ImageDirectory2[size];
        }
    };
}

package biggun.yanshuo.picture.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：Administrator on 2016/6/12 15:07
 * 邮箱：sun91985415@163.com
 */
public class ImageDirectory implements Comparable<ImageDirectory>, Parcelable
{
    private String name;
    private String parentPath;
    private String representPath;

    public ImageDirectory(String name, String parentPath, String representPath)
    {
        this.name = name;
        this.parentPath = parentPath;
        this.representPath = representPath;
    }



    public String getRepresentPath()
    {
        return representPath;
    }

    public void setRepresentPath(String representPath)
    {
        this.representPath = representPath;
    }

    public String getName()
    {
        return name;
    }

    public String getParentPath()
    {
        return parentPath;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setParentPath(String parentPath)
    {
        this.parentPath = parentPath;
    }

    @Override
    public int compareTo(ImageDirectory another)
    {
        return parentPath.compareTo(another.getParentPath());
    }

    @Override
    public String toString()
    {
        return "ImageDirectory{" +
                "name='" + name + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", representPath='" + representPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ImageDirectory) {
            return parentPath.equals(((ImageDirectory) o).getParentPath());
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

    protected ImageDirectory(Parcel in)
    {
        this.name = in.readString();
        this.parentPath = in.readString();
        this.representPath = in.readString();
    }

    public static final Parcelable.Creator<ImageDirectory> CREATOR = new Parcelable.Creator<ImageDirectory>()
    {
        @Override
        public ImageDirectory createFromParcel(Parcel source)
        {
            return new ImageDirectory(source);
        }

        @Override
        public ImageDirectory[] newArray(int size)
        {
            return new ImageDirectory[size];
        }
    };
}

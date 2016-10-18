package biggun.yanshuo.picture.Bean;

/**
 * 作者：Administrator on 2016/6/13 17:02
 * 邮箱：sun91985415@163.com
 */
public class FileBean
{
    String title;
    String name;
    long size;
    String path;
    private FileBean()
    {
    }

    public String getTitle()
    {
        return title;
    }

    public String getName()
    {
        return name;
    }

    public long getSize()
    {
        return size;
    }

    public String getPath()
    {
        return path;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof FileBean) {
            return this.path.equals(((FileBean) o).getPath());
        }
        return super.equals(o);
    }

    @Override
    public String toString()
    {
        return "FileBean{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", di='" + path + '\'' +
                '}';
    }

    public static final class Builder
    {
        FileBean mFileBean;
        public Builder()
        {
            mFileBean = new FileBean();
        }
        public Builder setTitle(String title)
        {
            mFileBean.title = title;
            return this;
        }
        public Builder setName(String name)
        {
            mFileBean.name = name;
            return this;
        }
        public Builder setSize(long size)
        {
            mFileBean.size = size;
            return this;
        }
        public Builder setPath(String path)
        {
            mFileBean.path = path;
            return this;
        }
        public FileBean Build()
        {
            return mFileBean;
        }
    }
}

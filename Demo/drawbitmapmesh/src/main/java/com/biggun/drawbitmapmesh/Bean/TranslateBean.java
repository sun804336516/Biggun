package com.biggun.drawbitmapmesh.Bean;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/3/28 16:37
 * 邮箱：sun91985415@163.com
 */
public class TranslateBean
{

    /**
     * errNum : 0
     * errMsg : success
     * retData : {"from":"en","to":"zh","trans_result":[{"src":"I am chinese,and you?","dst":"我是中国人，你呢？"}]}
     */

    private int errNum;
    private String errMsg;
    private RetDataEntity retData;

    public void setErrNum(int errNum)
    {
        this.errNum = errNum;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public void setRetData(RetDataEntity retData)
    {
        this.retData = retData;
    }

    public int getErrNum()
    {
        return errNum;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public RetDataEntity getRetData()
    {
        return retData;
    }

    @Override
    public String toString()
    {
        return "errnum:" + errNum + "\nerrmsg:" + errMsg + "\n" + (retData == null ? "null" : retData.toString());
    }

    public class RetDataEntity
    {
        /**
         * from : en
         * to : zh
         * trans_result : [{"src":"I am chinese,and you?","dst":"我是中国人，你呢？"}]
         */

        private String from;
        private String to;
        private List<TransResultEntity> trans_result;

        public void setFrom(String from)
        {
            this.from = from;
        }

        public void setTo(String to)
        {
            this.to = to;
        }

        public void setTrans_result(List<TransResultEntity> trans_result)
        {
            this.trans_result = trans_result;
        }

        public String getFrom()
        {
            return from;
        }

        public String getTo()
        {
            return to;
        }

        public List<TransResultEntity> getTrans_result()
        {
            return trans_result;
        }

        @Override
        public String toString()
        {
            return "from:" + from + "\nto:" + to + "\n" + (trans_result == null || trans_result.size() == 0 ? "null" : trans_result.toString());
        }

        public class TransResultEntity
        {
            /**
             * src : I am chinese,and you?
             * dst : 我是中国人，你呢？
             */

            private String src;
            private String dst;

            public void setSrc(String src)
            {
                this.src = src;
            }

            public void setDst(String dst)
            {
                this.dst = dst;
            }

            public String getSrc()
            {
                return src;
            }

            public String getDst()
            {
                return dst;
            }

            @Override
            public String toString()
            {
                return "原始:" + src + "\n翻译:" + dst;
            }
        }
    }
}

package com.biggun.drawbitmapmesh.Bean;

import java.util.List;

/**
 * 作者：孙贤武 on 2016/3/29 10:48
 * 邮箱：sun91985415@163.com
 */
public class MedicBean
{

    /**
     * showapi_res_code : 0
     * showapi_res_error :
     * showapi_res_body : {"allResults":7,"drugFactoryList":[{"addr":"安徽省铜陵市长江西路","factoryName":"铜陵有色金属集团股份有限公司金昌冶炼厂","linkPhone":"0562-5865371"},{"addr":"安徽省铜陵市铜陵县五松镇万鸡山路","factoryName":"铜陵龙达医药卫生用品有限责任公司","linkPhone":"0562-8811101 "},{"addr":"铜陵市经济技术开发区","factoryName":"铜陵和氏制药有限责任公司","linkPhone":"0562-2822940 "},{"addr":"安徽省铜陵市金山路(有色机械总厂内)","factoryName":"铜陵市医用氧气厂","linkPhone":"0562-5813554，5867681"},{"addr":"安徽省铜陵县钟鸣镇东街","factoryName":"安徽省铜陵冰片厂","linkPhone":"0562-8727369"},{"addr":"安徽省铜陵市经济技术开发区","factoryName":"上海长征富民药业铜陵有限公司","linkPhone":"0562-2839744"},{"addr":"铜陵市郊区铜山村6队","factoryName":"铜陵市新天商贸有限责任公司医用氧厂","linkPhone":"0562-28142882"}],"flag":true,"limit":20,"msg":"","page":1,"remark":"","ret_code":"0"}
     */

    private int showapi_res_code;
    private String showapi_res_error;
    private ShowapiResBodyEntity showapi_res_body;

    @Override
    public String toString()
    {
        return "MedicBean{" +
                "showapi_res_code=" + showapi_res_code +
                ", showapi_res_error='" + showapi_res_error + '\'' +
                ", showapi_res_body=" + showapi_res_body +
                '}';
    }

    public void setShowapi_res_code(int showapi_res_code)
    {
        this.showapi_res_code = showapi_res_code;
    }

    public void setShowapi_res_error(String showapi_res_error)
    {
        this.showapi_res_error = showapi_res_error;
    }

    public void setShowapi_res_body(ShowapiResBodyEntity showapi_res_body)
    {
        this.showapi_res_body = showapi_res_body;
    }

    public int getShowapi_res_code()
    {
        return showapi_res_code;
    }

    public String getShowapi_res_error()
    {
        return showapi_res_error;
    }

    public ShowapiResBodyEntity getShowapi_res_body()
    {
        return showapi_res_body;
    }

    public class ShowapiResBodyEntity
    {
        @Override
        public String toString()
        {
            return "ShowapiResBodyEntity{" +
                    "allResults=" + allResults +
                    ", flag=" + flag +
                    ", limit=" + limit +
                    ", msg='" + msg + '\'' +
                    ", page=" + page +
                    ", remark='" + remark + '\'' +
                    ", ret_code='" + ret_code + '\'' +
                    ", drugFactoryList=" +(drugFactoryList==null?"没有找到类似医药厂":drugFactoryList) +
                    '}';
        }

        /**
         * allResults : 7
         * drugFactoryList : [{"addr":"安徽省铜陵市长江西路","factoryName":"铜陵有色金属集团股份有限公司金昌冶炼厂","linkPhone":"0562-5865371"},{"addr":"安徽省铜陵市铜陵县五松镇万鸡山路","factoryName":"铜陵龙达医药卫生用品有限责任公司","linkPhone":"0562-8811101 "},{"addr":"铜陵市经济技术开发区","factoryName":"铜陵和氏制药有限责任公司","linkPhone":"0562-2822940 "},{"addr":"安徽省铜陵市金山路(有色机械总厂内)","factoryName":"铜陵市医用氧气厂","linkPhone":"0562-5813554，5867681"},{"addr":"安徽省铜陵县钟鸣镇东街","factoryName":"安徽省铜陵冰片厂","linkPhone":"0562-8727369"},{"addr":"安徽省铜陵市经济技术开发区","factoryName":"上海长征富民药业铜陵有限公司","linkPhone":"0562-2839744"},{"addr":"铜陵市郊区铜山村6队","factoryName":"铜陵市新天商贸有限责任公司医用氧厂","linkPhone":"0562-28142882"}]
         * flag : true
         * limit : 20
         * msg :
         * page : 1
         * remark :
         * ret_code : 0
         */

        private int allResults;
        private boolean flag;
        private int limit;
        private String msg;
        private int page;
        private String remark;
        private String ret_code;
        private List<DrugFactoryListEntity> drugFactoryList;

        public void setAllResults(int allResults)
        {
            this.allResults = allResults;
        }

        public void setFlag(boolean flag)
        {
            this.flag = flag;
        }

        public void setLimit(int limit)
        {
            this.limit = limit;
        }

        public void setMsg(String msg)
        {
            this.msg = msg;
        }

        public void setPage(int page)
        {
            this.page = page;
        }

        public void setRemark(String remark)
        {
            this.remark = remark;
        }

        public void setRet_code(String ret_code)
        {
            this.ret_code = ret_code;
        }

        public void setDrugFactoryList(List<DrugFactoryListEntity> drugFactoryList)
        {
            this.drugFactoryList = drugFactoryList;
        }

        public int getAllResults()
        {
            return allResults;
        }

        public boolean getFlag()
        {
            return flag;
        }

        public int getLimit()
        {
            return limit;
        }

        public String getMsg()
        {
            return msg;
        }

        public int getPage()
        {
            return page;
        }

        public String getRemark()
        {
            return remark;
        }

        public String getRet_code()
        {
            return ret_code;
        }

        public List<DrugFactoryListEntity> getDrugFactoryList()
        {
            return drugFactoryList;
        }

        public class DrugFactoryListEntity
        {
            /**
             * addr : 安徽省铜陵市长江西路
             * factoryName : 铜陵有色金属集团股份有限公司金昌冶炼厂
             * linkPhone : 0562-5865371
             */

            private String addr;
            private String factoryName;
            private String linkPhone;

            @Override
            public String toString()
            {
                return "{" +
                        "地址='" + addr + '\'' +
                        ", 名称='" + factoryName + '\'' +
                        ", 联系电话='" + linkPhone + '\'' +
                        '}';
            }

            public void setAddr(String addr)
            {
                this.addr = addr;
            }

            public void setFactoryName(String factoryName)
            {
                this.factoryName = factoryName;
            }

            public void setLinkPhone(String linkPhone)
            {
                this.linkPhone = linkPhone;
            }

            public String getAddr()
            {
                return addr;
            }

            public String getFactoryName()
            {
                return factoryName;
            }

            public String getLinkPhone()
            {
                return linkPhone;
            }
        }
    }
}

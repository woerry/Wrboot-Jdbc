package com.woerry.wrbootjdbc.Model.Entity;

import com.woerry.wrbootjdbc.Data.Annotation.WrColumn;
import com.woerry.wrbootjdbc.Data.Annotation.WrPrimarykey;
import com.woerry.wrbootjdbc.Data.Annotation.WrTable;
import com.woerry.wrbootjdbc.Data.Constant.ColType;
import com.woerry.wrbootjdbc.Data.Constant.DbType;

@WrTable(name = "wrboot_dbinfo",dbType = DbType.H2)
public class WrbootDbinfoEntity {
    @WrPrimarykey(name = "id",isAutoCreate = true,colType = ColType.INT)
    private Integer id;
    @WrColumn(name = "dbtype",colType=ColType.VARCHAR)
    private String dbtype;
    @WrColumn(name = "dbname",colType=ColType.VARCHAR)
    private String dbname;
    @WrColumn(name = "dbdriverclass",colType=ColType.VARCHAR)
    private String dbdriverclass;
    @WrColumn(name = "dburl",colType=ColType.VARCHAR)
    private String dburl;
    @WrColumn(name = "dbuser",colType=ColType.VARCHAR)
    private String dbuser;
    @WrColumn(name = "dbpassword",colType=ColType.VARCHAR)
    private String dbpassword ;
    @WrColumn(name = "remark",colType=ColType.VARCHAR)
    private String  remark;



    public WrbootDbinfoEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDbdriverclass() {
        return dbdriverclass;
    }

    public void setDbdriverclass(String dbdriverclass) {
        this.dbdriverclass = dbdriverclass;
    }

    public String getDburl() {
        return dburl;
    }

    public void setDburl(String dburl) {
        this.dburl = dburl;
    }

    public String getDbuser() {
        return dbuser;
    }

    public void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

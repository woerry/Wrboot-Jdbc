package com.woerry.wrbootjdbc.Model.Entity;

public class WrbootDbinfoEntity {
    private Integer id;
    private String dbtype;
    private String dbname;
    private String dbdriverclass;
    private String dburl;
    private String dbuser;
    private String dbpassword ;
    private String  remark;

    private String test;
    public String test1;
    public void setTest(String test) {
        this.test = test;
    }

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

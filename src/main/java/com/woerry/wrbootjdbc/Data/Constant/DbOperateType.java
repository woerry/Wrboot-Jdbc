package com.woerry.wrbootjdbc.Data.Constant;

/**
 * 数据库操作类型
 *
 * @auther woerry
 * @date 2020-03-27 10:59
 */
public enum DbOperateType {
    CT("create table","新建正式表"),CTB("create temporary table","新建临时表")

    ,CPD("create procedure","新建存储过程"),CSQ("create sequence","新建序列")
    ,CFN("create function","新建函数")
    ,IN("insert into","插入数据"),DL("delete","删除数据")
    ,TR("truncate","清空表格"),UP("update","更改数据")
    ,SF("select * from","查询数据")
            {
        
    };
    private String enname;
    private String cnname;

    public String getEnname() {
        return enname;
    }

    public String getCnname() {
        return cnname;
    }

    DbOperateType(String ENNAME, String CNNAME) {
    }

}

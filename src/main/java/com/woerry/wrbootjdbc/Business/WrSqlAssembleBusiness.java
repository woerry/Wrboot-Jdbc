package com.woerry.wrbootjdbc.Business;

import com.woerry.wrbootjdbc.Data.Annotation.WrPrimarykey;
import com.woerry.wrbootjdbc.Model.BaseEntity;

/**
 * SQL组装
 *
 * @auther woerry
 * @date 2020-03-26 15:10
 */
public class WrSqlAssembleBusiness {

    public  static  String assembleInsertSql(Object entity, BaseEntity baseEntity){
        Class<?> clazz = entity.getClass();
        String tableName = baseEntity.getTableName(clazz.getSimpleName());
        WrPrimarykey primaryName = baseEntity.getWrPrimarykey();
        StringBuilder sql = new StringBuilder("insert into ");
        return null;
    }
}

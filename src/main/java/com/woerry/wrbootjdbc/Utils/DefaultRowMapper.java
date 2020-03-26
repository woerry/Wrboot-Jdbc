package com.woerry.wrbootjdbc.Utils;


import com.woerry.wrbootjdbc.Model.BaseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultRowMapper<T> implements RowMapper<Object>,Serializable {
    private static final long serialVersionUID = -171970005792938279L;
    /** 转换的目标对象 */
    private Class<?>    clazz;
    /** 名称处理器 */
    private BaseEntity nameHandler;
    public DefaultRowMapper(Class<T> clazz, BaseEntity nameHandler) {
        this.clazz = clazz;
        this.nameHandler = nameHandler;
    }

    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Object entity = ClassUtils.newInstance(this.clazz);
        BeanInfo beanInfo = ClassUtils.getSelfBeanInfo(this.clazz);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String column = nameHandler.getColumnName(pd.getName());
            Method writeMethod = pd.getWriteMethod();
            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                writeMethod.setAccessible(true);
            }
            try {
                writeMethod.invoke(entity, resultSet.getObject(column));
            } catch (Exception e) {
                System.out.println("col="+column+"错误！");
                e.printStackTrace();
            }
        }
        return entity;
    }
}

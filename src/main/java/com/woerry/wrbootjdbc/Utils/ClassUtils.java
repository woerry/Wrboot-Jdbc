package com.woerry.wrbootjdbc.Utils;

import com.woerry.wrbootjdbc.Model.Entity.WrbootDbinfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 类辅助
 */
public class ClassUtils
{
    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(ClassUtils.class);
    /**
     * Map keyed by class containing CachedIntrospectionResults. Needs to be a
     * WeakHashMap with WeakReferences as values to allow for proper garbage
     * collection in case of multiple class loaders.
     */
    private static final Map<Class, BeanInfo> classCache = Collections.synchronizedMap(new WeakHashMap<Class, BeanInfo>());
    /**
     * 获取类本身的BeanInfo，不包含父类属性
     *
     * @param clazz
     * @return
     */
    public static BeanInfo getSelfBeanInfo(Class<?> clazz)
    {
        BeanInfo beanInfo = null;
        try
        {
            if (classCache.get(clazz) == null)
            {
                beanInfo = Introspector.getBeanInfo(clazz, clazz.getSuperclass());
                classCache.put(clazz, beanInfo);
// Immediately remove class from Introspector cache, to allow for proper
// garbage collection on class loader shutdown - we cache it here anyway,
// in a GC-friendly manner. In contrast to CachedIntrospectionResults,
// Introspector does not use WeakReferences as values of its WeakHashMap!
                Class classToFlush = clazz;
                do
                {
                    Introspector.flushFromCaches(classToFlush);
                    classToFlush = classToFlush.getSuperclass();
                } while (classToFlush != null);
            }
            else
            {
                beanInfo = classCache.get(clazz);
            }
        }
        catch (IntrospectionException e)
        {
            LOG.error("获取BeanInfo失败", e);
            e.printStackTrace();
        }
        return beanInfo;
    }
    /**
     * 初始化实例
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(Class<?> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (Exception e)
        {
            LOG.error("根据class创建实例失败", e);
            e.printStackTrace();
        }
        return null;
    }

    public static  Object getValue(Object obj,String fieldname) throws IllegalAccessException {
        Class clazz=obj.getClass();
       Field[] fields= clazz.getDeclaredFields();
       Field field=null;
        for (Field f:fields
             ) {
            f.setAccessible(true);
            if(f.getName().equals(fieldname)){
                field=f;
            }
        }
       Object oc= field.get(obj);
        return oc;
    }

    public static void main(String[] args) {
        WrbootDbinfoEntity entity=new WrbootDbinfoEntity();
        entity.setId(1);
        entity.setDbname("hasagi");
        entity.setTest("test");
        entity.test1="test1";
        Object obj=null;
        try {
            obj=(String)getValue(entity,"test");
            System.out.println(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

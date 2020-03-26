package com.woerry.wrbootjdbc.Utils;

import java.sql.Timestamp;
import java.sql.Types;

/**
 * TODO
 *
 * @auther woerry
 * @date 2020-03-26 15:14
 */
public class WrSqlUtils {
    /***
     * 进行参数匹配，返回的是加入sql语句的部分
     *
     * @param obj
     * @return  {@link String}
     * @throws
     * @auther woerry
     * @date 2020-03-26 17:17
     */
    public static String switchArgType(Object obj){
        String res=null;
        if(obj==null){
            System.out.println("switchArgType:obj=null");
            return null;
        }


        switch (obj.getClass().getSimpleName()){
            case "Integer":
                res=String.valueOf(obj);
                break;
            case "Double":
                res=String.valueOf(obj);
                break;
            case "Byte":
                res=String.valueOf(obj);
                break;
            case "Bit":
                res=String.valueOf(obj);
                break;
            default:
                res="'"+obj.toString()+"'";
        }
        return res;
    }

    /***
     * 进行参数匹配，返回的是加入jdbcTemplate的执行参数的Types种类
     *
     * @param obj
     * @return  {@link String}
     * @throws
     * @auther woerry
     * @date 2020-03-26 17:17
     */
    public static int switchArgTypeToInt(Object obj){
        int res=0;
        if(obj==null){
            return Types.NULL;
        }
        switch (obj.getClass().getSimpleName()){
            case "Integer":
                res=Types.INTEGER;
                break;
            case "Double":
                res=Types.DOUBLE;
                break;
            case "Boolean":
                res=Types.BOOLEAN;
                break;
            case "Timestamp":
                res=Types.TIMESTAMP;
                break;
            case "Decimal":
                res=Types.DECIMAL;
                break;
            case "Date":
                res=Types.DATE;
                break;
            default:
                res=Types.VARCHAR;
        }
        return res;
    }


    public static void main(String[] args) {
        byte[] a={1,0};
        String b="cadss";
        Timestamp ts=DateUtil.getNowTimestamp();
        System.out.println(switchArgType(ts));
    }


}

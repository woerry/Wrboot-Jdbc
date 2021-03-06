package com.woerry.wrbootjdbc.Model;



import com.woerry.wrbootjdbc.Data.Annotation.*;
import com.woerry.wrbootjdbc.Exception.BaseException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  BaseEntity<T> implements Serializable  {


    private static final long serialVersionUID = -117426825547412173L;
    private String tablename;
    private String key;
    private List<WrUnionkey> ukeys=new ArrayList<WrUnionkey>();
    private List<WrForeignkey> fkeys=new ArrayList<WrForeignkey>();
    private List<WrColumn> commoncols=new ArrayList<WrColumn>();
    private List<WrUnionkey> autoCreatementUkeys=new ArrayList<WrUnionkey>();
    private String allcolsStr;
    private WrPrimarykey wrPrimarykey=null;
    // columns：key=字段名称，value=wr注解name
    private Map<String,String> columns=new HashMap<String, String>();

    private int keyCount=0;

    /***
     * 主键或联合主键数量
     *
     * @param
     * @return  {@link int}
     * @throws
     * @auther woerry
     * @date 2020-03-26 15:43
     */
    public int getKeyCount() {
        return keyCount;
    }

    public List<WrUnionkey> getAutoCreatementUkeys() {
        return autoCreatementUkeys;
    }

    /***
     * 取所有字段，包括主键。取的value是Wr注解中的name=值
     *
     * @param
     * @return  {@link Map< String, String>}
     * @throws
     * @auther woerry
     * @date 2020-03-26 14:58
     */
    public Map<String, String> getColumns() {
        return columns;
    }


    public WrPrimarykey getWrPrimarykey() {
        return wrPrimarykey;
    }

    public List<WrForeignkey> getFkeys() {
        return fkeys;
    }

    /***
     * 构造方法
     *
     * @param tclass 传入泛型
     * @return  {@link null}
     * @throws
     * @auther woerry
     * @date 2020-03-27 14:06
     */
    public BaseEntity(Class<T> tclass){


        Field[] fields= tclass.getDeclaredFields();
        WrTable wrTable=tclass.getAnnotation(WrTable.class);
        tablename=wrTable.name();
        for (Field field:fields
                ) {
             wrPrimarykey=field.getAnnotation(WrPrimarykey.class);
            if (wrPrimarykey!=null){
                key=wrPrimarykey.name();
                columns.put(field.getName(),key);
                keyCount=1;

                continue;
            }

            WrUnionkey wrUnionkey=field.getAnnotation(WrUnionkey.class);
            if (wrUnionkey!=null){
                ukeys.add(wrUnionkey);
                columns.put(field.getName(),wrUnionkey.name());
                if(wrUnionkey.isAutoCreate()){
                    autoCreatementUkeys.add(wrUnionkey);
                }
                keyCount++;

                continue;
            }
            WrForeignkey wrForeignkey=field.getAnnotation(WrForeignkey.class);
            if (wrForeignkey!=null){
                fkeys.add(wrForeignkey);
                columns.put(field.getName(),wrForeignkey.name());
                continue;
            }
            WrColumn wrColumn=field.getAnnotation(WrColumn.class);
            if (wrColumn!=null){
                commoncols.add(wrColumn);
                columns.put(field.getName(),wrColumn.name());
                continue;
            }
        }
        if(wrPrimarykey!=null&&fkeys.size()>0){
            throw new BaseException("不能同时设置主键和联合主键！");
        }

        if (key==null&&ukeys==null){

                throw new BaseException("类中没有@WrPrimarykey或@WrUnionkey主键。");

        }

        generateAllcolsStr(key,  ukeys, commoncols);

    }

    public BaseEntity(String tablename, String key, List<WrUnionkey> ukeys, List<WrColumn> cols) {
        this.tablename = tablename;
        this.key = key;
        this.ukeys = ukeys;
        this.commoncols = cols;
        generateAllcolsStr(key,  ukeys, cols);
    }

    /***
     *
     * 获取所有字段的字符串，以","拼接
     * @param key
     * @param ukeys
     * @param cols
     * @return  {@link String}
     * @throws
     * @auther woerry
     * @date 2020-03-27 14:07
     */
    private String generateAllcolsStr(String key, List<WrUnionkey> ukeys, List<WrColumn> cols){

        String result="";
        if(key!=null&&!key.equals("")){
            result=" "+key+" ";

        }else{
            if(ukeys.size()>0){
                int i=0;
                for (WrUnionkey uk:ukeys
                        ) {

                    if(i==0){
                        result=uk.name();
                    }else{
                        result=result+","+uk.name();
                    }

                    i++;
                }
            }
        }


        if(cols.size()>0){
            for (WrColumn uk:cols
                    ) {
                result=result+","+uk.name();
            }
        }

        this.allcolsStr=result;
        return result;
    }

    public String getAllcolsStr() {
        return allcolsStr;
    }

    public void setAllcolsStr(String allcols) {
        this.allcolsStr = allcols;
    }

    public List<WrColumn> getCommoncols() {
        return commoncols;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "tablename='" + tablename + '\'' +
                ", key='" + key + '\'' +
                ", ukeys=" + ukeys +
                ", cols=" + commoncols +
                '}';
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<WrUnionkey> getUkeys() {
        return ukeys;
    }

    public void setUkeys(List<WrUnionkey> ukeys) {
        this.ukeys = ukeys;
    }

    public List<WrColumn> getCols() {
        return commoncols;
    }

    public void setCols(List<WrColumn> cols) {
        this.commoncols = cols;
    }

    public String getTableName(String entityName) {

        return this.tablename;
    }

    public String getPrimaryName(String entityName) {
        return this.key;
    }

    public String getTableName() {

        return this.tablename;
    }

    public String getPrimaryName() {
        return this.key;
    }
    public String getColumnName(String fieldName) {
        return this.getColumns().get(fieldName);
    }
}

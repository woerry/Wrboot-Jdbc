package com.woerry.wrbootjdbc.Model;


import com.picc.woerry.bidbond.Data.Annotation.WrColumn;
import com.picc.woerry.bidbond.Data.Annotation.WrPrimarykey;
import com.picc.woerry.bidbond.Data.Annotation.WrTable;
import com.picc.woerry.bidbond.Data.Annotation.WrUnionkey;
import com.picc.woerry.bidbond.Utils.NameHandler;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  BaseEntity<T> implements Serializable ,NameHandler {


    private static final long serialVersionUID = -117426825547412173L;
    private String tablename;
    private String key;
    private List<WrUnionkey> ukeys=new ArrayList<WrUnionkey>();
    private List<WrColumn> cols=new ArrayList<WrColumn>();
    private String allcols;
    private Map<String,String> columns=new HashMap<String, String>();

    public Map<String, String> getColumns() {
        return columns;
    }





    public BaseEntity(Class<T> tclass){


        //String key=null;
      //  String table=null;
       // List<WrUnionkey> ukeys=new ArrayList();
        //List<WrColumn> cols=new ArrayList();
        Field[] fields= tclass.getDeclaredFields();
        WrTable wrTable=tclass.getAnnotation(WrTable.class);
        tablename=wrTable.name();
        for (Field field:fields
                ) {
            WrPrimarykey wrPrimarykey=field.getAnnotation(WrPrimarykey.class);
            if (wrPrimarykey!=null){
                key=wrPrimarykey.name();
                columns.put(field.getName(),key);
                continue;
            }

            WrUnionkey wrUnionkey=field.getAnnotation(WrUnionkey.class);
            if (wrUnionkey!=null){
                ukeys.add(wrUnionkey);
                columns.put(field.getName(),wrUnionkey.name());
                continue;
            }
            WrColumn wrColumn=field.getAnnotation(WrColumn.class);
            if (wrColumn!=null){
                cols.add(wrColumn);
                columns.put(field.getName(),wrColumn.name());
                continue;
            }
        }


        if (key==null&&ukeys==null){
            try {
                throw new Exception("类中没有@WrPrimarykey或@WrUnionkey主键。");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        generateAllcols(key,  ukeys, cols);

    }

    public BaseEntity(String tablename, String key, List<WrUnionkey> ukeys, List<WrColumn> cols) {
        this.tablename = tablename;
        this.key = key;
        this.ukeys = ukeys;
        this.cols = cols;
        generateAllcols(key,  ukeys, cols);
    }

    private void generateAllcols(String key, List<WrUnionkey> ukeys, List<WrColumn> cols){

        String result="";
        if(key!=null&&!key.equals("")){
            result=" "+key+" ";
            if(ukeys.size()>0){
                for (WrUnionkey uk:ukeys
                        ) {
                    result=result+","+uk.name();
                }
            }
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

        this.allcols=result;

    }

    public String getAllcols() {
        return allcols;
    }

    public void setAllcols(String allcols) {
        this.allcols = allcols;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "tablename='" + tablename + '\'' +
                ", key='" + key + '\'' +
                ", ukeys=" + ukeys +
                ", cols=" + cols +
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
        return cols;
    }

    public void setCols(List<WrColumn> cols) {
        this.cols = cols;
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

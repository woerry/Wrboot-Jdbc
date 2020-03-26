package com.woerry.wrbootjdbc.Model;





import com.alibaba.fastjson.JSON;
import com.woerry.wrbootjdbc.Data.Annotation.WrPrimarykey;
import com.woerry.wrbootjdbc.Data.Annotation.WrTable;
import com.woerry.wrbootjdbc.Data.Annotation.WrUnionkey;
import com.woerry.wrbootjdbc.Data.Constant.OrderType;
import com.woerry.wrbootjdbc.Utils.DefaultRowMapper;
import com.woerry.wrbootjdbc.Utils.WrSqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * 使用WrBaseDao时，请继承本类。然后在子类需要处理sql时，调用本类的setJdbcTemplate()方法进行赋值。
 */
@SuppressWarnings("unchecked")
public  class WrBaseDao <T> {
    private Logger log = LoggerFactory.getLogger(WrBaseDao.class);
    private Class<T> tclass;
        private List<WrUnionkey> unionkeys=null;
    private int keyCount=0;

    private  BaseEntity beseentity=null;

    public WrBaseDao() {

       Class objclass= (Class<T>) ((ParameterizedType) getClass()
               .getGenericSuperclass()).getActualTypeArguments()[0];

      tclass= (Class<T>) objclass;

        try {
            setTableClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



   private   void  setTableClass() throws Exception {

       if ( tclass.isAnnotationPresent(WrTable.class)){

           beseentity=new BaseEntity<T>(tclass);
           if (beseentity.getUkeys()!=null&&beseentity.getUkeys().size()>0){
               unionkeys=beseentity.getUkeys();
           }
           keyCount=beseentity.getKeyCount();
           log.info("columns="+ JSON.toJSON(beseentity.getColumns()));
       }else {
           throw new Exception("类中没有@WrTable注释");
       }

   }


    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        log.info("初始化jdbcTemplate="+this.jdbcTemplate);
    }







    /**
     * 插入一条记录(自增主键适用)
     *
     * @param entity
     */
    public Long insert(T entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
//        final SqlContext sqlContext = SqlUtils.buildInsertSql(entity, this.beseentity);
//
//        log.info("insert:"+entity.getClass().getSimpleName()+",sql="+sqlContext.getSql());
//        PreparedStatementCreator psc=new PreparedStatementCreator(){
//            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                PreparedStatement ps = con.prepareStatement(sqlContext.getSql().toString(),
//                        new String[] { sqlContext.getPrimaryKey() });
//                int index = 0;
//                for (Object param : sqlContext.getParams()) {
//                    index++;
//                    ps.setObject(index, param);
//                }
//                return ps;
//            }
//        };
//        jdbcTemplate.update(psc , keyHolder);


        return keyHolder.getKey().longValue();
//        return i;
    }

    /**
     * 生成关于主键的where后语句
     * @return
     */
        private Map<String,Object> generateWhereKeySql(T entity){
        String res=null;
            Map<String,Object> map=new HashMap<>();
          List<WrUnionkey> ulist= beseentity.getUkeys();
           WrPrimarykey pk= beseentity.getWrPrimarykey();
        if(keyCount>1){
            for (WrUnionkey uk:ulist
                 ) {
//               map.put(uk.name(),);
            }
        }else{
            res=res+" and "+pk.name()+"=? ";
        }
        return null;
        }

//没写好
    public int update(Map<String,Object> map) {
        String tableName = beseentity.getTableName();

        String sql = "update " + tableName +" set ";
        Object[] objects=new Object[map.entrySet().size()];
        int[] types=new int[map.entrySet().size()];
        int i=0;
        if(map!=null){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sql=sql+ " WHERE "+entry.getKey()+"=?";
                objects[i]=entry.getValue();
                types[i]=WrSqlUtils.switchArgTypeToInt(entry.getValue());
                i++;
            }
        }

        return jdbcTemplate.update(sql,objects,types);
    }


    /***
     *
     * 更新记录，除了某列
     * @param entity
     * @param exceptcolumn  被排除的某列
     * @return
     * @throws
     * @auther woerry
     * @date 2020-03-20 15:04
     */
    public void updateExcept(T entity,@Nullable String exceptcolumn) {
//        SqlContext sqlContext = SqlUtils.buildUpdateSqlExcept(entity, this.beseentity,exceptcolumn);
//        System.out.println("updateExceptsql="+sqlContext.getSql());
//        jdbcTemplate.update(sqlContext.getSql().toString(), sqlContext.getParams().toArray());
    }


    /***
     * 删除数据
     *
     * @param map
     * @return  {@link int}
     * @throws
     * @auther woerry
     * @date 2020-03-26 18:01
     */
    public int delete(Map<String,Object> map) {
        String tableName = this.beseentity.getTableName();
        String sql = "DELETE FROM " + tableName ;
        Object[] objects=new Object[map.entrySet().size()];
        int[] types=new int[map.entrySet().size()];
        int i=0;
                if(map!=null){
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        sql=sql+ " WHERE "+entry.getKey()+"=?";
                        objects[i]=entry.getValue();
                        types[i]=WrSqlUtils.switchArgTypeToInt(entry.getValue());
                        i++;
                    }
                }

       return jdbcTemplate.update(sql,objects,types);
    }



    /***
     * 根据主键删除数据
     *
     * @param value
     * @return  {@link int}
     * @throws
     * @auther woerry
     * @date 2020-03-26 18:00
     */
    public int deleteByPkey(Object value){
        Map<String,Object> map=new HashMap<>();
        map.put(beseentity.getPrimaryName(),value);
        return delete(map);
    }

    /**
     * 删除所有记录
     */

    public void deleteAll() {
        String tableName = this.beseentity.getTableName();
        String sql = " TRUNCATE TABLE " + tableName;
        jdbcTemplate.execute(sql);
    }



    /**
     * 查询记录(param的字符串.不需要写where，必须以and开头)
     * @Param wherestr
     * @return
     */
    public List<T> retrieve(String wherestr) {
        String sql = "SELECT * FROM "
                + this.beseentity.getTableName()+" where 1=1 ";
        if(wherestr!=null&&!wherestr.equals("")){
            sql=sql+" "+wherestr;
        }
        log.info("Retrieve():sql="+sql);
        return (List<T>) jdbcTemplate.query(sql,
                new DefaultRowMapper(this.tclass, this.beseentity));
    }



    /**
     * 查询所有记录
     * @return
     */
    public List<T> retrieveAll() {
        return retrieve(null,null,null);
    }

    /**
     * 查询记录
     * @Param param的Map数组
     * @return
     */
    public List<T> retrieve(Map<String,Object> map, String index, OrderType orderby) {
        StringBuilder sql = new StringBuilder("SELECT * FROM "
                + this.beseentity.getTableName()+" where 1=1 ");
        Object[] objects=new Object[map.entrySet().size()];
        int i=0;
        if(map!=null){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sql.append(" and ").append(entry.getKey()).append("= ?");
                objects[i]=entry.getValue();
                i++;
            }
        }
        if(orderby!=null&&index!=null&&!index.equals("")){
            sql.append(" order by  "+index+" "+orderby);
        }
        log.info("Retrieve="+sql.toString());
        return  (List<T>)jdbcTemplate.query(sql.toString(),objects,new DefaultRowMapper(this.tclass, this.beseentity));
    }

    /***
     *
     * 查询记录,按index从高到低
     * @param map
     * @param index 排序的索引
     * @return  {@link List<T>}
     * @throws
     * @auther woerry
     * @date 2020-03-20 14:19
     */
    public List<T> retrieveDesc(Map<String,Object> map,String index) {
        return retrieve(map,index,OrderType.DESC);
    }



    /**
     * 查询记录数
     *
     * @param entity
     * @return
     */
    public int queryCount(T entity) {
        String tableName = this.beseentity.getTableName();
        StringBuilder countSql = new StringBuilder("select count(*) from ");
        countSql.append(tableName);
//        SqlContext sqlContext = SqlUtils.buildQueryCondition(entity, this.beseentity);
//        if (sqlContext.getSql().length() > 0) {
//            countSql.append(" where ");
//            countSql.append(sqlContext.getSql());
//        }

        return jdbcTemplate.queryForObject(countSql.toString(),Integer.class);
    }

}

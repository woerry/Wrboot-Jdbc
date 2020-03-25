package com.woerry.wrbootjdbc.Model;





import com.woerry.wrbootjdbc.Data.Annotation.WrUnionkey;
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
        private List<WrUnionkey> forignkeys=null;
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



    private  BaseEntity beseentity=null;

    private String allCols=null;
   private   void  setTableClass() throws Exception {



       if ( tclass.isAnnotationPresent(WrTable.class)){

           //beseentity=new BaseEntity(tclass);
           beseentity=new BaseEntity<T>(tclass);
           if (beseentity.getUkeys()!=null&&beseentity.getUkeys().size()>0){
               forignkeys=beseentity.getUkeys();
           }

           log.info("columns="+JSON.toJSON(beseentity.getColumns()));
       }else {
           throw new Exception("类中没有@WrTable注释");
       }

   }

    @Autowired
    @Qualifier("devJdbcTemplate")
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
        final SqlContext sqlContext = SqlUtils.buildInsertSql(entity, this.beseentity);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("insert:"+entity.getClass().getSimpleName()+",sql="+sqlContext.getSql());
        PreparedStatementCreator psc=new PreparedStatementCreator(){
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sqlContext.getSql().toString(),
                        new String[] { sqlContext.getPrimaryKey() });
                int index = 0;
                for (Object param : sqlContext.getParams()) {
                    index++;
                    ps.setObject(index, param);
                }
                return ps;
            }
        };
        jdbcTemplate.update(psc , keyHolder);
//        List<Object> os=sqlContext.getParams();
//        Object[] objs= os.toArray();
//       int i= jdbcTemplate.update(sqlContext.getSql().toString(),objs);
        return keyHolder.getKey().longValue();
//        return i;
    }

    /**
     * 插入一条记录(非自增主键适用)
     *
     * @param entity
     */
    public int insert2(T entity) {
        final SqlContext sqlContext = SqlUtils.buildInsertSql(entity, this.beseentity);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("非自增insert:"+entity.getClass().getSimpleName()+",sql="+sqlContext.getSql()+",params="+JSON.toJSON(sqlContext.getParams()));

        List<Object> os=sqlContext.getParams();
        Object[] objs= os.toArray();
       int i= jdbcTemplate.update(sqlContext.getSql().toString(),objs);
        return i;
    }

    /**
     * 更新记录
     *
     * @param entity
     */

    public void update(T entity) {
        SqlContext sqlContext = SqlUtils.buildUpdateSql(entity, this.beseentity);

        jdbcTemplate.update(sqlContext.getSql().toString(), sqlContext.getParams().toArray());
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
        SqlContext sqlContext = SqlUtils.buildUpdateSqlExcept(entity, this.beseentity,exceptcolumn);
        System.out.println("updateExceptsql="+sqlContext.getSql());
        jdbcTemplate.update(sqlContext.getSql().toString(), sqlContext.getParams().toArray());
    }

    /**
     * 删除记录
     *
     * @param id
     */

    public void delete(Serializable id) {
        String tableName = this.beseentity.getTableName();
        String primaryName = this.beseentity.getPrimaryName();
        String sql = "DELETE FROM " + tableName + " WHERE " + primaryName + " = ?";
        jdbcTemplate.update(sql, id);
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
     * 得到记录
     *
     * @param id
     * @return
     */

    public T getById(Serializable id) {
        String tableName = this.beseentity.getTableName();
        String primaryName = this.beseentity.getPrimaryName();
        String sql = "SELECT * FROM " + tableName + " WHERE " + primaryName + " = ?";
        return (T) jdbcTemplate.query(sql,
                new DefaultRowMapper(this.tclass, this.beseentity), id).get(0);
    }

    /**
     * 查询记录(param的字符串.不需要写where，必须以and开头)
     * @Param wherestr
     * @return
     */
    public List<T> receive(String wherestr) {
        String sql = "SELECT * FROM "
                + this.beseentity.getTableName()+" where 1=1 ";
        if(wherestr!=null&&!wherestr.equals("")){
            sql=sql+" "+wherestr;
        }
        log.info("receive():sql="+sql);
        return (List<T>) jdbcTemplate.query(sql,
                new DefaultRowMapper(this.tclass, this.beseentity));
    }



    /**
     * 查询所有记录
     * @return
     */

    public List<T> receive() {
        String sql = "SELECT * FROM "
                + this.beseentity.getTableName()+" where 1=1 ";

        return (List<T>) jdbcTemplate.query(sql,
                new DefaultRowMapper(this.tclass, this.beseentity));
    }

    /**
     * 查询记录
     * @Param param的Map数组
     * @return
     */

    public List<T> receive(Map<String,Object> map) {
        StringBuilder sql = new StringBuilder("SELECT * FROM "
                + this.beseentity.getTableName()+" where 1=1 ");
        if(map!=null){

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sql.append(" and ").append(entry.getKey()).append("=")
                        .append(entry.getValue());
            }
        }
        return (List<T>) jdbcTemplate.query(sql.toString(),
                new DefaultRowMapper(this.tclass, this.beseentity));
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
    public List<T> receiveASC(Map<String,Object> map,String index) {
        StringBuilder sql = new StringBuilder("SELECT * FROM "
                + this.beseentity.getTableName()+" where 1=1 ");
        if(map!=null){

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String classtype=entry.getValue().getClass().getSimpleName();
                if(classtype.equals("Double")||classtype.equals("double")||classtype.equals("Float")
                ||classtype.equals("float")||classtype.equals("Integer")||classtype.equals("int")){
                    sql.append(" and ").append(entry.getKey()).append("=")
                            .append(entry.getValue());
                }else{
                    sql.append(" and ").append(entry.getKey()).append("= '")
                            .append(entry.getValue()).append("'");
                }

            }
        }


        sql.append(" order by ").append(index).append(" desc");
        return (List<T>) jdbcTemplate.query(sql.toString(),
                new DefaultRowMapper(this.tclass, this.beseentity));
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
        SqlContext sqlContext = SqlUtils.buildQueryCondition(entity, this.beseentity);
        if (sqlContext.getSql().length() > 0) {
            countSql.append(" where ");
            countSql.append(sqlContext.getSql());
        }

        return jdbcTemplate.queryForObject(countSql.toString(),Integer.class);
    }

}

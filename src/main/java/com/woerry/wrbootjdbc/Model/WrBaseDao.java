package com.woerry.wrbootjdbc.Model;





import com.alibaba.fastjson.JSON;
import com.woerry.wrbootjdbc.Data.Annotation.*;
import com.woerry.wrbootjdbc.Data.Constant.DbOperateType;
import com.woerry.wrbootjdbc.Data.Constant.OrderType;
import com.woerry.wrbootjdbc.Exception.BaseException;
import com.woerry.wrbootjdbc.Utils.ClassUtils;
import com.woerry.wrbootjdbc.Utils.DefaultRowMapper;
import com.woerry.wrbootjdbc.Utils.WrSqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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
    private List<WrForeignkey> foreignkeys=null;
    private List<WrColumn> comcols=null;
    private WrPrimarykey pkey=null;
    private int keyCount=0;
    private  BaseEntity beseentity=null;
    private String tableName=null;
    private List<WrUnionkey> autoCreatementUkeys=null;
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
       tableName = this.beseentity.getTableName();
       if ( tclass.isAnnotationPresent(WrTable.class)){

           beseentity=new BaseEntity<T>(tclass);
           if (beseentity.getUkeys()!=null&&beseentity.getUkeys().size()>0){
               unionkeys=beseentity.getUkeys();
               autoCreatementUkeys=beseentity.getAutoCreatementUkeys();
           }
           if(beseentity.getWrPrimarykey()!=null){
               pkey=beseentity.getWrPrimarykey();
           }
           keyCount=beseentity.getKeyCount();
           comcols=beseentity.getCommoncols();
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


    public Object insert(Map<String,Object> map,T entity) {
        String sql=" insert into "+tableName+" (";
        //主键自增
        if(pkey.isAutoCreate()){
            for (Map.Entry<String, Object> param:map.entrySet()
            ) {
                if(param.getKey().equals(pkey.name())){
                    continue;
                }
                    sql=sql+" "+param.getKey()+",";
            }
            sql=sql.substring(0,sql.length()-1)+" ) values (";
        }else
        if(autoCreatementUkeys.size()>0){
            List<String> ukname=new ArrayList<>();
            for (WrUnionkey uk:autoCreatementUkeys
                 ) {
                if(uk.isAutoCreate()) {
                    ukname.add(uk.name());
                }
            }

            for (Map.Entry<String, Object> param:map.entrySet()
            ) {
                if(ukname.contains(param.getKey())){
                    continue;
                }
                sql=sql+" "+param.getKey()+",";
            }
            sql=sql.substring(0,sql.length()-1)+" ) values (";
        }else {


        }

        return null;
    }



    /**
     * 生成关于主键的where后map.用来组装where后的限制条件。
     * @return
     */
        private Map<String,Object> generateKeyMap(T entity){
            Map<String,Object> map=new HashMap<>();


        if(keyCount>1){
            for (WrUnionkey uk:unionkeys
                 ) {
               map.put(uk.name(), ClassUtils.getValue(entity,uk.name()));
            }
        }else{
            map.put(pkey.name(), ClassUtils.getValue(entity,pkey.name()));
        }
        return map;
        }
    /**
     * 生成关于主键的where后sql.用来组装where后的限制条件。
     * @return
     */
    private String  generateKeySql(T entity){
            String res=null;


        if(keyCount>1){
            for (WrUnionkey uk:unionkeys
            ) {
                res=" and "+uk.name()+"="+WrSqlUtils.switchArgType(ClassUtils.getValue(entity,uk.name())) ;
            }
        }else{
            res=" and "+pkey.name()+"="+WrSqlUtils.switchArgType(ClassUtils.getValue(entity,pkey.name())) ;
        }
        return res;
    }


    /***
     * 更新传入的entity
     *
     * @param entity
     * @return  {@link int}
     * @throws
     * @auther woerry
     * @date 2020-03-27 15:06
     */
    public int update(T entity){
        Map<String,Object> map=new HashMap<>();

        for (WrColumn comcol:comcols
             ) {
            map.put(comcol.name(),ClassUtils.getValue(entity,comcol.name()));
        }

        if(!pkey.isAutoCreate()){
            //只有不新增的主键，才能更新
            map.put(pkey.name(),ClassUtils.getValue(entity,pkey.name()));
        }

        for (WrUnionkey ukey:unionkeys
             ) {
            if(!ukey.isAutoCreate()){
                //只有不新增的主键，才能更新
                map.put(ukey.name(),ClassUtils.getValue(entity,ukey.name()));
            }
        }

       return update(map,entity);
    }

    /***
     * 将map的值更新给entity
     *
     * @param map 传入的更新参数
     * @param entity 原entity
     * @return  {@link int}
     * @throws
     * @auther woerry
     * @date 2020-03-27 14:50
     */
    public int update(Map<String,Object> map,T entity) {
        if(map==null){
            throw new BaseException("update的map参数为空!");
        }
        String whereAndSql=generateKeySql(entity);

        String sql = "update " + tableName +" set ";
        Object[] objects=new Object[map.entrySet().size()];
        int[] types=new int[map.entrySet().size()];
        int i=0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
                sql=sql+ "  "+entry.getKey()+"=?,";
                objects[i]=entry.getValue();
                types[i]=WrSqlUtils.switchArgTypeToInt(entry.getValue());
                i++;
        }
        sql=sql.substring(0,sql.length()-1);
        sql=sql+" where 1=1 "+whereAndSql;

        return jdbcTemplate.update(sql,objects,types);
    }




    /***
     *  删除entity。根据主键或者联合主键的值。
     *
     * @param entity 要删除的entity
     * @return  {@link int}
     * @throws
     * @auther woerry
     * @date 2020-03-27 10:40
     */
    public int delete(T entity){

        processForeignkeys(DbOperateType.DL,entity);
        return delete(generateKeyMap(entity));
    }


    /***
     * 处理外键
     *
     * @param
     * @return  {@link String}
     * @throws
     * @auther woerry
     * @date 2020-03-27 10:57
     */
    private Boolean processForeignkeys(DbOperateType optype,T entity){

        String sql=null;

        if(foreignkeys.size()<1){
            return false;
        }
        for (WrForeignkey fkey:foreignkeys
             ) {
            switch (optype){
                case DL:
                    sql=" delete from "+fkey.aimtable()+" " +
                            "where "+fkey.aimcol()+"="+WrSqlUtils.switchArgType(ClassUtils.getValue(entity,fkey.name()));
                   log.info("执行删除外键的关联数据sql="+sql);
                    this.getJdbcTemplate().execute(sql);
                    break;
                default:

            }
        }

        return true;

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





    /**
     * 删除所有记录
     */

    public void deleteAll() {

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
                + tableName+" where 1=1 ";
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
                + tableName+" where 1=1 ");
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
     * @return
     */
    public int queryCount() {

        StringBuilder countSql = new StringBuilder("select count(*) from ");
        countSql.append(tableName);

        return jdbcTemplate.queryForObject(countSql.toString(),Integer.class);
    }

}

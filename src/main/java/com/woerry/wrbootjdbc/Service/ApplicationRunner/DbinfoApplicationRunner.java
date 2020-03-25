package com.woerry.wrbootjdbc.Service.ApplicationRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Order(1)
public class DbinfoApplicationRunner implements ApplicationRunner {

    @Autowired
    JdbcTemplate h2JdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String sql="select * from test";
       List<Map<String,Object>> list= h2JdbcTemplate.queryForList(sql);
        for (Map<String,Object> map:list
             ) {
            System.out.println(map.get("name").toString());
        }
    }
}

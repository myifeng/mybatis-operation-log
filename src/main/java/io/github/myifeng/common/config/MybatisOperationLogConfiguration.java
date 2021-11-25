package io.github.myifeng.common.config;

import io.github.myifeng.common.interceptor.MybatisOperationLogInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@AutoConfigureOrder(Integer.MAX_VALUE)
@ConditionalOnClass(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MybatisOperationLogConfiguration {

    @Autowired
    MybatisOperationLogInterceptor mybatisOperationLogInterceptor;

    @Autowired
    List<SqlSessionFactory> sqlSessionFactories;

    @PostConstruct
    public void addMybatisOperationLogInterceptor(){
        for (SqlSessionFactory factory : sqlSessionFactories) {
            factory.getConfiguration().addInterceptor(mybatisOperationLogInterceptor);
        }
    }

}

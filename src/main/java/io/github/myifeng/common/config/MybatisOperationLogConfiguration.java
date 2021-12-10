package io.github.myifeng.common.config;

import io.github.myifeng.common.interceptor.MybatisCommonInterceptor;
import io.github.myifeng.common.interceptor.MybatisOperationLogInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
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

    final MybatisOperationLogInterceptor mybatisOperationLogInterceptor;

    final MybatisCommonInterceptor mybatisCommonInterceptor;

    final List<SqlSessionFactory> sqlSessionFactories;

    public MybatisOperationLogConfiguration(MybatisOperationLogInterceptor mybatisOperationLogInterceptor, MybatisCommonInterceptor mybatisCommonInterceptor, List<SqlSessionFactory> sqlSessionFactories) {
        this.mybatisOperationLogInterceptor = mybatisOperationLogInterceptor;
        this.mybatisCommonInterceptor = mybatisCommonInterceptor;
        this.sqlSessionFactories = sqlSessionFactories;
    }

    @PostConstruct
    public void addMybatisOperationLogInterceptor(){
        for (SqlSessionFactory factory : sqlSessionFactories) {
            factory.getConfiguration().addInterceptor(mybatisCommonInterceptor);
            factory.getConfiguration().addInterceptor(mybatisOperationLogInterceptor);
        }
    }

}

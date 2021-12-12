package io.github.myifeng.common.interceptor;

import io.github.myifeng.example.entity.BaseEntity;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Properties;
import java.util.UUID;


@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisCommonInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		Object parameter = invocation.getArgs()[1];

		if (parameter instanceof BaseEntity) {
			Class<?> superclass = parameter.getClass().getSuperclass();
			if (SqlCommandType.INSERT.equals(sqlCommandType)) {
				Field id = superclass.getDeclaredField("id");
				id.setAccessible(true);
				if (id.get(parameter) == null) {
					id.set(parameter, UUID.randomUUID().toString().replace("-", ""));
				}

			}
		}

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object o) {
		if (o instanceof Executor) {
			return Plugin.wrap(o, this);
		} else {
			return o;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}

}

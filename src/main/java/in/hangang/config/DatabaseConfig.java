package in.hangang.config;


import org.mybatis.spring.annotation.MapperScan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;



@MapperScan("in.hangang.mapper")
@Configuration
public class DatabaseConfig {

	public class DatabaseConfiguration {
	@Bean
	public DataSourceTransactionManager mybatisTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
}

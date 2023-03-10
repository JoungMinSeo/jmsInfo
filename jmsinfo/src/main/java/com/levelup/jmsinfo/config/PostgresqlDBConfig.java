package com.levelup.jmsinfo.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @purpose postgre DB ?€?  ??Ό
 * 
 * @  ?? ?Ό            ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? λ―Όμ       μ΅μ΄??±
 *
 * @author ? λ―Όμ
 * @since  2023.02.08
 *
 */
@Configuration
@MapperScan(value="com.cyma.bestnhp.mapper.postgre", sqlSessionFactoryRef="postgreSqlSessionFactory")
@EnableTransactionManagement
public class PostgresqlDBConfig {
	
	@Primary
	@Bean(name="postgreDataSource")
	@ConfigurationProperties(prefix="spring.pstgre.datasource")
	public DataSource postgreDataSource() {
		//application.properties?? ? ?? DB ?°κ²? ? λ³΄λ?? λΉλ
		return DataSourceBuilder.create().build();
	}
	
	@Primary
	@Bean(name="postgreSqlSessionFactory")
	public SqlSessionFactory postgreSqlSessionFactory(@Qualifier("postgreDataSource") DataSource postgreDataSource, ApplicationContext applicationContext) throws Exception{
		//?Έ? ??± ?, λΉλ? DataSourceλ₯? ?Έ??κ³? SQLλ¬Έμ κ΄?λ¦¬ν  mapper.xml? κ²½λ‘λ₯? ?? €μ€??€.
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(postgreDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:sqlmap/postgre/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}
	
	@Primary
	@Bean(name="postgreSqlSessionTemplate")
	public SqlSessionTemplate postgreSqlSessionTemplate(SqlSessionFactory postgreSqlSessionFactory) throws Exception{
		return new SqlSessionTemplate(postgreSqlSessionFactory);
	}
	
}

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
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @purpose Oracle DB ?��?�� ?��?��
 * 
 * @  ?��?��?��            ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?��민서       최초?��?��
 *
 * @author ?��민서
 * @since  2023.02.08
 *
 */
@Configuration
@MapperScan(value="com.cyma.bestnhp.mapper.oracle", sqlSessionFactoryRef="oracleSqlSessionFactory")
@EnableTransactionManagement
public class OracleDBConfig {
	
	@Bean(name="oracleDataSource")
	@ConfigurationProperties(prefix="spring.oracle.datasource")
	public DataSource masterDataSource() {
		//application.properties?��?�� ?��?��?�� DB ?���? ?��보�?? 빌드
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="oracleSqlSessionFactory")
	public SqlSessionFactory oracleSqlSessionFactory(@Qualifier("oracleDataSource") DataSource oracleDataSource, ApplicationContext applicationContext) throws Exception{
		//?��?�� ?��?�� ?��, 빌드?�� DataSource�? ?��?��?���? SQL문을 �?리할 mapper.xml?�� 경로�? ?��?���??��.
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(oracleDataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:sqlmap/oracle/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean(name="oracleSqlSessionTemplate")
	public SqlSessionTemplate oracleSqlSessionTemplate(SqlSessionFactory oracleSqlSessionFactory) throws Exception{
		return new SqlSessionTemplate(oracleSqlSessionFactory);
	}
	
}

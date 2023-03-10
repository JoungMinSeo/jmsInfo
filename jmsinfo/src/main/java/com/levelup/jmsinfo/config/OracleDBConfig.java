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
 * @purpose Oracle DB ?€?  ??Ό
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
@MapperScan(value="com.cyma.bestnhp.mapper.oracle", sqlSessionFactoryRef="oracleSqlSessionFactory")
@EnableTransactionManagement
public class OracleDBConfig {
	
	@Bean(name="oracleDataSource")
	@ConfigurationProperties(prefix="spring.oracle.datasource")
	public DataSource masterDataSource() {
		//application.properties?? ? ?? DB ?°κ²? ? λ³΄λ?? λΉλ
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="oracleSqlSessionFactory")
	public SqlSessionFactory oracleSqlSessionFactory(@Qualifier("oracleDataSource") DataSource oracleDataSource, ApplicationContext applicationContext) throws Exception{
		//?Έ? ??± ?, λΉλ? DataSourceλ₯? ?Έ??κ³? SQLλ¬Έμ κ΄?λ¦¬ν  mapper.xml? κ²½λ‘λ₯? ?? €μ€??€.
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

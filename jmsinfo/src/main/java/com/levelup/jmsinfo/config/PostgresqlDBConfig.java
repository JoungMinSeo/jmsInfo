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
 * @purpose postgre DB ?Ñ§?†ï ?åå?ùº
 * 
 * @  ?àò?†ï?ùº            ?àò?†ï?ûê       ?àò?†ï?Ç¥?ö©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?†ïÎØºÏÑú       ÏµúÏ¥à?Éù?Ñ±
 *
 * @author ?†ïÎØºÏÑú
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
		//application.properties?óê?Ñú ?†ï?ùò?ïú DB ?ó∞Í≤? ?†ïÎ≥¥Î?? ÎπåÎìú
		return DataSourceBuilder.create().build();
	}
	
	@Primary
	@Bean(name="postgreSqlSessionFactory")
	public SqlSessionFactory postgreSqlSessionFactory(@Qualifier("postgreDataSource") DataSource postgreDataSource, ApplicationContext applicationContext) throws Exception{
		//?Ñ∏?Öò ?Éù?Ñ± ?ãú, ÎπåÎìú?êú DataSourceÎ•? ?Ñ∏?åÖ?ïòÍ≥? SQLÎ¨∏ÏùÑ Í¥?Î¶¨Ìï† mapper.xml?ùò Í≤ΩÎ°úÎ•? ?ïå?†§Ï§??ã§.
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

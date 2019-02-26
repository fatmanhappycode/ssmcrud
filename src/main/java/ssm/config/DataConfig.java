package ssm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;

@Configuration
/**
 * @ClassName WebConfig
 * @author 肥宅快乐码
 * @date 2019/2/20
 */
public class DataConfig {

  /**
   * 测试用h2数据库
   * @return
   */
  @Profile("test")
  @Bean
  public DataSource embeddedDataSource() {
    return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setScriptEncoding("utf-8")
            .addScript("schema.sql")
            .addScript("test-data.sql")
            .build();
  }

  /**
   * 开发用mysql数据库
   * @return
   * @throws PropertyVetoException
   */
  @Profile("development")
  @Bean
  public DataSource devDataSource() throws PropertyVetoException {
    ComboPooledDataSource cp = new ComboPooledDataSource();
    cp.setJdbcUrl("jdbc:mysql://localhost:3306/ssmcrud?useUnicode=true&characterEncoding=utf-8");
    cp.setDriverClass("com.mysql.jdbc.Driver");
    cp.setUser("root");
    cp.setPassword("");
    return cp;
  }

  @Profile("production")
  @Bean
  public DataSource dataSource() {
    JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
    jndiObjectFactoryBean.setJndiName("jdbc/SpittrDS");
    jndiObjectFactoryBean.setResourceRef(true);
    jndiObjectFactoryBean.setProxyInterface(javax.sql.DataSource.class);
    return (DataSource) jndiObjectFactoryBean.getObject();
  }

  /**
   * 配置SqlSessionFactoryBean
   * @param dataSource
   * @return
   * @throws IOException
   */
  @Bean
  public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
    sqlSessionFactoryBean.setDataSource(dataSource);
    sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*Mapper.xml"));
    return sqlSessionFactoryBean;
  }

  /**
   * 配置DataSourceTransactionManager
   * @param dataSource
   * @return
   */
  @Bean
  public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
    return dataSourceTransactionManager;
  }

  /**
   * 配置MapperScannerConfigurer
   * @return
   */
  @Bean
  public MapperScannerConfigurer mapperScannerConfigurer() {
    MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
    mapperScannerConfigurer.setBasePackage("ssm.dao");
    return mapperScannerConfigurer;
  }

  /**
   * 配置SqlSessionTemplate
   * @param sqlSessionFactory
   * @return
   */
  @Bean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    //ExecutorType.BATCH为批量执行
    return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
  }
}

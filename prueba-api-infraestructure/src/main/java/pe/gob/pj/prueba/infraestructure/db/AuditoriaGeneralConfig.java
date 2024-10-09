package pe.gob.pj.prueba.infraestructure.db;

import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "pe.gob.pj.prueba.infraestructure.db.auditoriageneral.repository",
    entityManagerFactoryRef = "auditoriaEntityManagerFactory",
    transactionManagerRef = "txManagerAuditoriaGeneral")
public class AuditoriaGeneralConfig {

  // CONEXION AUDITORIA GENERAL
  @Bean(name = "cxAuditoriaGeneralDS")
  DataSource auditoriaDataSource() throws NamingException {
    return (DataSource) new InitialContext()
        .lookup("java:jboss/datasources/servicioPruebaAPIAuditoriaGeneral");
  }

  @Bean(name = "auditoriaEntityManagerFactory")
  LocalContainerEntityManagerFactoryBean auditoriaEntityManagerFactory(
      EntityManagerFactoryBuilder builder, @Qualifier("cxAuditoriaGeneralDS") DataSource dataSource) {
    return builder.dataSource(dataSource)
        .packages("pe.gob.pj.prueba.infraestructure.db.auditoriageneral.entity")
        .persistenceUnit("auditoria").properties(getHibernateProperties()).build();
  }

  @Bean(name = "txManagerAuditoriaGeneral")
  PlatformTransactionManager auditoriaTransactionManager(
      @Qualifier("auditoriaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  @Bean(name = "sessionAuditoriaGeneral")
  SessionFactory sessionFactoryAuditoriaGeneral(
      @Qualifier("auditoriaEntityManagerFactory") EntityManagerFactory emf) {
    if (emf.unwrap(SessionFactory.class) == null) {
      throw new NullPointerException("auditoria factory is not a hibernate factory");
    }
    return emf.unwrap(SessionFactory.class);
  }

  private Map<String, Object> getHibernateProperties() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    properties.put("hibernate.show_sql", "true");
    properties.put("hibernate.format_sql", "true");
    properties.put("hibernate.connection.release_mode", "AFTER_TRANSACTION");
    properties.put("hibernate.type", "true");
    return properties;
  }
}

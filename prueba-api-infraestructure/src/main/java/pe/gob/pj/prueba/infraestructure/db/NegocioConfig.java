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
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "pe.gob.pj.prueba.infraestructure.db.negocio.repository",
    entityManagerFactoryRef = "negocioEntityManagerFactory",
    transactionManagerRef = "txManagerNegocio")
public class NegocioConfig {

  // CONEXION CON LA BASE DE DATOS DE NEGOCIO
  @Bean(name = "cxNegocioDS")
  DataSource negocioDataSource() throws NamingException {
    return (DataSource) new InitialContext()
        .lookup("java:jboss/datasources/servicioPruebaAPINegocio");
  }

  @Bean(name = "negocioEntityManagerFactory")
  LocalContainerEntityManagerFactoryBean negocioEntityManagerFactory(
      EntityManagerFactoryBuilder builder, @Qualifier("cxNegocioDS") DataSource dataSource) {
    return builder.dataSource(dataSource)
        .packages("pe.gob.pj.prueba.infraestructure.db.negocio.entity").persistenceUnit("negocio")
        .properties(getHibernateProperties()).build();
  }

  //Para usar transacciones
  @Bean(name = "txManagerNegocio")
  PlatformTransactionManager negocioTransactionManager(
      @Qualifier("negocioEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  //Para usar hibernate con SessionFactory
  @Bean(name = "sessionNegocio")
  SessionFactory sessionFactoryNegocio(
      @Qualifier("negocioEntityManagerFactory") EntityManagerFactory emf) {
    if (emf.unwrap(SessionFactory.class) == null) {
      throw new NullPointerException("negocio factory is not a hibernate factory");
    }
    return emf.unwrap(SessionFactory.class);
  }

  //Para usar querydsl
  @Bean(name = "negocioQDSL")
  JPAQueryFactory jpaQueryFactoryNegocio(
      @Qualifier("negocioEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    return new JPAQueryFactory(entityManager);
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

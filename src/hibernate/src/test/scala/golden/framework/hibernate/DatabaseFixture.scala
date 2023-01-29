package golden.framework.hibernate

import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
import java.util.UUID

trait DatabaseFixture:

  protected def createConnection(): Connection = {
    val dbName = "mem_db_" + UUID.randomUUID.toString.replace("-", "")
    DriverManager.getConnection(s"jdbc:h2:mem:$dbName;database_to_lower=true;ignorecase=true", "sa", "")
  }

  protected def createSessionFactory(mappingResource: String): (SessionFactory, Connection) = {
    val create = true
    val connection = createConnection()
    val properties = new Properties {
      setProperty("hibernate.dialect", classOf[org.hibernate.dialect.H2Dialect].getName)
      setProperty("hibernate.connection.driver_class", classOf[org.h2.Driver].getName)
      setProperty("hibernate.connection.url", connection.getMetaData.getURL)
      setProperty("hibernate.hbm2ddl.auto", if create then "update" else "none")
      setProperty("hibernate.show_sql", "true")
      setProperty("hibernate.globally_quoted_identifiers", "true")
      setProperty("hibernate.connection.username", connection.getMetaData.getUserName)
      setProperty("hibernate.connection.password", "")
      setProperty("hibernate.connection.useUnicode", "true")
      setProperty("hibernate.connection.charSet", "utf8")
      setProperty("hibernate.connection.characterEncoding", "utf8")
    }
    val registryBuilder = new StandardServiceRegistryBuilder().applySettings(properties)
    val serviceRegistry = registryBuilder.build()
    val metadata = new MetadataSources(serviceRegistry)
    val metadataBuilder = metadata.getMetadataBuilder
    metadata.addResource(mappingResource)
    val builder = metadataBuilder.build.getSessionFactoryBuilder
    val factory = builder.build
    factory.withOptions.connection(connection)

    (factory, connection)
  }

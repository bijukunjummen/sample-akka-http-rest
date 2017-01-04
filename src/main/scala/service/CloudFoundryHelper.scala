package service

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

class CloudFoundryHelper(env: Map[String, String]) {

  /**
    * Get the configuration for a service type(p-mysql, p-rabbitmq etc) and a service name
    *
    * @param serviceType
    * @param name
    * @return configuration
    */
  def getConfigFor(serviceType: String, name: String): Config = {
    val vcapServices = env("VCAP_SERVICES")
    val rootConfig = ConfigFactory.parseString(vcapServices)
    val configs = rootConfig.getConfigList(serviceType).asScala
      .filter(_.getString("name") == name)
      .map(instance => instance.getConfig("credentials"))

    if (configs.length > 0) configs.head
    else ConfigFactory.empty()
  }

  /**
    * Determines if the application is running in Cloud Foundry by the presence of
    * VCAP_APPLICATION environment variable in the env.
    *
    * @return true/false
    */
  def inCfCloud(): Boolean = {
    env.contains("VCAP_APPLICATION")
  }
}

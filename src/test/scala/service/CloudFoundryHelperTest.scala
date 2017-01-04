package service

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest._
import org.scalatest.Assertions._

class CloudFoundryHelperTest extends FlatSpec with BeforeAndAfter with GivenWhenThen {

  "A relevant vcap_services" should "be retrievable by name" in {
    Given("A sample VCAP_SERVICES json")
    val sampleVcap = """{
                       |  "p-mysql": [
                       |   {
                       |    "credentials": {
                       |     "hostname": "mysql.local.pcfdev.io",
                       |     "jdbcUrl": "jdbc:mysql://mysql.local.pcfdev.io:3306/aninstance?user=aUser&password=aPass",
                       |     "name": "anInstance",
                       |     "password": "aPass",
                       |     "port": 3306,
                       |     "uri": "mysql://aUser:aPass@mysql.local.pcfdev.io:3306/mydb?reconnect=true",
                       |     "username": "aUser"
                       |    },
                       |    "label": "p-mysql",
                       |    "name": "mydb",
                       |    "plan": "512mb",
                       |    "provider": null,
                       |    "syslog_drain_url": null,
                       |    "tags": [
                       |     "mysql"
                       |    ]
                       |   }
                       |  ]
                       | }
                     """.stripMargin


    When("A helper is created from the root configuration")
    val vcapHelper = new CloudFoundryHelper(Map("VCAP_SERVICES" -> sampleVcap))

    Then("A configuration should be retrievable by the name element")
    val mydbConfig:Config = vcapHelper.getConfigFor("p-mysql", "mydb")

    assert(mydbConfig.getString("hostname") === "mysql.local.pcfdev.io")
    assert(mydbConfig.getInt("port") === 3306)
    assert(mydbConfig.getString("username") === "aUser")
  }

  "Vcap_services with missing name" should "should return an empty " in {
    Given("A sample VCAP_SERVICES json")
    val sampleVcap = """{
                       |  "p-mysql": [
                       |   {
                       |    "credentials": {
                       |     "hostname": "mysql.local.pcfdev.io",
                       |     "jdbcUrl": "jdbc:mysql://mysql.local.pcfdev.io:3306/aninstance?user=aUser&password=aPass",
                       |     "name": "anInstance",
                       |     "password": "aPass",
                       |     "port": 3306,
                       |     "uri": "mysql://aUser:aPass@mysql.local.pcfdev.io:3306/mydb?reconnect=true",
                       |     "username": "aUser"
                       |    },
                       |    "label": "p-mysql",
                       |    "name": "diffname",
                       |    "plan": "512mb",
                       |    "provider": null,
                       |    "syslog_drain_url": null,
                       |    "tags": [
                       |     "mysql"
                       |    ]
                       |   }
                       |  ]
                       | }
                     """.stripMargin

    When("A helper is created from the root configuration")
    val vcapHelper = new CloudFoundryHelper(Map("VCAP_SERVICES" -> sampleVcap))

    When("And a configuration retrieved by name")
    val mydbConfig:Config = vcapHelper.getConfigFor("p-mysql", "mydb")

    Then("The expected paths should be missing")
    assert(mydbConfig.hasPath("hostname") === false)
    assert(mydbConfig.hasPath("port") === false)
    assert(mydbConfig.hasPath("username") === false)
  }

  "Mangled VCAP_SERVICES" should "should return an empty Config" in {
    Given("A sample VCAP_SERVICES json")
    val sampleVcap = """{
                       |  "p-mysql": [
                       |   
                       |    "credentials": {
                       | }
                     """.stripMargin

    When("Parsing a mangled json string")
    val vcapHelper = new CloudFoundryHelper(Map("VCAP_SERVICES" -> sampleVcap))

    Then("An exception should be propagated up")
    assertThrows[Exception](vcapHelper.getConfigFor("test", "test"))
  }


  "Vcap services helper" should "test if deployed in CF" in {
    Given("An environment map with VCAP_APPLICATION entry")
    val map = Map("VCAP_APPLICATION" -> "some string")
    val vcapHelper = new CloudFoundryHelper(map)

    When("tested if deployed to CF")
    Then("should work")
    assert(vcapHelper.inCfCloud())
    assert(new CloudFoundryHelper(Map()).inCfCloud() === false)
  }
}

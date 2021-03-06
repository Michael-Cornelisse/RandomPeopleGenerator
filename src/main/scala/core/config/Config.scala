package core.config

import com.typesafe.config.ConfigFactory

/**
 * Main config file that gets the required config variables from the application.conf file
 */

trait Config {
  private val config = ConfigFactory.load()

  lazy val serverInterface: String = config.getString("my-app.http.interface")
  lazy val serverPort: Int = config.getInt("my-app.http.port")

  lazy val dbUrl: String = config.getString("my-app.db.url")
  lazy val dbUser: String = config.getString("my-app.db.user")
  lazy val dbPassword: String = config.getString("my-app.db.password")
}

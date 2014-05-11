package skinny

import scalikejdbc._, config._
import skinny.exception._

trait DBSettings {
  DBSettings.initialize()
}

/**
 * Skinny ORM Database settings initializer.
 *
 * @see https://github.com/seratch/scalikejdbc
 */
object DBSettings {

  private[this] case class State(var alreadyInitialized: Boolean = false)
  private[this] val state: State = State()

  /**
   * Initializes DB settings.
   */
  def initialize(force: Boolean = false): Unit = {
    state.synchronized {
      if (force || !state.alreadyInitialized) {
        SkinnyEnv.get().map(env => DBsWithEnv(env).setupAll()).getOrElse {
          if (!TypesafeConfigReaderWithEnv(SkinnyEnv.Development).dbNames.isEmpty) {
            DBsWithEnv(SkinnyEnv.Development).setupAll()
          } else if (!TypesafeConfigReader.dbNames.isEmpty) {
            DBs.setupAll()
          } else {
            throw new DBSettingsException(s"""
        | ---------------------------------------------
        |
        |  !!! Skinny Configuration Error !!!
        |
        |  DB settings was not found. Add some db settings to src/main/resources/application.conf like this:
        |
        |   development {
        |     db {
        |       default {
        |         driver="org.h2.Driver"
        |         url="jdbc:h2:mem:example"
        |         user="sa"
        |         password="sa"
        |         poolInitialSize=2
        |         poolMaxSize=10
        |       }
        |     }
        |   }
        |
        |
        |  "development" is the default env value. If you don't need env prefix it also works.
        |  You can pass env value from environment variables "export skinny.env=qa" or via JVM option like "-Dskinny.env=qa".
        |  Though you're not forced to use these values, recommended env values are "development", "test", "qa" and "production".
        |
        | ---------------------------------------------
        |""".stripMargin)
          }
        }
        state.alreadyInitialized = true
      }
    }
  }

  /**
   * Wipes out all DB settings.
   */
  def destroy() = DBs.closeAll()

}


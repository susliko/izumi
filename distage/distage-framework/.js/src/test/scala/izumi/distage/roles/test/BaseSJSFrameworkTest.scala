package izumi.distage.roles.test

import izumi.distage.plugins.{PluginConfig, PluginDef}
import izumi.distage.roles.RoleAppMain
import izumi.distage.roles.RoleAppMain.ArgV
import izumi.distage.roles.model.definition.RoleModuleDef
import izumi.distage.roles.test.fixtures.TestTask00
import izumi.functional.bio.UnsafeRun2
import izumi.fundamentals.platform.cli.model.raw.RawRoleParams
import org.scalatest.wordspec.AsyncWordSpec
import zio.internal.Platform

import scala.concurrent.ExecutionContext

class BaseSJSFrameworkTest extends AsyncWordSpec {
  "distage-framework" should {
    "run on cats.effect.IO" in {

      val main = new RoleAppMain[cats.effect.IO]() {
        override protected def pluginConfig: PluginConfig = PluginConfig.const(
          Seq(
            new PluginDef with RoleModuleDef {
              makeRole[TestTask00[cats.effect.IO]]
              make[ExecutionContext].named("cpu").fromValue(executionContext)
            }
          )
        )

        override protected def requiredRoles(argv: ArgV): Vector[RawRoleParams] = Vector(RawRoleParams("testtask00"))
      }
      for {
        _ <- main.main()
      } yield {
        assert(true)
      }
    }

    "run on zio.Task" in {
      val platform = Platform.fromExecutionContext(executionContext)
      val r = UnsafeRun2.createZIO[Any](platform, ())

      val main = new RoleAppMain[zio.Task]() {
        override protected def pluginConfig: PluginConfig = PluginConfig.const(
          Seq(
            new PluginDef with RoleModuleDef {
              makeRole[TestTask00[zio.Task]]
              make[ExecutionContext].named("cpu").from(executionContext)

              make[UnsafeRun2[zio.IO]].fromValue(r)
              make[Platform].fromValue(platform)
              make[zio.Runtime[Any]].fromValue(r.runtime)
            }
          )
        )

        override protected def requiredRoles(argv: ArgV): Vector[RawRoleParams] = Vector(RawRoleParams("testtask00"))
      }
      for {
        _ <- main.main()
      } yield {
        assert(true)
      }
    }
  }
}

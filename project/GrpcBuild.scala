import sbt._
import Keys._

import sbtprotobuf.ProtobufPlugin._

object ProjectBuild extends Build {

  val grpcVersion = "0.7.1"

  def mkProject(module: String, name: String) =
    Project(name, file(module), settings = Seq(
      organization  := "io.grpc",
      // TODO Enable after first stable release
      // version       := (s"git describe --long --tags --match v${grpcVersion}" !!).trim.drop(1),
      version       := (s"git describe --long --tags --match v0.0" !!).trim.drop(1),
      scalaVersion  := "2.11.6",
      scalacOptions ++= Seq("-feature","-deprecation", "-Xlint")
    ))

  lazy val root = Project("root", file("."))
    .aggregate(library, examples)

  lazy val library = mkProject("library", "grpc-scala")
    .settings(libraryDependencies += "io.grpc" % "grpc-all" % grpcVersion)

  lazy val examples = mkProject("examples", "grpc-scala-examples").dependsOn(library)
    .settings(protobufSettings:_*)
    .settings(
      version in protobufConfig := "3.0.0-alpha-3.1",
      protocOptions in protobufConfig ++= Seq(
        "--plugin=protoc-gen-java_rpc=/usr/local/bin/protoc-gen-grpc-java",
        "--java_rpc_out=examples/target/scala-2.11/src_managed/main/compiled_protobuf"
      )
    )
}


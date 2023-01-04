@file:Suppress("UnstableApiUsage")

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.7.21"
  kotlin("plugin.serialization") version "1.7.21"
  application
}


allprojects {
  apply {
    plugin("org.jetbrains.kotlin.jvm")
  }

  repositories {
    mavenCentral()
  }

  dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.16")
  }

  kotlin {
    jvmToolchain(17)
  }

  testing {
    suites {
      val test by getting(JvmTestSuite::class) {
        useKotlinTest()
      }
    }
  }
}

dependencies {
  implementation(platform(kotlin("bom")))
  implementation(project(":model"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

  fun http4k(module: String, version: String? = null) =
    "org.http4k:http4k-$module${version?.let { ":$it" } ?: ""}"
  implementation(platform(http4k("bom", version = "4.34.4.0")))
  implementation(http4k("core"))
  implementation(http4k("server-jetty"))
  implementation(http4k("format-kotlinx-serialization"))
  implementation(http4k("contract"))

  testImplementation(http4k("client-okhttp"))
}

testing {
  suites {
    val test by getting(JvmTestSuite::class) {
      useKotlinTest()
    }
  }
}

application {
  mainClass.set("com.omricat.countdown.server.AppKt")
}

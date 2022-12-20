@file:Suppress("UnstableApiUsage")

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.7.21"
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
    jvmToolchain(11)
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

@file:Suppress("UnstableApiUsage")

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.7.21"
  application
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.16")
}

testing {
  suites {
    val test by getting(JvmTestSuite::class) {
      // Use Kotlin Test test framework
      useKotlinTest()
    }
  }
}

application {
  mainClass.set("com.omricat.countdown.server.AppKt")
}

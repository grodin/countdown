plugins {
  kotlin("plugin.serialization")
  application
}

dependencies {
  implementation(project(":model"))
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.4.1")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}

kotlin {
  explicitApi()
}

application {
  this.mainClass.set("com.omricat.countdown.preprocessor.PreProcessor")
}

val run by tasks.getting(JavaExec::class) {
  standardInput = System.`in`
}

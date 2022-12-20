plugins {
  kotlin("plugin.serialization")
}
dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.4.1")
}

kotlin {
  explicitApi()
}

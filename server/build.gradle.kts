import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.springframework.boot)
  alias(libs.plugins.spring.dependency.management)
  kotlin("jvm")  version libs.versions.kotlin.version
  kotlin("plugin.spring")  version libs.versions.kotlin.version
  alias(libs.plugins.manes)
  alias(libs.plugins.littlerobots)
}

group = "com.mobilejazz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}


dependencies {
  //	Spring Boot
  implementation(libs.spring.boot.starter.web)
  implementation(libs.spring.boot.starter.thymeleaf)
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.devtools)
  implementation(libs.spring.boot.configuration.processor)

  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.stdlib.jdk8)

  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.okhttp)

  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.kotlinx.coroutines.reactor)
  implementation(libs.kotlinx.serialization.json)

  implementation(libs.opencsv)

  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.mockk)
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

springBoot {
  mainClass.set("com.mobilejazz.colloc.CollocApplicationKt")
}

// disable a new configuration in spring boot which generate multiple jar files
tasks.named<Jar>("jar") {
  enabled = false
}

//region version-catalog update configuration
fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}

// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
  rejectVersionIf {
    isNonStable(candidate.version) && !isNonStable(currentVersion)
  }
}

versionCatalogUpdate {
  // sort the catalog by key (default is true)
  sortByKey.set(false)
  keep {
    // keep versions without any library or plugin reference
    keepUnusedVersions.set(true)
    // keep all libraries that aren't used in the project
    keepUnusedLibraries.set(false)
    // keep all plugins that aren't used in the project
    keepUnusedPlugins.set(false)
  }
}
//endregion version-catalog update configuration

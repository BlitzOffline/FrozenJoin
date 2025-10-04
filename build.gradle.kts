import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "9.2.2"
    kotlin("jvm") version "2.2.20"
}

group = "com.github.frcsty"
version = "2.3.2-builds"

val libsPath = "com.github.frcsty.frozenjoin"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://jitpack.io/")
}

dependencies {
    implementation(projects.actionLib)
    implementation(libs.triumph.cmds)
    implementation(libs.bstats)

    compileOnly(libs.paper)
    compileOnly(libs.papi)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

tasks {
    withType<ProcessResources> {
        filesMatching(listOf("plugin.yml", "config.yml")) {
            expand("version" to project.version)
        }
    }

    withType<ShadowJar> {
        relocate("org.bstats", "${libsPath}.bstats")
        relocate("me.mattstudios.mf", "${libsPath}.mf-utils")
        relocate("kotlin", "${libsPath}.kotlin")
        dependencies {
            exclude(dependency("org.jetbrains:annotations"))
        }

        archiveClassifier.set(null as String?)
    }
}

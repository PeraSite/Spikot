import org.jetbrains.dokka.ExternalDocumentationLink

plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("kapt") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
    id("org.jetbrains.dokka") version "1.4.20"
    id("maven-publish")
}

val kotlin_version = "1.4.21"
val coroutine_version = "1.4.1"
val paper_version = "1.16.2-R0.1-SNAPSHOT"
val serialization_version = "1.0.1"

group = "kr.heartpattern"
version = "4.3.0-SNAPSHOT"

repositories {
    maven("https://maven.heartpattern.io/repository/maven-public/")
    maven("https://jitpack.io")
    mavenLocal()
}

dependencies {
    // Kotlin family
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    api("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutine_version")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

    // compile(dependencies)
    api("io.github.microutils:kotlin-logging:2.0.4")
    api("com.esotericsoftware.yamlbeans:yamlbeans:1.15")
    api("net.swiftzer.semver:semver:1.1.1")
    api("com.github.salomonbrys.kotson:kotson:2.5.0")
    api("org.slf4j:slf4j-jdk14:1.7.30")
    api("com.charleskorn.kaml:kaml:0.26.0")

    // compile(only dependencies)
    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc7")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.6.0-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-api:$paper_version")
    compileOnly("org.spigotmc:plugin-annotations:1.2.0-SNAPSHOT") {
        exclude("org.bukkit", "bukkit")
    }

    // Test
    testApi("org.jetbrains.kotlin:kotlin-test:1.4.0")
    testApi("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

    // KAPT
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    kapt("org.spigotmc:plugin-annotations:1.2.0-SNAPSHOT")
    kapt("kr.heartpattern:SpikotAnnotationProcessor:4.0.0-SNAPSHOT")
    kapt("kr.heartpattern:SpikotClassLocator:4.0.0-SNAPSHOT")
}

configurations {
    testCompile.configure {
        extendsFrom(compileOnly.get())
        extendsFrom(api.get())
    }
}

configurations.api.get().isCanBeResolved = true

tasks {
    test {
        useJUnitPlatform()
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-XXLanguage:+InlineClasses",
                "-Xuse-experimental=kotlin.Experimental"
            )
        }
    }

    create<Jar>("createPlugin") {
        archiveFileName.set("Spikot-Plugin-${archiveVersion.get()}.jar")
        from(
            configurations.api.get().map {
                if (it.isDirectory)
                    it
                else
                    zipTree(it)
            }
        )
        with(jar.get())
    }

    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("./build/kdoc/"))
    dokkaSourceSets {
        ExternalDocumentationLink(
            "https://hub.spigotmc.org/javadocs/spigot/",
            "https://hub.spigotmc.org/javadocs/spigot/package-list"
        )
    }
}

// Load local.gradle.kts if exists
if (File("local.gradle.kts").exists())
    apply("local.gradle.kts")

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "Spikot"
            from(components["java"])
//            artifact(tasks["dokkaJar"])
            artifact(tasks["sourcesJar"])
        }
    }
    repositories {
        if ("nexusUser" in properties && "nexusPassword" in properties) {
            maven(
                if (version.toString().endsWith("SNAPSHOT"))
                    "https://maven.heartpattern.kr/repository/maven-public-snapshots/"
                else
                    "https://maven.heartpattern.kr/repository/maven-public-releases/"
            ) {
                credentials {
                    username = properties["nexusUser"] as String
                    password = properties["nexusPassword"] as String
                }
            }
        }
    }
}

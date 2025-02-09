import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.yuhtin.lauren"
version = "3.0.0-BETA"

application {
    mainClass = "com.yuhtin.lauren.Startup"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.lavalink.dev/snapshots")
    maven("https://maven.lavalink.dev/releases")
    maven("https://maven.topi.wtf/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:5.1.0") {
        exclude(group = "org.apache.logging.log4j")
    }

    implementation("com.google.guava:guava:32.0.0-android")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")

    implementation("org.mongodb:mongodb-driver-sync:4.11.0")

    implementation("dev.lavalink.youtube:v2:1.7.2")
    implementation("dev.arbjerg:lavaplayer:0eaeee195f0315b2617587aa3537fa202df07ddc-SNAPSHOT")
    implementation("com.github.TopiSenpai.LavaSrc:lavasrc:3.2.10")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    archiveFileName.set("bot.jar")

    println("Shadowing ${project.name} to ${destinationDirectory.get()}")
}

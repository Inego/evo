import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.2.30"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlinVersion))
    }
}

group = "org.inego.evo"
version = "1.0-SNAPSHOT"

apply {
    plugin("java")
    plugin("kotlin")
}

val kotlinVersion: String by extra

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlinVersion))
    testCompile("org.jetbrains.kotlin:kotlin-test")
    testCompile("org.jetbrains.kotlin:kotlin-test-junit")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.0.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
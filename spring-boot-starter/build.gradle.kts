plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm")
}

group = "ge.nika"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(":core")

    implementation("org.springframework:spring-context:6.2.2")
    implementation("org.springframework:spring-core:6.2.2")
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.4.3")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
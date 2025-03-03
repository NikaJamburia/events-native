plugins {
    id("java")
    kotlin("jvm")
}

group = "ge.nika"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.15.2")
    implementation(kotlin("stdlib-jdk8"))

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
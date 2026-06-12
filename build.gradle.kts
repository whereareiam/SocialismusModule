import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    `maven-publish`
    alias(libs.plugins.shadow)
}

defaultTasks("shadowJar")

group = "me.whereareiam"
version = System.getenv("VERSION") ?: "dev"

repositories {
    mavenCentral()
    maven("https://maven.whereareiam.me/release")
    maven("https://maven.whereareiam.me/development")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    compileOnly(libs.socialismus.module.api)

    // lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    // general
    compileOnly(libs.annotations)
    compileOnly(libs.guice)

    // test
    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.junit.platform)
}

tasks.named<Copy>("processResources") {
    filter<ReplaceTokens>(
        "tokens" to mapOf(
            "projectName" to rootProject.name,
            "projectVersion" to project.version
        )
    )
}

tasks.withType<ShadowJar>().configureEach {
    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("")
}

tasks.named<Jar>("jar").configure {
    dependsOn(tasks.named("shadowJar"))
}

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            val realm = (System.getenv("PUBLISH_REALM")
                ?: if ((System.getenv("VERSION") ?: "dev").contains("dev", true)) "development" else "release")
                .lowercase()
            url = uri("https://maven.whereareiam.me/$realm")
            credentials {
                username = System.getenv("PUBLISH_USER") ?: ""
                password = System.getenv("PUBLISH_TOKEN") ?: ""
            }
        }
    }
}

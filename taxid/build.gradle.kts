import at.asitplus.gradle.Logger
import at.asitplus.gradle.setupDokka

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("at.asitplus.gradle.conventions")
    id("org.jetbrains.dokka")
    id("signing")
}

/* required for maven publication */
val artifactVersion: String by extra
group = "at.asitplus.wallet"
version = artifactVersion

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                api("at.asitplus.wallet:vck:5.2.2")
            }
        }
    }
}

val javadocJar = setupDokka(baseUrl = "https://github.com/a-sit-plus/tax-id-credential/tree/main/")

//catch the missing `signMavenPublication` Task, which slips through for reasons unknown
afterEvaluate {
    val signTasks = tasks.filter { it.name.startsWith("sign") }
    tasks.filter { it.name.startsWith("publish") }.forEach {
        Logger.lifecycle("   * ${it.name} now depends on ${signTasks.joinToString { it.name }}")
        it.dependsOn(*signTasks.toTypedArray())
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set("Tax ID Credential")
                description.set("Tax ID Verifiable Credential")
                url.set("https://github.com/a-sit-plus/tax-id-credential/")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("JesusMcCloud")
                        name.set("Bernd Prünster")
                        email.set("bernd.pruenster@a-sit.at")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:a-sit-plus/tax-id-credential.git")
                    developerConnection.set("scm:git:git@github.com:a-sit-plus/tax-id-credential.git")
                    url.set("https://github.com/a-sit-plus/tax-id-credential/")
                }
            }
        }
    }
    repositories {
        mavenLocal {
            signing.isRequired = false
        }
    }
}

repositories {
    mavenCentral()
    maven(url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
}

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
}


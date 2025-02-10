pluginManagement {
    repositories {
        maven {
            url = uri("https://raw.githubusercontent.com/a-sit-plus/gradle-conventions-plugin/mvn/repo")
            name = "aspConventions"
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots") //KOTEST snapshot
    }
}

if (System.getProperty("regressionTest") == "true") {
    includeBuild("../signum") {
        dependencySubstitution {
            substitute(module("at.asitplus.wallet:indispensable")).using(project(":indispensable"))
            substitute(module("at.asitplus.signum:indispensable-josef")).using(project(":indispensable-josef"))
            substitute(module("at.asitplus.signum:indispensable-cosef")).using(project(":indispensable-cosef"))
            substitute(module("at.asitplus.signum:supreme")).using(project(":supreme"))
        }
    }
    includeBuild("..") {
        dependencySubstitution {
            substitute(module("at.asitplus.wallet:vck")).using(project(":vck"))
            substitute(module("at.asitplus.wallet:vck-openid")).using(project(":vck-openid"))
            substitute(module("at.asitplus.wallet:vck-openid-ktor")).using(project(":vck-openid-ktor"))
            substitute(module("at.asitplus.wallet:openid-data-classes")).using(project(":openid-data-classes"))
        }
    }
}


rootProject.name = "tax-id"
include(":taxid")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven ( "https://seeso.jfrog.io/artifactory/visualcamp-seeso-android-gradle-release/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( "https://seeso.jfrog.io/artifactory/visualcamp-seeso-android-gradle-release/")
    }
}

rootProject.name = "PruebasATET"
include(":app")
 
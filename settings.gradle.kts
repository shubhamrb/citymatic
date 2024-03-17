pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jcenter.bintray.com/") }
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://maven.cashfree.com/release") }
        maven {
            setUrl("https://artifactory.paytm.in/libs-release-local")
        }
    }
}



rootProject.name = "CityMatic"
include(":app")
 
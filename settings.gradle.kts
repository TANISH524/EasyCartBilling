pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.3.0" apply false
        id("com.android.library") version "8.3.0" apply false
        kotlin("android") version "1.9.0" apply false
        id("kotlin-kapt") version "1.9.0" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "RestaurantManagement"
include(":app")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Enforce settings repositories
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ToDo"
include(":app")

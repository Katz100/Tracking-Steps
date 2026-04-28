pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Tracking-Steps"
include(":app")
include(":permissions-details")
include(":home")
project(":home").projectDir = file("feature/home")
include(":core")
include(":goals")
project(":goals").projectDir = file("feature/goals")
include(":settings")
project(":settings").projectDir = file("feature/settings")
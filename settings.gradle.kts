pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "DroidKit"

include(
    ":apps:showcase",
    ":core:theme",
    ":core:foundation",
    ":registry",
)

project(":apps:showcase").projectDir = file("apps/showcase")
project(":core:theme").projectDir = file("core/theme")
project(":core:foundation").projectDir = file("core/foundation")
project(":registry").projectDir = file("registry")

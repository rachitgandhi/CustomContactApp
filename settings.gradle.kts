pluginManagement {
    repositories {
        maven { url = uri("https://nexus-ci.onefiserv.net/repository/Maven_Central")
            credentials {
                username = System.getenv("NEXUS_USERNAME")
                password = System.getenv("NEXUS_ACCESS_TOKEN")
            }
        }
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
        maven { url = uri("https://nexus-ci.onefiserv.net/repository/Maven_Central")
            credentials {
                username = System.getenv("NEXUS_USERNAME")
                password = System.getenv("NEXUS_ACCESS_TOKEN")
            }
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "ContactsApp"
include(":app")
 
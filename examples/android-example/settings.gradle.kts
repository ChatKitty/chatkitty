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
        maven("https://jitpack.io")
    }
}

rootProject.name = "ChatUiExample"
include(":chat-ui-example")
include(":chat-ui")
include(":flex-hybrid-app")
project(":chat-ui").projectDir = file("../../libraries/android/chat-ui")
project(":flex-hybrid-app").projectDir = file("../../libraries/android/flex-hybrid-app")


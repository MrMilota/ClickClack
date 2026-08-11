pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")                // fabric-loom
        maven("https://maven.kikugie.dev/releases")         // stonecutter
        maven("https://maven.shedaniel.me/")                // cloth-config
        maven("https://maven.terraformersmc.com/releases/") // modmenu
        gradlePluginPortal()
        mavenCentral()
    }
    val loomVersion = providers.gradleProperty("loom_version").get()

    plugins {
        id("net.fabricmc.fabric-loom") version loomVersion
        id("net.fabricmc.fabric-loom-remap") version loomVersion
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.7"
}


stonecutter {
    create(rootProject) {
        versions(
            "1.21.1",
            "1.21.4",
            "1.21.6",
            "1.21.8",
            "1.21.11",
            "26.1", // не работает и хз почему
            "26.2"
        )
        vcsVersion = "26.2"
    }
}

rootProject.name = "clickclack"

import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("net.fabricmc.fabric-loom") apply false
    id("net.fabricmc.fabric-loom-remap") apply false
    id("maven-publish")
    id("dev.kikugie.stonecutter")
}

val mcVersion = stonecutter.current.version

val unobfuscated = stonecutter.eval(mcVersion, ">=26.1")

apply(plugin = if (unobfuscated) "net.fabricmc.fabric-loom" else "net.fabricmc.fabric-loom-remap")

val loomExtension = extensions.getByType<LoomGradleExtensionAPI>()

val newInputApi = (findProperty("uses_new_input_api") as String? ?: "true").toBoolean()

stonecutter.constants["new_input_api"] = newInputApi

stonecutter.constants["new_render_api"] = unobfuscated

stonecutter.constants["new_resource_location"] = stonecutter.eval(mcVersion, ">=1.21.11")

version = "${property("mod_version")}+mc$mcVersion"
group = property("maven_group") as String

stonecutter {
    replacements.string(eval(current.version, "<26.1")) {
        replace("GuiGraphicsExtractor", "GuiGraphicsExtractor")
        replace("GuiGraphicsExtractor", "GuiGraphics")
    }
    replacements.string(eval(current.version, "<1.21.11")) {
        replace("Identifier", "ResourceLocation")
        replace("Identifier", "Identifier")
    }
}

extensions.configure<BasePluginExtension> {
    archivesName.set(property("archives_base_name") as String)
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

fun DependencyHandlerScope.modOrPlain(configuration: String, dep: String) {
    val cfg = if (unobfuscated) configuration else "mod${configuration.replaceFirstChar { it.uppercase() }}"
    add(cfg, dep)
}

dependencies {
    compileOnly("org.projectlombok:lombok:${property("lombok_version")}")
    annotationProcessor("org.projectlombok:lombok:${property("lombok_version")}")

    add("minecraft", "com.mojang:minecraft:$mcVersion")

    if (!unobfuscated) {
        add("mappings", loomExtension.officialMojangMappings())
        // mappings("net.fabricmc:yarn:${property("yarn_version")}:v2")
    }

    modOrPlain("implementation", "net.fabricmc:fabric-loader:${property("loader_version")}")
    modOrPlain("implementation", "net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    modOrPlain("api", "me.shedaniel.cloth:cloth-config-fabric:${property("cloth_config_version")}")
    modOrPlain("api", "com.terraformersmc:modmenu:${property("modmenu_version")}")
}

tasks.named<ProcessResources>("processResources") {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", mcVersion)
    inputs.property("loader_version", project.property("loader_version")!!)

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to mcVersion,
            "loader_version" to project.property("loader_version")!!
        )
    }

    filesMatching("clickclack.mixins.json") {
        expand("mixin_compat" to project.property("mixin_compat")!!)
    }
}

val javaVersion = (property("java_version") as String).toInt()

tasks.withType<JavaCompile>().configureEach {
    options.release.set(javaVersion)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
}

tasks.named<Jar>("jar") {
    from("LICENSE") {
        rename { "${it}_${property("archives_base_name")}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = property("archives_base_name") as String
            from(components["java"])
        }
    }
}

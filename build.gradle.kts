import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
  kotlin("jvm") version "1.8.21"
  id("com.ncorti.ktfmt.gradle") version "0.12.0"
  id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "se.rsrp"
version = "1.0.0"

repositories {
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
  maven("https://papermc.io/repo/repository/maven-public/")
  mavenCentral()
}

dependencies {
  compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
  implementation("net.kyori:adventure-text-minimessage:4.13.1")
  implementation("net.kyori:adventure-platform-bukkit:4.3.0")
  implementation("io.papermc:paperlib:1.0.8")
}

kotlin {
    jvmToolchain(8)
}

tasks.withType(ShadowJar::class.java) {
  archiveClassifier.set("")
  dependencies {
    exclude(dependency("org.spigotmc:spigot-api:"))
    exclude(dependency("org.jetbrains.kotlin::"))
  }
  minimize()
}

ktfmt { kotlinLangStyle() }

bukkit {
  // Default values can be overridden if needed
  name = "HomeTeleport"
  version = "1.0.0"
  description = "Allow players to teleport to their bed or world spawn."
  website = "https://github.com/rosengrenen/HomeTeleport"
  author = "rosengrenen"

  // Plugin main class (required)
  main = "se.rsrp.hometeleport.Main"

  // API version (should be set for 1.13+)
  apiVersion = "1.13"

  // Other possible properties from plugin.yml (optional)
  // load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
  // authors = listOf()
  // contributors = listOf()
  depend = listOf("Kotlin")
  // softDepend = listOf()
  // loadBefore = listOf()
  // prefix = ""
  // defaultPermission = BukkitPluginDescription.Permission.Default.OP
  // provides = listOf()

  commands {
      register("home") {
        description = "Teleports the player home."
        permission = "hometeleport.commands.home"
      }
      register("hometeleport") {
        description = "Management command."
        permission = "hometeleport.commands.hometeleport"
      }
  }

  permissions {
    register("hometeleport.reload") {
      description = "Allows reloading the config."
      default = BukkitPluginDescription.Permission.Default.OP
    }
    register("hometeleport.ignorecooldown") {
      description = "Allows player to ignore the cooldown between teleports."
      default = BukkitPluginDescription.Permission.Default.OP
    }
    register("hometeleport.commands.home") {
      description = "Allows teleportation to bed/spawn."
      default = BukkitPluginDescription.Permission.Default.TRUE
    }
    register("hometeleport.commands.hometeleport") {
      description = "Allows the usage of the management command /hometeleport."
      default = BukkitPluginDescription.Permission.Default.OP
    }
  }
}
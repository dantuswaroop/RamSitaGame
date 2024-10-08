// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("com.google.devtools.ksp") version "2.0.10-RC-1.0.23"
    alias(libs.plugins.compose.compiler) apply  false
    alias(libs.plugins.hilt.android) apply false


}
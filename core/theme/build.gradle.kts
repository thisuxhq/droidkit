plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.droidkit.core.theme"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    sourceSets {
        named("main") {
            kotlin.directories.add(
                layout.buildDirectory
                    .dir("generated/themeMain")
                    .get()
                    .asFile
                    .absolutePath,
            )
        }
    }
}

kotlin {
    jvmToolchain(17)
}

val themeMainSources = layout.buildDirectory.dir("generated/themeMain")

val syncThemeMain by tasks.registering(Sync::class) {
    from("${rootDir}/registry/theme")
    include("**/*.kt")
    exclude("**/*Test.kt")
    into(themeMainSources)
}

tasks.named("preBuild") {
    dependsOn(syncThemeMain)
}

afterEvaluate {
    tasks.matching { it.name.startsWith("compile") && it.name.contains("Kotlin") }.configureEach {
        dependsOn(syncThemeMain)
    }
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.tooling.preview)
    debugApi(libs.androidx.compose.ui.tooling)
}

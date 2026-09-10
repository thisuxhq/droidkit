plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.droidkit.registry"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    sourceSets {
        named("main") {
            kotlin.directories.add(
                layout.buildDirectory
                    .dir("generated/registryMain")
                    .get()
                    .asFile
                    .absolutePath,
            )
        }
        named("test") {
            kotlin.directories.add(
                layout.buildDirectory
                    .dir("generated/registryTest")
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

val registryMainSources = layout.buildDirectory.dir("generated/registryMain")
val registryTestSources = layout.buildDirectory.dir("generated/registryTest")

val syncRegistryMain by tasks.registering(Sync::class) {
    from("components")
    from("patterns")
    from("blocks")
    include("**/*.kt")
    exclude("**/*Test.kt")
    exclude("**/*ScreenshotTest.kt")
    into(registryMainSources)
}

val syncRegistryTest by tasks.registering(Sync::class) {
    from("components")
    from("patterns")
    from("blocks")
    include("**/*Test.kt")
    include("**/*ScreenshotTest.kt")
    into(registryTestSources)
}

tasks.named("preBuild") {
    dependsOn(syncRegistryMain, syncRegistryTest)
}

afterEvaluate {
    tasks.matching { it.name.startsWith("compile") && it.name.contains("Test") }.configureEach {
        dependsOn(syncRegistryTest)
    }
    tasks.matching { it.name.startsWith("compile") && it.name.contains("Kotlin") }.configureEach {
        dependsOn(syncRegistryMain)
    }
}

val lintRegistryImports by tasks.registering {
    group = "verification"
    description = "Registry Kotlin may import only com.droidkit.registry.*, Compose, Material 3, AndroidX, and the JDK."
    val sourceRoot = layout.projectDirectory.asFile
    val filesToLint =
        fileTree(sourceRoot) {
            include("theme/**/*.kt", "components/**/*.kt", "patterns/**/*.kt", "blocks/**/*.kt")
            exclude("**/*Test.kt", "**/*ScreenshotTest.kt", "build/**")
        }.files
            .toList()
    inputs.files(filesToLint)
    doLast {
        val allowed =
            listOf(
                "com.droidkit.registry.",
                "androidx.",
                "android.",
                "kotlin.",
                "kotlinx.",
                "java.",
                "javax.",
            )
        val violations = mutableListOf<String>()
        filesToLint.forEach { file ->
            file.readLines().forEachIndexed { index, line ->
                val trimmed = line.trim()
                if (trimmed.startsWith("import ")) {
                    val imported = trimmed.removePrefix("import ").trim()
                    if (allowed.none { imported.startsWith(it) }) {
                        violations += "${file.relativeTo(sourceRoot)}:${index + 1}: $imported"
                    }
                }
            }
        }
        check(violations.isEmpty()) {
            "registry import lint failed:\n${violations.joinToString("\n")}"
        }
    }
}

tasks.named("check") {
    dependsOn(lintRegistryImports)
}

dependencies {
    api(project(":core:theme"))
    api(project(":core:foundation"))

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    testImplementation(composeBom)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.androidx.compose.ui)
    testImplementation(libs.robolectric)
}

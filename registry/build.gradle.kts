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
        getByName("main") {
            kotlin.srcDirs(
                "components",
                "patterns",
                "blocks",
            )
            kotlin.exclude("**/*Test.kt")
            kotlin.exclude("**/*ScreenshotTest.kt")
        }
        getByName("test") {
            kotlin.srcDirs(
                "components",
                "patterns",
                "blocks",
            )
            kotlin.include("**/*Test.kt")
            kotlin.include("**/*ScreenshotTest.kt")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

val lintRegistryImports by tasks.registering {
    group = "verification"
    description = "Registry Kotlin may import only com.droidkit.registry.*, Compose, Material 3, AndroidX, and the JDK."
    val roots = listOf(
        layout.projectDirectory.dir("theme"),
        layout.projectDirectory.dir("components"),
        layout.projectDirectory.dir("patterns"),
        layout.projectDirectory.dir("blocks"),
    )
    inputs.files(
        files(roots).asFileTree.matching {
            include("**/*.kt")
            exclude("**/*Test.kt")
            exclude("**/*ScreenshotTest.kt")
        },
    )
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
        inputs.files.forEach { file ->
            file.readLines().forEachIndexed { index, line ->
                val trimmed = line.trim()
                if (trimmed.startsWith("import ")) {
                    val imported = trimmed.removePrefix("import ").trim()
                    if (allowed.none { imported.startsWith(it) }) {
                        violations += "${file.relativeTo(projectDir)}:${index + 1}: $imported"
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
    testImplementation(libs.robolectric)
}

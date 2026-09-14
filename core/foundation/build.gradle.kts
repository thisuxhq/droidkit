plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.droidkit.core.foundation"
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
                    .dir("generated/foundationMain")
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

// registry/foundation is the copyable item (decisions #20); this module compiles it, like
// :core:theme compiles registry/theme.
val foundationMainSources = layout.buildDirectory.dir("generated/foundationMain")

val syncFoundationMain by tasks.registering(Sync::class) {
    from("${rootDir}/registry/foundation")
    include("**/*.kt")
    exclude("**/*Test.kt")
    into(foundationMainSources)
}

tasks.named("preBuild") {
    dependsOn(syncFoundationMain)
}

afterEvaluate {
    tasks.matching { it.name.startsWith("compile") && it.name.contains("Kotlin") }.configureEach {
        dependsOn(syncFoundationMain)
    }
}

dependencies {
    api(project(":core:theme"))

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

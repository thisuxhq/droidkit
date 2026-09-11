import droidkit.ContactSheets
import droidkit.Registry
import droidkit.RegistryLint
import droidkit.RegistryScreenshots

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.screenshot)
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
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
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
        // Created by the screenshot plugin after evaluation; match lazily.
        matching { it.name == "screenshotTest" }.configureEach {
            kotlin.directories.add(
                layout.buildDirectory
                    .dir("generated/registryScreenshotTest")
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

// ---------------------------------------------------------------------------
// Gates. registry.json is the source of truth; rules live in buildSrc/droidkit/Registry.kt.
// docs/verification.md explains the loop these tasks are part of.
// ---------------------------------------------------------------------------

val registryRoot: File = layout.projectDirectory.asFile
val registryMainSources = layout.buildDirectory.dir("generated/registryMain")
val registryTestSources = layout.buildDirectory.dir("generated/registryTest")
val registryScreenshotSources = layout.buildDirectory.dir("generated/registryScreenshotTest")
val registryInputs =
    fileTree(registryRoot) {
        include("theme/**", "components/**", "patterns/**", "blocks/**")
        exclude("build/**")
    }
val registryKotlinNoTests =
    fileTree(registryRoot) {
        include("theme/**/*.kt", "components/**/*.kt", "patterns/**/*.kt", "blocks/**/*.kt")
        exclude("**/*Test.kt", "build/**")
    }

// theme/ is compiled by :core:theme (it is the theme contract); do not sync it here.
val syncRegistryMain by tasks.registering(Sync::class) {
    from("components")
    from("patterns")
    from("blocks")
    include("**/*.kt")
    exclude("**/*Test.kt")
    into(registryMainSources)
}

val syncRegistryTest by tasks.registering(Sync::class) {
    from("components")
    from("patterns")
    from("blocks")
    include("**/*Test.kt")
    into(registryTestSources)
}

val lintRegistryImports by tasks.registering {
    group = "verification"
    description = "Registry Kotlin may import only com.droidkit.registry.*, Compose, Material 3, AndroidX, and the JDK."
    val root = registryRoot
    val files = registryKotlinNoTests
    inputs.files(files)
    doLast {
        val violations = RegistryLint.imports(root, files.files)
        check(violations.isEmpty()) { "registry import lint failed:\n${violations.joinToString("\n")}" }
    }
}

val lintRegistryStates by tasks.registering {
    group = "verification"
    description = "Each registry.json state must have a @Preview named '<item> <state>'; each @Preview must be a declared state."
    val root = registryRoot
    inputs.files(registryInputs)
    doLast {
        val violations = RegistryLint.states(root, Registry.readItems(root))
        check(violations.isEmpty()) { "registry states lint failed:\n${violations.joinToString("\n")}" }
    }
}

val lintRegistryConventions by tasks.registering {
    group = "verification"
    description = "App prefix on theme/components, modifier first optional param, dp only as named private val, no slop copy."
    val root = registryRoot
    inputs.files(registryInputs)
    doLast {
        val violations = RegistryLint.conventions(root, Registry.readItems(root))
        check(violations.isEmpty()) { "registry conventions lint failed:\n${violations.joinToString("\n")}" }
    }
}

// Screenshot tests are generated from states: one @PreviewTest per declared state, carrying the
// original @Preview arguments (uiMode, fontScale, widthDp). RTL is not a @Preview argument;
// previews wrap content in CompositionLocalProvider(LocalLayoutDirection provides Rtl).
//   ./gradlew :registry:updateDebugScreenshotTest    record goldens
//   ./gradlew :registry:validateDebugScreenshotTest  verify (part of check)
// Goldens live in registry/src/screenshotTestDebug/reference/.
val generateRegistryScreenshotTests by tasks.registering {
    group = "verification"
    description = "Generate @PreviewTest wrappers for every registry.json state."
    dependsOn(lintRegistryStates)
    val root = registryRoot
    val outDir = registryScreenshotSources
    inputs.files(registryInputs)
    outputs.dir(outDir)
    doLast {
        val out = outDir.get().asFile
        out.deleteRecursively()
        out.mkdirs()
        RegistryScreenshots.generate(root, Registry.readItems(root)).forEach { (name, content) ->
            out.resolve(name).writeText(content)
        }
    }
}

// One PNG per item with every state so a human can judge taste in ten seconds.
val contactSheets by tasks.registering {
    group = "verification"
    description = "Stitch golden screenshots into one image per item: build/contact-sheets/<item>.png"
    val root = registryRoot
    val referenceDir = layout.projectDirectory.dir("src/screenshotTestDebug/reference").asFile
    val outDir = layout.buildDirectory.dir("contact-sheets")
    inputs.dir(referenceDir).optional()
    outputs.dir(outDir)
    doLast {
        val written = ContactSheets.write(Registry.readItems(root), referenceDir, outDir.get().asFile)
        if (written.isEmpty()) logger.lifecycle("no goldens yet; run :registry:updateDebugScreenshotTest first")
        else logger.lifecycle("contact sheets: ${written.joinToString { it.name }} in ${outDir.get().asFile}")
    }
}

tasks.named("preBuild") {
    dependsOn(syncRegistryMain, syncRegistryTest, generateRegistryScreenshotTests)
}

afterEvaluate {
    tasks.matching { it.name.startsWith("compile") && it.name.contains("Test") }.configureEach {
        dependsOn(syncRegistryTest)
    }
    tasks.matching { it.name.startsWith("compile") && it.name.contains("Kotlin") }.configureEach {
        dependsOn(syncRegistryMain)
    }
    tasks.matching { it.name.startsWith("compile") && it.name.contains("ScreenshotTest") }.configureEach {
        dependsOn(generateRegistryScreenshotTests)
    }
    tasks.matching { it.name == "validateDebugScreenshotTest" || it.name == "updateDebugScreenshotTest" }.configureEach {
        finalizedBy(contactSheets)
    }
    contactSheets.configure {
        mustRunAfter("validateDebugScreenshotTest", "updateDebugScreenshotTest")
    }
}

tasks.named("check") {
    dependsOn(lintRegistryImports, lintRegistryStates, lintRegistryConventions)
    dependsOn("validateDebugScreenshotTest")
}

dependencies {
    api(project(":core:theme"))
    api(project(":core:foundation"))

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    testImplementation(composeBom)
    screenshotTestImplementation(composeBom)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    screenshotTestImplementation(libs.screenshot.validation.api)
    screenshotTestImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.androidx.compose.ui)
    testImplementation(libs.robolectric)
}

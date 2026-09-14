package droidkit

import groovy.json.JsonSlurper
import java.io.File

/**
 * The registry model and the mechanical gates that run over it.
 *
 * registry.json is the source of truth for what an item is (`type`), which UX states it
 * promises (`states`), and what it copies (`files`). Everything here derives from it.
 * Plain Kotlin, no Gradle types, so tasks stay configuration-cache friendly and the
 * rules can be unit tested. See docs/verification.md.
 */
data class RegistryItem(
    val name: String,
    val type: String,
    val status: String,
    val dir: File,
    val states: List<String>,
    val sourceFiles: List<File>,
    val previewFiles: List<File>,
    val ux: List<UxMoment> = emptyList(),
)

/** One row of the experience spec (`registry.json` → `ux`). See docs/verification.md. */
data class UxMoment(
    val moment: String,
    val behaviour: String,
    val why: String,
    val signature: Boolean,
    val reducedMotion: String?,
)

data class PreviewFunction(
    /** Text inside `@Preview( ... )`, verbatim. */
    val annotation: String,
    /** Value of `name = "..."`, or empty. */
    val name: String,
    val functionName: String,
    /** "", "internal", "private", or "public". */
    val visibility: String,
)

object Registry {
    private val itemGroups = listOf("components", "patterns", "blocks")

    /** Items compiled by a `:core:*` module rather than `:registry` (they are the contract items). */
    val coreItems = listOf("theme", "foundation")

    fun readItems(root: File): List<RegistryItem> {
        val slurper = JsonSlurper()
        val itemDirs =
            coreItems.map { root.resolve(it) } +
                itemGroups
                    .map { root.resolve(it) }
                    .filter { it.isDirectory }
                    .flatMap { it.listFiles { f -> f.isDirectory }?.toList() ?: emptyList() }
        return itemDirs
            .filter { it.resolve("registry.json").isFile }
            .map { dir ->
                @Suppress("UNCHECKED_CAST")
                val json = slurper.parse(dir.resolve("registry.json")) as Map<String, Any?>

                @Suppress("UNCHECKED_CAST")
                val files = (json["files"] as? List<Map<String, String>>) ?: emptyList()

                @Suppress("UNCHECKED_CAST")
                val states = (json["states"] as? List<String>) ?: emptyList()

                @Suppress("UNCHECKED_CAST")
                val ux = (json["ux"] as? List<Map<String, Any?>>) ?: emptyList()
                RegistryItem(
                    name = json["name"] as String,
                    type = json["type"] as String,
                    status = (json["status"] as? String) ?: "draft",
                    dir = dir,
                    states = states,
                    sourceFiles = files.filter { it["kind"] == "source" }.map { dir.resolve(it.getValue("path")) },
                    previewFiles = files.filter { it["kind"] == "preview" }.map { dir.resolve(it.getValue("path")) },
                    ux =
                        ux.map { row ->
                            UxMoment(
                                moment = (row["moment"] as? String).orEmpty(),
                                behaviour = (row["behaviour"] as? String).orEmpty(),
                                why = (row["why"] as? String).orEmpty(),
                                signature = (row["signature"] as? Boolean) ?: false,
                                reducedMotion = row["reducedMotion"] as? String,
                            )
                        },
                )
            }
            .sortedBy { it.name }
    }

    private val previewRegex =
        Regex(
            """@Preview\s*\(((?:[^()]|\([^()]*\))*)\)\s*@Composable\s*(?:(private|internal|public)\s+)?fun\s+([A-Za-z0-9_]+)\s*\(""",
            RegexOption.DOT_MATCHES_ALL,
        )
    private val previewNameRegex = Regex("""\bname\s*=\s*"([^"]*)"""")

    fun parsePreviews(file: File): List<PreviewFunction> =
        previewRegex.findAll(file.readText()).map { match ->
            val annotation = match.groupValues[1].trim()
            PreviewFunction(
                annotation = annotation,
                name = previewNameRegex.find(annotation)?.groupValues?.get(1) ?: "",
                visibility = match.groupValues[2],
                functionName = match.groupValues[3],
            )
        }.toList()

    fun String.toIdentifier(): String = replace(Regex("[^A-Za-z0-9]"), "_")

    fun String.toPascal(): String =
        split(Regex("[^A-Za-z0-9]+"))
            .filter { it.isNotEmpty() }
            .joinToString("") { it.replaceFirstChar(Char::uppercaseChar) }

    fun previewName(item: RegistryItem, state: String): String = "${item.name} $state"

    fun screenshotFunctionName(item: RegistryItem, state: String): String =
        "${item.name.toIdentifier()}__${state.toIdentifier()}"
}

object RegistryLint {
    private val allowedImports =
        listOf("com.droidkit.registry.", "androidx.", "android.", "kotlin.", "kotlinx.", "java.", "javax.")

    /** Gate 1: copied Kotlin may only import what a consumer app already has. */
    fun imports(root: File, files: Collection<File>): List<String> {
        val violations = mutableListOf<String>()
        files.forEach { file ->
            file.readLines().forEachIndexed { index, line ->
                val trimmed = line.trim()
                if (trimmed.startsWith("import ")) {
                    val imported = trimmed.removePrefix("import ").trim()
                    if (allowedImports.none { imported.startsWith(it) }) {
                        violations += "${file.relativeTo(root)}:${index + 1}: $imported"
                    }
                }
            }
        }
        return violations
    }

    /**
     * Gate 2: every declared state has exactly one `@Preview(name = "<item> <state>")`
     * and every `@Preview` is a declared state. Turns the state list into a CI gate.
     */
    fun states(root: File, items: List<RegistryItem>): List<String> {
        val violations = mutableListOf<String>()
        items.forEach { item ->
            val rel = item.dir.relativeTo(root)
            if (item.states.isEmpty()) violations += "$rel/registry.json: states is empty"
            if (item.previewFiles.isEmpty()) violations += "$rel/registry.json: no file with kind \"preview\""
            val previews = item.previewFiles.filter { it.isFile }.flatMap { Registry.parsePreviews(it) }
            val expected = item.states.map { Registry.previewName(item, it) }
            item.states.forEach { state ->
                val name = Registry.previewName(item, state)
                val count = previews.count { it.name == name }
                if (count == 0) violations += "$rel: state \"$state\" has no @Preview(name = \"$name\")"
                if (count > 1) violations += "$rel: @Preview(name = \"$name\") declared $count times"
            }
            previews.forEach { p ->
                when {
                    p.name.isBlank() ->
                        violations += "$rel: @Preview on ${p.functionName} has no name; use name = \"${item.name} <state>\""
                    p.name !in expected ->
                        violations += "$rel: @Preview(name = \"${p.name}\") on ${p.functionName} is not a declared state"
                }
                if (p.visibility == "private") {
                    violations += "$rel: ${p.functionName} is private; previews must be internal so the screenshot test can call them"
                }
            }
        }
        return violations
    }

    private val composableFun =
        Regex("""@Composable\s*\n\s*(?:(private|internal|public)\s+)?fun\s+([A-Za-z0-9_]+)\s*\(""")
    private val dpLiteral = Regex("""\b\d+(?:\.\d+)?\.dp\b""")
    private val stringWithBang = Regex(""""[^"\n]*![^"\n]*"""")
    private val oops = Regex(""""[^"\n]*\b(Oops|Whoops|Uh[- ]oh)\b[^"\n]*"""", RegexOption.IGNORE_CASE)

    /**
     * Gate 3: conventions that make copied code read like an app file. Small on purpose;
     * taste is a human review. These are the mechanical part.
     */
    fun conventions(root: File, items: List<RegistryItem>): List<String> {
        val violations = mutableListOf<String>()
        items.forEach { item ->
            (item.sourceFiles + item.previewFiles).filter { it.isFile }.forEach { file ->
                val rel = file.relativeTo(root)
                val text = file.readText()
                val isPreview = file in item.previewFiles

                if (!isPreview) composableFun.findAll(text).forEach { m ->
                    val visibility = m.groupValues[1]
                    val fn = m.groupValues[2]
                    val line = text.substring(0, m.range.first).count { it == '\n' } + 2
                    val isPublic = visibility.isEmpty() || visibility == "public"
                    if (isPublic && item.type in setOf("theme", "component") && !fn.startsWith("App")) {
                        violations += "$rel:$line: public composable $fn must use the App prefix (decisions #2)"
                    }
                    val params = splitParams(parameterList(text, m.range.last + 1))
                    val modifierIndex = params.indexOfFirst { it.substringBefore(':').trim() == "modifier" }
                    if (modifierIndex >= 0) {
                        val firstOptional = params.indexOfFirst { it.contains("=") }
                        if (firstOptional != modifierIndex) {
                            violations += "$rel:$line: $fn: modifier must be the first optional parameter"
                        }
                        if (!params[modifierIndex].replace(" ", "").endsWith("=Modifier")) {
                            violations += "$rel:$line: $fn: modifier must default to Modifier"
                        }
                    }
                }

                // Theme files define the dp tokens; they are exempt from the literal rule.
                // Previews are checked too: copied demo code must read like app code.
                val checkDp = item.type != "theme"
                text.lines().forEachIndexed { idx, raw ->
                    val line = raw.trim()
                    if (line.startsWith("//") || line.startsWith("*") || line.startsWith("import ")) return@forEachIndexed
                    if (checkDp && dpLiteral.containsMatchIn(line) && !line.startsWith("private val ")) {
                        violations += "$rel:${idx + 1}: dp literal inline; use AppTheme.spacing or a named private val (decisions #4)"
                    }
                    if (stringWithBang.containsMatchIn(line)) {
                        violations += "$rel:${idx + 1}: exclamation mark in UI copy"
                    }
                    if (oops.containsMatchIn(line)) {
                        violations += "$rel:${idx + 1}: 'Oops' copy; name the problem and the next step (docs/catalog.md)"
                    }
                }
            }
        }
        return violations
    }

    private val motionWords =
        Regex("""\b(shak\w*|scal\w*|crossfad\w*|animat\w*|puls\w*|slid\w*|morph\w*|pop\w*|shimmer\w*|bounc\w*|spring\w*)\b""", RegexOption.IGNORE_CASE)

    /**
     * Gate 4: the experience spec exists and is well formed. Whether the code honours it is
     * the reviewer's job (docs/verification.md → "The experience spec"); this only makes sure
     * there is something to review. Theme and foundation are contracts, not moments.
     */
    fun ux(root: File, items: List<RegistryItem>): List<String> {
        val violations = mutableListOf<String>()
        items.filter { it.type !in Registry.coreItems }.forEach { item ->
            val rel = "${item.dir.relativeTo(root)}/registry.json"
            if (item.ux.isEmpty()) {
                violations += "$rel: no ux entries; write the moments before the states (decisions #19)"
                return@forEach
            }
            val signatures = item.ux.count { it.signature }
            if (signatures == 0) violations += "$rel: no ux entry has signature: true; name the detail a user would remember"
            if (signatures > 1) violations += "$rel: $signatures ux entries claim signature; pick one"
            item.ux.forEachIndexed { index, row ->
                if (motionWords.containsMatchIn(row.behaviour) && row.reducedMotion.isNullOrBlank()) {
                    violations += "$rel: ux[$index] (${row.moment}) animates but has no reducedMotion"
                }
                if (row.why.isBlank() || row.behaviour.isBlank()) {
                    violations += "$rel: ux[$index] (${row.moment}) needs both behaviour and why"
                }
            }
        }
        return violations
    }

    /** Text between the opening paren at [start] and its matching close paren. */
    private fun parameterList(text: String, start: Int): String {
        var depth = 1
        var i = start
        while (i < text.length && depth > 0) {
            when (text[i]) {
                '(' -> depth++
                ')' -> depth--
            }
            i++
        }
        return text.substring(start, (i - 1).coerceAtLeast(start))
    }

    private fun splitParams(params: String): List<String> {
        val out = mutableListOf<String>()
        var depth = 0
        val current = StringBuilder()
        params.forEach { c ->
            when (c) {
                '(', '<', '[', '{' -> {
                    depth++
                    current.append(c)
                }
                ')', '>', ']', '}' -> {
                    depth--
                    current.append(c)
                }
                ',' ->
                    if (depth == 0) {
                        out += current.toString().trim()
                        current.clear()
                    } else {
                        current.append(c)
                    }
                else -> current.append(c)
            }
        }
        if (current.isNotBlank()) out += current.toString().trim()
        return out.filter { it.isNotEmpty() }
    }
}

object RegistryScreenshots {
    /**
     * One `@PreviewTest` wrapper per declared state, carrying the original `@Preview`
     * arguments (uiMode, fontScale, widthDp). RTL is not a `@Preview` argument; previews wrap
     * their content in `CompositionLocalProvider(LocalLayoutDirection provides Rtl)`. The copied
     * preview file stays free of test-tool imports. Returns file name → contents.
     */
    fun generate(root: File, items: List<RegistryItem>): Map<String, String> =
        items.associate { item ->
            val previewFile = item.previewFiles.first { it.isFile }
            val text = previewFile.readText()
            val pkg =
                Regex("""^package\s+([\w.]+)""", RegexOption.MULTILINE).find(text)?.groupValues?.get(1)
                    ?: error("${previewFile.name}: no package")
            val imports = text.lines().filter { it.startsWith("import ") }
            val previews = Registry.parsePreviews(previewFile).associateBy { it.name }
            // Missing previews are reported by RegistryLint.states; skip here so a partial
            // registry can still render what it has.
            val functions =
                item.states.mapNotNull { state ->
                    val p = previews[Registry.previewName(item, state)] ?: return@mapNotNull null
                    """
                    |@PreviewTest
                    |@Preview(${p.annotation})
                    |@Composable
                    |fun ${Registry.screenshotFunctionName(item, state)}() {
                    |    ${p.functionName}()
                    |}
                    """.trimMargin()
                }
            val previewImports = previews.values.map { "import $pkg.${it.functionName}" }
            val fileName = with(Registry) { "${item.name.toPascal()}Screenshots.kt" }
            fileName to
                buildString {
                    appendLine("// Generated by :registry:generateRegistryScreenshotTests from ${item.dir.relativeTo(root)}/registry.json. Do not edit.")
                    appendLine("package com.droidkit.registry.screenshots")
                    appendLine()
                    (imports + previewImports + "import com.android.tools.screenshot.PreviewTest").toSortedSet().forEach { appendLine(it) }
                    appendLine()
                    functions.forEach {
                        appendLine(it)
                        appendLine()
                    }
                }
        }
}

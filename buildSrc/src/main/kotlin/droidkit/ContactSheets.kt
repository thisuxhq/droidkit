package droidkit

import droidkit.Registry.toIdentifier
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Stitches the golden screenshots of one item into a single labelled grid so a human can
 * judge every state (light, dark, large font, RTL) in one look. Taste is the one gate we
 * cannot automate; this makes it cheap.
 */
object ContactSheets {
    private const val PAD = 24
    private const val LABEL = 28
    private const val MAX_COLS = 4

    fun write(items: List<RegistryItem>, referenceDir: File, outDir: File): List<File> {
        if (!referenceDir.isDirectory) return emptyList()
        outDir.mkdirs()
        val pngs = referenceDir.walkTopDown().filter { it.extension == "png" }.toList()
        return items.filter { it.type != "theme" }.mapNotNull { item ->
            val cells =
                item.states.mapNotNull { state ->
                    val marker = "${Registry.screenshotFunctionName(item, state)}_"
                    pngs.firstOrNull { it.name.contains(marker) }?.let { state to ImageIO.read(it) }
                }
            if (cells.isEmpty()) return@mapNotNull null
            val cols = minOf(cells.size, MAX_COLS)
            val rows = (cells.size + cols - 1) / cols
            val cellW = cells.maxOf { it.second.width }
            val cellH = cells.maxOf { it.second.height }
            val sheet =
                BufferedImage(
                    cols * (cellW + PAD) + PAD,
                    rows * (cellH + PAD + LABEL) + PAD,
                    BufferedImage.TYPE_INT_ARGB,
                )
            val g = sheet.createGraphics()
            g.color = Color(0xF2, 0xF2, 0xF2)
            g.fillRect(0, 0, sheet.width, sheet.height)
            g.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
            cells.forEachIndexed { index, (state, image) ->
                val x = PAD + (index % cols) * (cellW + PAD)
                val y = PAD + (index / cols) * (cellH + PAD + LABEL)
                g.color = Color.DARK_GRAY
                g.drawString("${item.name} · $state", x, y + 18)
                g.drawImage(image, x, y + LABEL, null)
            }
            g.dispose()
            val out = outDir.resolve("${item.name}.png")
            ImageIO.write(sheet, "png", out)
            out
        }
    }
}

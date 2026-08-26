package com.example.data.export

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.example.data.engine.SoulResonanceData
import com.example.data.local.EvaluationRecordEntity
import com.example.data.local.EvolutionEventEntity
import com.example.data.model.IdentityMilestoneCatalog
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SoulPdfExporter {

    private const val PAGE_WIDTH = 595 // Standard A4 width in pt
    private const val PAGE_HEIGHT = 842 // Standard A4 height in pt

    fun generateSoulHistoryPdf(
        soul: SoulIdentity,
        records: List<EvaluationRecordEntity>,
        events: List<EvolutionEventEntity>,
        resonance: SoulResonanceData?
    ): ByteArray {
        val outputStream = ByteArrayOutputStream()
        writeSoulHistoryPdfToStream(soul, records, events, resonance, outputStream)
        return outputStream.toByteArray()
    }

    fun writeSoulHistoryPdfToStream(
        soul: SoulIdentity,
        records: List<EvaluationRecordEntity>,
        events: List<EvolutionEventEntity>,
        resonance: SoulResonanceData?,
        outputStream: OutputStream
    ) {
        val document = PdfDocument()

        // Page 1: Vessel Dossier & Archetype Polarity Geometry
        val pageInfo1 = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page1 = document.startPage(pageInfo1)
        drawPage1(page1.canvas, soul, events, resonance)
        document.finishPage(page1)

        // Page 2: Transmutation History Chronicle
        val pageInfo2 = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 2).create()
        val page2 = document.startPage(pageInfo2)
        drawPage2(page2.canvas, records)
        document.finishPage(page2)

        document.writeTo(outputStream)
        document.close()
    }

    private fun drawPage1(
        canvas: Canvas,
        soul: SoulIdentity,
        events: List<EvolutionEventEntity>,
        resonance: SoulResonanceData?
    ) {
        // Background
        val bgPaint = Paint().apply { color = Color.rgb(18, 14, 34) }
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), bgPaint)

        // Decorative Header Banner
        val headerPaint = Paint().apply { color = Color.rgb(28, 20, 52) }
        canvas.drawRect(20f, 20f, (PAGE_WIDTH - 20).toFloat(), 95f, headerPaint)

        val borderPaint = Paint().apply {
            color = Color.rgb(212, 175, 55) // Radiant Gold
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        }
        canvas.drawRect(20f, 20f, (PAGE_WIDTH - 20).toFloat(), 95f, borderPaint)

        // App & System Title
        val titlePaint = Paint().apply {
            color = Color.rgb(255, 215, 0)
            textSize = 18f
            isFakeBoldText = true
            typeface = Typeface.SERIF
        }
        canvas.drawText("GODLY SYSTEM — ASTRAL VESSEL DOSSIER", 36f, 50f, titlePaint)

        val subPaint = Paint().apply {
            color = Color.rgb(192, 132, 252)
            textSize = 10f
            typeface = Typeface.SANS_SERIF
        }
        val dateStr = SimpleDateFormat("MMMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date())
        canvas.drawText("Generated: $dateStr  |  Identity Evolution Chronicle", 36f, 72f, subPaint)

        // Section 1: Vessel Profile Card
        val cardPaint = Paint().apply { color = Color.rgb(24, 18, 44) }
        val cardBorder = Paint().apply {
            color = Color.rgb(56, 42, 90)
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        canvas.drawRoundRect(RectF(20f, 105f, (PAGE_WIDTH - 20).toFloat(), 245f), 8f, 8f, cardPaint)
        canvas.drawRoundRect(RectF(20f, 105f, (PAGE_WIDTH - 20).toFloat(), 245f), 8f, 8f, cardBorder)

        val sectionHeaderPaint = Paint().apply {
            color = Color.rgb(212, 175, 55)
            textSize = 11f
            isFakeBoldText = true
        }
        canvas.drawText("1. VESSEL CLASSIFICATION & PERSONA MATRIX", 36f, 126f, sectionHeaderPaint)

        val textBold = Paint().apply {
            color = Color.WHITE
            textSize = 12f
            isFakeBoldText = true
        }
        val textMuted = Paint().apply {
            color = Color.rgb(180, 180, 200)
            textSize = 10f
        }
        val textCyan = Paint().apply {
            color = Color.rgb(56, 189, 248)
            textSize = 11f
            isFakeBoldText = true
        }

        // Row 1
        canvas.drawText("Title: « ${soul.currentTitle} »", 36f, 150f, textBold)
        canvas.drawText("Race / Vessel: ${soul.race}", 36f, 170f, textCyan)
        canvas.drawText("Class: ${soul.advancedClass ?: soul.className}", 36f, 190f, textMuted)
        canvas.drawText("Element: ${soul.element}", 36f, 210f, textMuted)
        canvas.drawText("Alignment: ${soul.alignment}", 36f, 230f, textMuted)

        // Metrics Column Right
        val resPercent = resonance?.percentage ?: ((soul.humanity * 0.5f) + (soul.stability * 0.5f)).toInt()
        canvas.drawText("Soul Resonance: $resPercent%", 320f, 150f, textCyan)
        canvas.drawText("Frequency: ${resonance?.frequencyLabel ?: "528 Hz • Miraculous Harmony"}", 320f, 170f, textMuted)
        canvas.drawText("Humanity Tether: ${soul.humanity}%", 320f, 190f, textMuted)
        canvas.drawText("Soul Shards: 💎 ${soul.soulShards}", 320f, 210f, textBold)
        canvas.drawText("Metamorphic Progress: ${soul.evolutionProgress}% (Next: ${soul.possibleEvolution})", 320f, 230f, textMuted)

        // Section 2: Seven Sins vs Seven Virtues Breakdown Table
        canvas.drawRoundRect(RectF(20f, 255f, (PAGE_WIDTH - 20).toFloat(), 535f), 8f, 8f, cardPaint)
        canvas.drawRoundRect(RectF(20f, 255f, (PAGE_WIDTH - 20).toFloat(), 535f), 8f, 8f, cardBorder)
        canvas.drawText("2. SEVEN DEADLY SINS VS. SEVEN HEAVENLY VIRTUES BALANCE", 36f, 276f, sectionHeaderPaint)

        // Table Headers
        val headerRowPaint = Paint().apply { color = Color.rgb(35, 26, 65) }
        canvas.drawRect(30f, 288f, (PAGE_WIDTH - 30).toFloat(), 308f, headerRowPaint)

        val thPaint = Paint().apply {
            color = Color.rgb(255, 215, 0)
            textSize = 9f
            isFakeBoldText = true
        }
        canvas.drawText("DEADLY SIN (SHADOW)", 38f, 302f, thPaint)
        canvas.drawText("SCORE", 180f, 302f, thPaint)
        canvas.drawText("HEAVENLY VIRTUE", 290f, 302f, thPaint)
        canvas.drawText("SCORE", 430f, 302f, thPaint)
        canvas.drawText("POLARITY", 490f, 302f, thPaint)

        val sinPairs = listOf(
            Pair(ShadowType.PRIDE, VirtueType.HUMILITY),
            Pair(ShadowType.GREED, VirtueType.CHARITY),
            Pair(ShadowType.DESIRE, VirtueType.TEMPERANCE),
            Pair(ShadowType.ENVY, VirtueType.GRATITUDE),
            Pair(ShadowType.GLUTTONY, VirtueType.COURAGE),
            Pair(ShadowType.WRATH, VirtueType.PATIENCE),
            Pair(ShadowType.SLOTH, VirtueType.DILIGENCE)
        )

        var tableY = 328f
        val redText = Paint().apply { color = Color.rgb(248, 113, 113); textSize = 9.5f }
        val goldText = Paint().apply { color = Color.rgb(250, 204, 21); textSize = 9.5f }
        val whiteText = Paint().apply { color = Color.WHITE; textSize = 9.5f }

        sinPairs.forEach { (sin, virtue) ->
            val sinScore = soul.shadowScores[sin] ?: 30
            val virtueScore = soul.virtueScores[virtue] ?: 30
            val dominantText = if (virtueScore >= sinScore) "Virtue (+${virtueScore - sinScore})" else "Shadow (+${sinScore - virtueScore})"

            canvas.drawText("${sin.displayName} (${sin.name})", 38f, tableY, redText)
            canvas.drawText("$sinScore / 100", 180f, tableY, whiteText)
            canvas.drawText("${virtue.displayName} (${virtue.name})", 290f, tableY, goldText)
            canvas.drawText("$virtueScore / 100", 430f, tableY, whiteText)
            canvas.drawText(dominantText, 490f, tableY, if (virtueScore >= sinScore) goldText else redText)

            tableY += 28f
        }

        // Section 3: Identity Milestones & Badges
        val badges = IdentityMilestoneCatalog.evaluateMilestones(soul, emptyList(), events)
        canvas.drawRoundRect(RectF(20f, 545f, (PAGE_WIDTH - 20).toFloat(), 800f), 8f, 8f, cardPaint)
        canvas.drawRoundRect(RectF(20f, 545f, (PAGE_WIDTH - 20).toFloat(), 800f), 8f, 8f, cardBorder)
        canvas.drawText("3. IDENTITY MILESTONES & ASTRAL BADGES", 36f, 566f, sectionHeaderPaint)

        var badgeY = 590f
        badges.take(6).forEach { badge ->
            val statusColor = if (badge.isUnlocked) Color.rgb(52, 211, 153) else Color.rgb(150, 150, 150)
            val badgeStatusPaint = Paint().apply { color = statusColor; textSize = 9f; isFakeBoldText = true }

            val statusText = if (badge.isUnlocked) "[UNLOCKED] • ${badge.tier.displayName}" else "[LOCKED] (${badge.progressCurrent}/${badge.progressMax})"
            canvas.drawText("${badge.name} — $statusText", 38f, badgeY, badgeStatusPaint)
            canvas.drawText(badge.description, 38f, badgeY + 14f, textMuted)
            badgeY += 34f
        }
    }

    private fun drawPage2(
        canvas: Canvas,
        records: List<EvaluationRecordEntity>
    ) {
        // Background
        val bgPaint = Paint().apply { color = Color.rgb(18, 14, 34) }
        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), bgPaint)

        // Header
        val headerPaint = Paint().apply { color = Color.rgb(28, 20, 52) }
        canvas.drawRect(20f, 20f, (PAGE_WIDTH - 20).toFloat(), 80f, headerPaint)

        val borderPaint = Paint().apply {
            color = Color.rgb(212, 175, 55)
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        }
        canvas.drawRect(20f, 20f, (PAGE_WIDTH - 20).toFloat(), 80f, borderPaint)

        val titlePaint = Paint().apply {
            color = Color.rgb(255, 215, 0)
            textSize = 15f
            isFakeBoldText = true
            typeface = Typeface.SERIF
        }
        canvas.drawText("SOUL HISTORY TRANSMUTATION LOG", 36f, 48f, titlePaint)

        val subPaint = Paint().apply {
            color = Color.rgb(192, 132, 252)
            textSize = 9.5f
        }
        canvas.drawText("Chronological record of catalyst self-evaluations and systemic reflections (${records.size} total entries)", 36f, 66f, subPaint)

        // Table Container
        val cardPaint = Paint().apply { color = Color.rgb(24, 18, 44) }
        val cardBorder = Paint().apply {
            color = Color.rgb(56, 42, 90)
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        canvas.drawRoundRect(RectF(20f, 90f, (PAGE_WIDTH - 20).toFloat(), 815f), 8f, 8f, cardPaint)
        canvas.drawRoundRect(RectF(20f, 90f, (PAGE_WIDTH - 20).toFloat(), 815f), 8f, 8f, cardBorder)

        if (records.isEmpty()) {
            val emptyPaint = Paint().apply { color = Color.rgb(160, 160, 180); textSize = 11f; isFakeBoldText = true }
            canvas.drawText("No evaluation logs recorded yet. Complete transmutations in the Record Sanctuary.", 36f, 130f, emptyPaint)
            return
        }

        var entryY = 118f
        val dateFmt = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
        val datePaint = Paint().apply { color = Color.rgb(56, 189, 248); textSize = 9f; isFakeBoldText = true }
        val traitPaint = Paint().apply { color = Color.rgb(250, 204, 21); textSize = 9.5f; isFakeBoldText = true }
        val insightPaint = Paint().apply { color = Color.rgb(220, 220, 230); textSize = 8.5f }
        val dividerPaint = Paint().apply { color = Color.rgb(45, 35, 75); strokeWidth = 0.8f }

        records.take(12).forEachIndexed { index, rec ->
            val dateStr = dateFmt.format(Date(rec.timestamp))
            val forceStr = buildString {
                append(rec.emotion.ifBlank { "Transmutation" })
                rec.primaryShadow?.let { append(" | Sin: $it") }
                rec.primaryVirtue?.let { append(" | Virtue: $it") }
            }

            canvas.drawText("#${index + 1} • $dateStr", 36f, entryY, datePaint)
            canvas.drawText(forceStr, 220f, entryY, traitPaint)

            val insight = rec.analysisInsight.ifBlank { rec.reflection.ifBlank { "Soul forces recorded into the celestial matrix." } }
            val truncatedInsight = if (insight.length > 90) insight.substring(0, 87) + "..." else insight
            canvas.drawText("Insight: $truncatedInsight", 36f, entryY + 16f, insightPaint)

            canvas.drawLine(30f, entryY + 28f, (PAGE_WIDTH - 30).toFloat(), entryY + 28f, dividerPaint)
            entryY += 56f
        }
    }
}

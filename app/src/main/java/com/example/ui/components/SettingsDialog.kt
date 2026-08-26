package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.RarePalette
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.ExportFormat
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsDialog(
    currentThemeMode: AppThemeMode,
    currentPalette: RarePalette,
    onSetThemeMode: (AppThemeMode) -> Unit,
    onSetPalette: (RarePalette) -> Unit,
    onExportData: (ExportFormat, (String) -> Unit) -> Unit,
    onExportPdf: (((ByteArray) -> Unit) -> Unit)? = null,
    onImportData: (String, (Boolean, String) -> Unit) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedExportFormat by remember { mutableStateOf(ExportFormat.PDF) }
    var fileOperationStatus by remember { mutableStateOf<String?>(null) }
    var isFileOperationSuccess by remember { mutableStateOf(true) }
    var pendingExportData by remember { mutableStateOf<String?>(null) }
    var pendingExportBytes by remember { mutableStateOf<ByteArray?>(null) }

    // File Save Launcher
    val saveFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(selectedExportFormat.mimeType)
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    if (selectedExportFormat == ExportFormat.PDF && pendingExportBytes != null) {
                        stream.write(pendingExportBytes!!)
                    } else if (pendingExportData != null) {
                        stream.write(pendingExportData!!.toByteArray(Charsets.UTF_8))
                    }
                }
                isFileOperationSuccess = true
                fileOperationStatus = "✓ ${selectedExportFormat.displayName} saved successfully to your device storage."
                Toast.makeText(context, "File saved successfully!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                isFileOperationSuccess = false
                fileOperationStatus = "Failed to save file: ${e.localizedMessage}"
            }
        }
    }

    // File Open Launcher (Supports JSON, Markdown, Text, CSV)
    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val content = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                if (!content.isNullOrBlank()) {
                    onImportData(content) { success, msg ->
                        isFileOperationSuccess = success
                        fileOperationStatus = if (success) "✓ Restored: $msg" else "Error: $msg"
                    }
                } else {
                    isFileOperationSuccess = false
                    fileOperationStatus = "Selected file is empty."
                }
            } catch (e: Exception) {
                isFileOperationSuccess = false
                fileOperationStatus = "Could not read backup file: ${e.localizedMessage}"
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, SurfaceCardBorder, RoundedCornerShape(24.dp))
                .testTag("settings_dialog"),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "SYSTEM SETTINGS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Themes & Multi-Format File Backup",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // SECTION 1: DISPLAY MODE
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "DISPLAY MODE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    letterSpacing = 1.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AppThemeMode.values().forEach { mode ->
                                        val isSelected = currentThemeMode == mode
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                                    else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                                                )
                                                .border(
                                                    width = if (isSelected) 1.5.dp else 0.8.dp,
                                                    color = if (isSelected) MaterialTheme.colorScheme.primary else SurfaceCardBorder,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable { onSetThemeMode(mode) }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(text = mode.icon, fontSize = 14.sp)
                                                Text(
                                                    text = mode.displayName,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 2: MINIMAL PALETTE THEMES (INCL. 2 BRIGHTER THEMES)
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "COLOR PALETTES",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "${RarePalette.values().size} Minimal Themes",
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }

                                // Minimal Theme Cards Grid
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    maxItemsInEachRow = 2
                                ) {
                                    RarePalette.values().forEach { palette ->
                                        val isSelected = currentPalette == palette
                                        val isBright = palette == RarePalette.RADIANT_SOLAR || palette == RarePalette.PRISMATIC_OPAL

                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .border(
                                                    width = if (isSelected) 1.8.dp else 0.8.dp,
                                                    color = if (isSelected) palette.primaryColor else SurfaceCardBorder,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable { onSetPalette(palette) },
                                            color = MaterialTheme.colorScheme.surface
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                // Minimalist Dual-Dot Color Indicator
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy((-4).dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(14.dp)
                                                            .clip(CircleShape)
                                                            .background(palette.primaryColor)
                                                            .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                                                    )
                                                    Box(
                                                        modifier = Modifier
                                                            .size(14.dp)
                                                            .clip(CircleShape)
                                                            .background(palette.secondaryColor)
                                                            .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                                                    )
                                                }

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = palette.title.substringBefore(" "),
                                                        fontSize = 11.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        maxLines = 1
                                                    )
                                                    if (isBright) {
                                                        Text(
                                                            text = "☀ Bright",
                                                            fontSize = 9.sp,
                                                            color = RadiantGold,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }

                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Selected",
                                                        tint = palette.primaryColor,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 3: MULTI-FORMAT FILE EXPORT & FILE IMPORT
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "MULTI-FORMAT FILE EXPORT & RESTORE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 1.sp
                                    )
                                    Text(text = "FILE BACKUP", fontSize = 10.sp, color = TextMuted)
                                }

                                Text(
                                    text = "Export your vessel profile, transmutation records, and unlocked catalog across multiple formats (JSON, Markdown, Plain Text, CSV).",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )

                                // Export Format Selector Chips
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "SELECT EXPORT FORMAT:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        ExportFormat.values().forEach { fmt ->
                                            val isFmtSelected = selectedExportFormat == fmt
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(10.dp))
                                                .background(
                                                    if (isFmtSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                                    else MaterialTheme.colorScheme.surface
                                                )
                                                .border(
                                                    1.dp,
                                                    if (isFmtSelected) MaterialTheme.colorScheme.primary else SurfaceCardBorder,
                                                    RoundedCornerShape(10.dp)
                                                )
                                                .clickable { selectedExportFormat = fmt }
                                                .padding(vertical = 8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = ".${fmt.extension.uppercase()}",
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isFmtSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isFmtSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }

                                // Export Buttons (File Save + File Share)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            if (selectedExportFormat == ExportFormat.PDF && onExportPdf != null) {
                                                onExportPdf { bytes ->
                                                    pendingExportBytes = bytes
                                                    saveFileLauncher.launch(selectedExportFormat.defaultFilename)
                                                }
                                            } else {
                                                onExportData(selectedExportFormat) { content ->
                                                    pendingExportData = content
                                                    saveFileLauncher.launch(selectedExportFormat.defaultFilename)
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .weight(1.2f)
                                            .testTag("export_file_button")
                                    ) {
                                        Icon(imageVector = Icons.Default.SaveAlt, contentDescription = "Export File", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "Save as .${selectedExportFormat.extension}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            if (selectedExportFormat == ExportFormat.PDF && onExportPdf != null) {
                                                onExportPdf { bytes ->
                                                    try {
                                                        val cacheFile = File(context.cacheDir, selectedExportFormat.defaultFilename)
                                                        FileOutputStream(cacheFile).use { fos ->
                                                            fos.write(bytes)
                                                        }
                                                        val uri = FileProvider.getUriForFile(
                                                            context,
                                                            "${context.packageName}.fileprovider",
                                                            cacheFile
                                                        )
                                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                            type = selectedExportFormat.mimeType
                                                            putExtra(Intent.EXTRA_STREAM, uri)
                                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                        }
                                                        context.startActivity(Intent.createChooser(shareIntent, "Share ${selectedExportFormat.displayName}"))
                                                    } catch (e: Exception) {
                                                        isFileOperationSuccess = false
                                                        fileOperationStatus = "Share failed: ${e.localizedMessage}"
                                                    }
                                                }
                                            } else {
                                                onExportData(selectedExportFormat) { content ->
                                                    try {
                                                        val cacheFile = File(context.cacheDir, selectedExportFormat.defaultFilename)
                                                        FileOutputStream(cacheFile).use { fos ->
                                                            fos.write(content.toByteArray(Charsets.UTF_8))
                                                        }
                                                        val uri = FileProvider.getUriForFile(
                                                            context,
                                                            "${context.packageName}.fileprovider",
                                                            cacheFile
                                                        )
                                                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                            type = selectedExportFormat.mimeType
                                                            putExtra(Intent.EXTRA_STREAM, uri)
                                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                        }
                                                        context.startActivity(Intent.createChooser(shareIntent, "Share ${selectedExportFormat.displayName}"))
                                                    } catch (e: Exception) {
                                                        isFileOperationSuccess = false
                                                        fileOperationStatus = "Share failed: ${e.localizedMessage}"
                                                    }
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(imageVector = Icons.Default.IosShare, contentDescription = "Share File", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Share", fontSize = 12.sp)
                                    }
                                }

                                // Import Button (Pick File from device storage)
                                Button(
                                    onClick = {
                                        openFileLauncher.launch(arrayOf("*/*", "application/json", "text/*", "text/plain", "text/csv"))
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EtherealCyan,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("import_file_button")
                                ) {
                                    Icon(imageVector = Icons.Default.FolderOpen, contentDescription = "Import File", modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Import & Restore from File (.json / .csv / .md / .txt)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                if (fileOperationStatus != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                if (isFileOperationSuccess) Color(0xFF064E3B).copy(alpha = 0.6f)
                                                else Color(0xFF7F1D1D).copy(alpha = 0.6f)
                                            )
                                            .border(
                                                0.8.dp,
                                                if (isFileOperationSuccess) Color(0xFF10B981) else Color(0xFFEF4444),
                                                RoundedCornerShape(10.dp)
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = fileOperationStatus!!,
                                            fontSize = 11.sp,
                                            color = if (isFileOperationSuccess) Color(0xFF6EE7B7) else Color(0xFFFCA5A5),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

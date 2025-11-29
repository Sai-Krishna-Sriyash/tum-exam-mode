package de.tum.exam

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.options.advanced.AdvancedSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.registry.Registry

class ExamModeStartupActivity : ProjectActivity {

    // --- THE COMPREHENSIVE AI BAN LIST ---
    private val forbiddenPlugins = listOf(
        // === 1. JetBrains Internal AI & ML ===
        "org.jetbrains.completion.full.line", // Full Line Code Completion (New ID)
        "com.intellij.completion.full.line", // Full Line Code Completion (Old ID)
        "com.intellij.ml.llm",             // JetBrains AI Assistant (The big one)
        "com.intellij.junie",              // Junie (New AI Agent)
        "org.jetbrains.junie",             // Junie (Alternative ID)
        "com.jetbrains.junie",             // Junie (Alternative ID)
        "com.intellij.completion.ml.ranking", // ML Sorting
        "com.intellij.searcheverywhere.ml",   // ML in Search
        "com.intellij.marketplace.ml",        // ML in Marketplace
        "com.intellij.turboComplete",         // Turbo Complete
        "com.intellij.inline.completion",     // Core Inline Completion Infrastructure
        "com.intellij.grazie.pro",            // Grazie Pro (AI Grammar checks)

        // === 2. The "Big Three" External AIs ===
        "com.github.copilot",              // GitHub Copilot
        "com.github.copilot.chat",         // Copilot Chat
        "aws.toolkit",                     // Amazon Q / CodeWhisperer
        "com.sourcegraph.jetbrains",       // Cody (Sourcegraph)

        // === 3. Popular Third-Party AI Assistants ===
        "com.tabnine.TabNine",             // Tabnine
        "com.codeium.intellij",            // Codeium
        "co.bito.bito-intellij",           // Bito AI
        "ai.blackbox.jetbrains",           // Blackbox AI
        "com.tabbyml.intellij-tabby",      // Tabby
        "com.fittentech.fittencode",       // Fitten Code
        "com.alibaba.tongyi.lingma",       // Tongyi Lingma (Alibaba)
        "cn.aminer.geex",                  // CodeGeeX
        "com.marscode.marscode",           // MarsCode / Trae AI
        "com.codigami.launchable",         // Launchable
        "com.mintlify.doc_writer",         // Mintlify (Doc Writer)
        "com.codium.ai",                   // CodiumAI (Test Generator)
        "com.rubberduck.rubberduck",       // Rubberduck
        "com.supermaven.intellij",         // Supermaven
        "io.continuum.continue"            // Continue.dev
    )

    // Registry keys to force-disable internal features
    private val forbiddenRegistryKeys = listOf(
        "llm.enable.ghost.text",
        "inline.completion.enabled",
        "full.line.completion.enabled",
        "full.line.ai.enabled",
        "completion.stats.show.ml.ranking",
        "junie.enabled",
        "ai.assistant.enabled"
    )

    override suspend fun execute(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            enforceExamMode(project)
        }
    }

    private fun enforceExamMode(project: Project) {
        var changesMade = false

        // 1. Disable Plugins
        val disabledPlugins = disableForbiddenPlugins()
        if (disabledPlugins > 0) changesMade = true

        // 2. Disable Registry Keys
        if (disableRegistryKeys()) changesMade = true

        // 3. Force Advanced Settings
        if (disableAdvancedSettings()) changesMade = true

        if (changesMade) {
            Messages.showWarningDialog(
                project,
                "TUM Exam Mode has disabled $disabledPlugins common AI/ML plugins.\n\n" +
                        "⚠️ IMPORTANT USER RESPONSIBILITY:\n" +
                        "This plugin only targets known AI tools (Copilot, Junie, etc.).\n" +
                        "If you have installed any other niche or personal AI assistants not listed here, YOU must disable them manually to comply with exam regulations.\n\n" +
                        "You MUST close and reopen IntelliJ immediately for these changes to take full effect.",
                "Exam Mode Enforced"
            )
        }
    }

    private fun disableForbiddenPlugins(): Int {
        var count = 0
        for (idString in forbiddenPlugins) {
            val pluginId = PluginId.getId(idString)
            val pluginDescriptor = PluginManagerCore.getPlugin(pluginId)

            // Check if plugin exists AND is NOT explicitly disabled
            if (pluginDescriptor != null && !PluginManagerCore.isDisabled(pluginId)) {
                PluginManagerCore.disablePlugin(pluginId)
                println("TUM Exam Mode: Disabled plugin $idString")
                count++
            }
        }
        return count
    }

    private fun disableRegistryKeys(): Boolean {
        var changed = false
        for (key in forbiddenRegistryKeys) {
            try {
                if (Registry.`is`(key)) {
                    Registry.get(key).setValue(false)
                    changed = true
                }
            } catch (e: Exception) {
                // Key missing in this version
            }
        }
        return changed
    }

    private fun disableAdvancedSettings(): Boolean {
        var changed = false
        try {
            // Force disable the global setting for inline completion
            try {
                AdvancedSettings.setBoolean("editor.inline.completion.enabled", false)
                changed = true
            } catch (e: Exception) {
                // Setting might not exist
            }
        } catch (e: Throwable) {
            // Class might not exist
        }
        return changed
    }
}
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

    private val forbiddenPlugins = listOf(
        "org.jetbrains.completion.full.line",
        "com.intellij.completion.full.line",
        "com.intellij.ml.llm",
        "com.intellij.junie",
        "org.jetbrains.junie",
        "com.jetbrains.junie",
        "com.intellij.completion.ml.ranking",
        "com.intellij.searcheverywhere.ml",
        "com.intellij.marketplace.ml",
        "com.intellij.turboComplete",
        "com.intellij.inline.completion",
        "com.intellij.grazie.pro",
        "com.github.copilot",
        "com.github.copilot.chat",
        "aws.toolkit",
        "com.sourcegraph.jetbrains",
        "com.tabnine.TabNine",
        "com.codeium.intellij",
        "co.bito.bito-intellij",
        "ai.blackbox.jetbrains",
        "com.tabbyml.intellij-tabby",
        "com.fittentech.fittencode",
        "com.alibaba.tongyi.lingma",
        "cn.aminer.geex",
        "com.marscode.marscode",
        "com.codigami.launchable",
        "com.mintlify.doc_writer",
        "com.codium.ai",
        "com.rubberduck.rubberduck",
        "com.supermaven.intellij",
        "io.continuum.continue"
    )

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

        if (disableForbiddenPlugins() > 0) changesMade = true
        if (disableRegistryKeys()) changesMade = true
        if (disableAdvancedSettings()) changesMade = true

        if (changesMade) {
            Messages.showWarningDialog(
                project,
                "TUM Exam Mode has disabled common AI/ML plugins.\n\n" +
                        "⚠️ IMPORTANT:\n" +
                        "This plugin only targets known AI tools (Copilot, Junie, etc.).\n" +
                        "If you have niche/personal AI tools installed, YOU must disable them manually.\n\n" +
                        "Please CLOSE and REOPEN IntelliJ for changes to take effect.",
                "Exam Mode Enforced"
            )
        }
    }

    private fun disableForbiddenPlugins(): Int {
        var count = 0
        for (idString in forbiddenPlugins) {
            val pluginId = PluginId.getId(idString)
            val pluginDescriptor = PluginManagerCore.getPlugin(pluginId)

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
                // Ignore
            }
        }
        return changed
    }

    private fun disableAdvancedSettings(): Boolean {
        var changed = false
        try {
            AdvancedSettings.setBoolean("editor.inline.completion.enabled", false)
            changed = true
        } catch (e: Throwable) {
            // Ignore
        }
        return changed
    }
}
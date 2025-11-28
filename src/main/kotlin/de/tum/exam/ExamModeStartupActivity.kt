package de.tum.exam

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.registry.Registry

class ExamModeStartupActivity : ProjectActivity {

    private val forbiddenPlugins = listOf(
        "com.github.copilot",
        "com.intellij.ml.llm",
        "com.intellij.completion.full.line",
        "com.tabnine.TabNine",
        "com.codeium.intellij",
        "aws.toolkit",
        "com.sourcegraph.jetbrains"
    )

    override suspend fun execute(project: Project) {
        ApplicationManager.getApplication().invokeLater {
            enforceExamMode(project)
        }
    }

    private fun enforceExamMode(project: Project) {
        val disabledCount = disableForbiddenPlugins()
        disableInlineCompletionSettings()

        if (disabledCount > 0) {
            Messages.showWarningDialog(
                project,
                "TUM Exam Mode has disabled $disabledCount AI-related plugins.\n" +
                        "You must restart IntelliJ for these changes to take full effect.",
                "Exam Mode Enforced"
            )
        }
    }

    private fun disableForbiddenPlugins(): Int {
        var disabledCounter = 0
        for (idString in forbiddenPlugins) {
            val pluginId = PluginId.getId(idString)
            val pluginDescriptor = PluginManagerCore.getPlugin(pluginId)

            if (pluginDescriptor != null && pluginDescriptor.isEnabled) {
                PluginManagerCore.disablePlugin(pluginId)
                disabledCounter++
            }
        }
        return disabledCounter
    }

    private fun disableInlineCompletionSettings() {
        try {
            // Target the registry key that controls the "gray text" ghost completion
            val registryKey = "llm.enable.ghost.text"
            if (Registry.`is`(registryKey)) {
                Registry.get(registryKey).setValue(false)
            }
        } catch (e: Exception) {
            // Registry key might not be present in older versions
        }
    }
}
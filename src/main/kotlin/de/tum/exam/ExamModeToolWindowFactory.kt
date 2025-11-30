package de.tum.exam

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.SwingConstants
import com.intellij.openapi.ui.Messages

class ExamModeToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val examPanel = ExamStatusPanel(project)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(examPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private class ExamStatusPanel(private val project: Project) : JBPanel<ExamStatusPanel>() {
        private val statusLabel = JBLabel()
        private val detailLabel = JBLabel()

        // --- FULL BAN LIST (Synced with StartupActivity) ---
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

        init {
            layout = BorderLayout()
            border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

            val centerPanel = JBPanel<JBPanel<*>>().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
            }

            statusLabel.font = Font("Arial", Font.BOLD, 16)
            statusLabel.horizontalAlignment = SwingConstants.CENTER
            statusLabel.alignmentX = CENTER_ALIGNMENT

            detailLabel.font = Font("Arial", Font.PLAIN, 12)
            detailLabel.horizontalAlignment = SwingConstants.CENTER
            detailLabel.alignmentX = CENTER_ALIGNMENT

            val refreshButton = JButton("Re-scan & Enforce").apply {
                alignmentX = CENTER_ALIGNMENT
                addActionListener { enforceAndRefresh() }
            }

            centerPanel.add(statusLabel)
            centerPanel.add(javax.swing.Box.createVerticalStrut(10))
            centerPanel.add(detailLabel)
            centerPanel.add(javax.swing.Box.createVerticalStrut(20))
            centerPanel.add(refreshButton)

            add(centerPanel, BorderLayout.CENTER)

            // Initial check
            checkStatus()
        }

        private fun enforceAndRefresh() {
            var disabledCount = 0
            for (id in forbiddenPlugins) {
                val pluginId = PluginId.getId(id)
                val descriptor = PluginManagerCore.getPlugin(pluginId)

                // If plugin is found AND NOT disabled, kill it
                if (descriptor != null && !PluginManagerCore.isDisabled(pluginId)) {
                    PluginManagerCore.disablePlugin(pluginId)
                    println("Exam Mode ToolWindow: Disabled $id")
                    disabledCount++
                }
            }

            if (disabledCount > 0) {
                Messages.showInfoMessage(
                    "Disabled $disabledCount AI plugins.\nPlease restart the IDE to apply changes.",
                    "Enforcement Successful"
                )
            }

            checkStatus()
        }

        private fun checkStatus() {
            var activeForbidden = 0
            for (id in forbiddenPlugins) {
                val pluginId = PluginId.getId(id)
                val descriptor = PluginManagerCore.getPlugin(pluginId)
                if (descriptor != null && !PluginManagerCore.isDisabled(pluginId)) {
                    activeForbidden++
                }
            }

            if (activeForbidden == 0) {
                statusLabel.text = "✔ Exam Mode Active"
                statusLabel.foreground = Color(50, 168, 82) // Green
                detailLabel.text = "All known AI tools are disabled."
            } else {
                statusLabel.text = "❌ RESTRICTED"
                statusLabel.foreground = Color(200, 50, 50) // Red
                detailLabel.text = "$activeForbidden AI plugins are still active!"
            }
        }
    }
}
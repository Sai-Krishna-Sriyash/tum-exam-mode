TUM Exam Mode Plugin

This is an IntelliJ IDEA plugin designed for programming exams at the Technical University of Munich (TUM).

Purpose

To ensure exam integrity, this plugin automatically:

Disables AI Assistants (GitHub Copilot, JetBrains AI, TabNine, Amazon Q, etc.).

Disables "Full Line" Code Completion (The grey ghost text that suggests entire lines).

Warns the user if forbidden plugins were found and disabled.

Supported Exams

This plugin is suitable for:

PGdP (Praktikum Grundlagen der Programmierung)

EIST (Einführung in die Softwaretechnik)

And other CIT programming exams.

How to Install (For Exam Supervisors)

Download the latest .zip release from the releases page.

Open IntelliJ IDEA on the exam machine.

Go to Settings -> Plugins.

Click the Gear Icon ⚙️ -> Install Plugin from Disk....

Select the tum-exam-mode-1.0.0.zip file.

Restart the IDE.

Development

This project is built using Gradle and Kotlin.

Building the Plugin

Run the following command in the terminal:

./gradlew buildPlugin


The resulting plugin zip file will be located in build/distributions/.
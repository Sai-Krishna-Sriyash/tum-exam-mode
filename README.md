TUM Exam Mode Plugin (Unofficial)

Author: Sriyash Kommalapati

Current Version: 1.0.4

⚠️ DISCLAIMER: > This plugin is a private project developed by Sriyash Kommalapati.

It is NOT an official product of the Technical University of Munich (TUM), the PGdP Team, or the School of CIT.

The author assumes no liability for malfunctions, lost data, or issues arising from the use of this software. Use at your own risk.

Overview

This IntelliJ IDEA plugin is designed to enforce exam regulations by automatically disabling AI assistance tools. It is useful for setting up "Exam Mode" environments on student laptops or lab machines.

It automatically disables:

🚫 JetBrains Full Line Code Completion (The local AI model)

🚫 GitHub Copilot & Copilot Chat

🚫 JetBrains AI Assistant & Junie

🚫 Turbo Complete & ML-assisted Sorting

🚫 Inline Completion (Gray ghost text)

🚫 20+ Other AI Plugins (Bito, Tabnine, Codeium, Amazon Q, etc.)

⚠️ Important: Limitations & User Responsibility

While this plugin targets the most common and popular AI assistants (over 25 known plugins), it cannot guarantee that EVERY possible AI tool is disabled.

If a user has installed a niche, obscure, or brand-new AI plugin that is not on our blocklist, it is the user's sole responsibility to disable it manually. The user must ensure their environment complies with all exam regulations. This tool is a helper, not a guarantee.

How to Build & Install (For Developers)

If you have cloned this repository, follow these steps to generate the plugin file:

1. Build the Plugin

Open a terminal inside the project folder and run:

Mac/Linux:

./gradlew buildPlugin


Windows (Command Prompt/PowerShell):

gradlew buildPlugin


2. Locate the File

Once the build completes (approx. 20-60 seconds), the installable file will be located at:
build/distributions/tum-exam-mode-1.0.4.zip

3. Install in IntelliJ

Open IntelliJ IDEA.

Go to Settings (Mac) / File > Settings (Windows).

Navigate to Plugins.

Click the Gear Icon ⚙️ (top right of the panel).

Select "Install Plugin from Disk...".

Select the .zip file you just built.

Close and Reopen the IDE.

How it Works

Upon startup, the plugin scans for known AI plugins (by ID) and bundled machine learning features. If found, it programmatically disables them and modifies the IDE's internal registry to prevent "ghost text" completions. A warning dialog is shown to the user if any features were disabled.

License

This project is licensed under the MIT License - see the LICENSE file for details.
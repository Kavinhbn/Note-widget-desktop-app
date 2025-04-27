# Note-widget-desktop-app
🗒️ Sticky Notes JavaFX App
A lightweight, customizable sticky notes desktop app built using Java and JavaFX.
It allows you to create, save, edit, analyze, and organize your notes with ease!

✨ Features
  📜 Create Unlimited Notes — Each note opens in a separate window.

  🎨 Color Picker — Personalize the background color of each note.

  💾 Save Notes Automatically — Save as .txt files with timestamps.

  🗓️ Calendar View — View notes created on any specific date.

  📊 Analytics Dashboard — See how many notes were created each day.

  📂 Export Notes — Download notes as .txt or .pdf files.

  ⏰ Real-time Timestamp — Displays date and time of note creation.

  🔥 Cool, Responsive UI — Designed for a modern, clean experience.
  ![demo](https://github.com/user-attachments/assets/3f8f230b-868c-453d-9920-8cef0700553e)
  ![demo 2](https://github.com/user-attachments/assets/bb91b6e5-598a-4b11-a708-caad11e8a08f)


📁 Folder Structure
  plaintext
  Copy
  Edit
  project-root/
  ├── README.md
  ├── src/
  │   └── App.java
  ├── notes/        <-- (All your saved notes stored here)
  └── lib/          <-- (JavaFX SDK files here)
🚀 How to Run
Install Java JDK 17+ and JavaFX SDK 24+ on your system.

Set up JavaFX:

bash
Copy
Edit
  javac --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml src/App.java
  java --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp src App
Enjoy creating sticky notes!

🛠 Requirements
Java 17 or higher

JavaFX SDK 24 or higher

A code editor (VSCode, IntelliJ IDEA, or any Java IDE)

📅 Future Plans
   🧠 GPT-powered smart note suggestions

   🔔 Reminders and notifications

  🌐 Cloud sync (Google Drive or custom server)

  🎙️ Voice-to-Text Note Creation

  🗃️ Tagging system for better organization

🙌 Contribution
Pull requests are welcome! Feel free to suggest features or report bugs.
Let’s build the ultimate sticky note app together 🚀

📜 License
This project is open-source and available under the MIT License.

This is a To-Do App developed for Android using Jetpack Compose, Firebase Authentication, Room Database, and Camera API. The app allows users to register, log in, create tasks, take images for tasks, and manage their to-dos efficiently.

Features:
User Authentication: Users can sign up and log in using their email and password via Firebase Authentication.
Task Management: Users can create, view, edit, and delete tasks. Each task can also be marked as important.
Camera Integration: Users can capture and attach images to tasks.
GDPR Compliance: The app ensures compliance with GDPR, asking for user consent for data collection.
Dark/Light Mode: Users can switch between light and dark themes for a better user experience.

Technologies Used:
Android SDK (Jetpack Compose for UI)
Firebase Authentication (for user authentication)
Room Database (for local data storage)
Camera API (for image capture)
DataStore (for storing preferences like theme mode and GDPR consent)
Material Design (for UI components)

Prerequisites:
Android Studio (latest version)
Java Development Kit (JDK)
Firebase Project with Authentication and Firestore enabled.

Getting Started:
Open the project in Android Studio.
Go to the Firebase Console.
Create a new project or use an existing one.
Set up Firebase Authentication with email/password sign-in enabled.
Download the google-services.json file and place it in the app directory of your Android project.
Once Firebase is set up, sync the project to ensure all necessary dependencies are downloaded.
Select an Android Virtual Device (AVD) or connect your physical device to run the app.
Click on the Run button in Android Studio to install and launch the app on the emulator or your physical device.
App Flow:
Sign Up / Login:
New users can sign up with email and password. Existing users can log in.
Task Creation:
Users can create tasks by entering a title and description. They can also take photos and attach them to tasks.
Task Management:
Users can edit, delete, or mark tasks as completed.
Camera Integration:
Users can take photos using their device's camera and attach them to specific tasks.
Theme Preferences:
Users can toggle between light and dark themes, enhancing usability.
Privacy and Security:
The app collects personal data, including the user's Email, Password, and Task details.
GDPR Compliance: The app asks for user consent regarding data collection and usage. Users can opt out and delete their data anytime.
Firebase Authentication ensures secure login and session management.
Task Images are stored using Base64 encoding to protect privacy.

Licensing:
This project is licensed under the "Apache 2.0" License.

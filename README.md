Group Members:

Cameron Daniel Colley - ST10037966 
Dylan Lee Padayachee - ST10177615 
Kurt Joshau Siebritz - ST10208082 



mindXcape
mindXcape is an Android application designed for mood tracking and journaling. Users can log their moods, visualize mood trends, and manage their profiles.


Table of Contents
Features
Technologies
Installation
Usage
Activities Overview
License


Features
User registration and login
Mood selection and logging
Mood visualization with bar charts
Journaling
User profile management


Technologies
Android SDK
Kotlin
Firebase Authentication
Firebase Realtime Database
MPAndroidChart for data visualization



Installation
Clone the repository:
bash
Copy code
git clone https://github.com/kurtsiebritz/OPSC7311poe.git
Open the project in Android Studio.
Set up Firebase in your project:
Create a Firebase project in the Firebase Console.
Add your Android app to the project and follow the setup instructions.
Sync your project with Gradle files.
Run the app on an emulator or a physical device.



Usage
Launch the app.
Register a new account or log in with existing credentials.
Select your mood using the mood icons on the main screen.
View your mood history and trends in the mood tracking section.
Create a journal entry and save it.
Navigate to your profile to edit account details.










Activities Overview
MainActivity
Description: The main interface where users can select their mood and navigate to other sections of the app.
Features:
Mood selection using icons.
Save selected mood to Firebase.
Navigation buttons to access different parts of the app (Profile, Journal, Audio, Mood Tracking).

ComposeActivity
Description: Allows users to write journal entries related to their mood.
Features:
Text input for journaling.
Save entries linked to the selected mood.
Optional image attachments.

MoodTrack
Description: Displays a bar chart of the user's mood history.
Features:
Visual representation of moods over time using a bar chart.
Options to view mood data for the last 12 days or weekly averages.
Dynamic updates based on user-selected time frames.

EditProfileActivity
Description: Enables users to update their profile information.
Features:
Fields to edit name, surname, email, and password.
Update user information in Firebase.




AccountActivity
Description: Displays user account details and provides options for logging out.
Features:
Show user's name, email, and mood history.
Logout functionality.

Register
Description: User registration activity for creating a new account.
Features:
Input fields for name, surname, email, password, and password confirmation.
Firebase Authentication to create a new user account.
Navigate to login screen if the user already has an account.

Login
Description: Allows users to log in to their existing account.
Features:
Input fields for email and password.
Firebase Authentication to log in users.


License
This project is licensed under the MIT License. See the LICENSE file for details.


Youtube Video Link:
https://youtu.be/LypkwY1r6BM 

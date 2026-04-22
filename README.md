# Tracking-Steps
Android application for tracking steps and calories burned in a session

## Features
- Uses Android's built in sensors for tracking user steps/calories
- Displays a persistent notification when a user starts a session
- Integrated with Google's Health Connect API so user steps can be read from different fitness apps (todo)
- Uses Firebase's Gemini api to determine how many calories are in a food item

## Project Structure
```text
├── .github
│   └── workflows
├── app
│   └── src
│       └── main
│           └── java/com/example/tracking_steps
│               ├── firebase
│               ├── nav
│
│
├── feature
│   ├── goals
│   │   └── src
│   │       └── main
│   │           └── java/com/example/goals
│   └── home
│       └── src
│           └── main
│               └── java/com/example/feature_home
├── gradle
│   └── wrapper
├── permissions-details
│   └── src
│       └── main
│           └── java/com/example/core_permissions
│               └── ui
│                   └── theme
└── utility
    └── src
        └── main
            └── java/com/example/utility
                ├── activity_checker
                ├── composables
                ├── data
                │   ├── db
                │   └── di
                ├── di
                ├── foreground
                ├── health_connect
                └── sensor
```
## Project Overview
- **app**: Main entry point. Responsible for
  - Navigation between screens
  - Gemini model setup
- **feature/home**
  - Presentation for home screen
  - Here, users can view their current steps, calories burned, and calories consumed.
 - **feature/goals**
    - Presentation for goals screen
    - Users can set their goal for how many steps they want to complete in a session or how many calories they want to burn in a session
  - **utility**
    - Includes custom composables and data that can be shared within different modules
### Steps to run
- From Android Studio, import this project from version control
- Setup google-services.json in the root app's directory

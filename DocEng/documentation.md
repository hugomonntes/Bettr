# Bettr Project Documentation

## What is Bettr?

Bettr is a social habit tracking application. The idea is simple: users can create and share their daily habits with other people. Think of it like Twitter but for habits instead of tweets.

The project has four main parts:
1. A REST API (backend)
2. An Android mobile app
3. A Windows desktop app
4. A PHP web app

All these parts talk to each other through the API. The API connects to a cloud database called Supabase (which is like PostgreSQL).

---

## Project Structure

The main folder contains:
- `api_rest/` - The REST API (Java)
- `Bettr_Mobile_App/` - The Android app (Kotlin/Java)
- `Bettr_Desktop_App/` - The Windows desktop app (C#)
- `Bettr_Web_App/` - The web app (PHP)
- `Doc/` - Documentation in Spanish
- `DocEng/` - This documentation in English

---

## 1. The REST API (api_rest)

### What is this?
This is the backend. It handles all the data. When you do something in any app (like posting a habit), it talks to this API first. The API then talks to the database.

### How it works
The API is made with Java and uses a thing called JAX-RS ( Jersey). It runs on a server and exposes URLs that the apps can call.

### Main Files
- `UsersManager.java` - Handles user things like login, register, profile
- `HabitManager.java` - Handles habit things like creating habits, getting the feed, likes
- `Users.java` - The user data model
- `Habit.java` - The habit data model
- `pom.xml` - This tells Maven how to build the project

### Key Endpoints (URLs that the apps call)

For users:
- `POST /users` - Register a new user
- `GET /users/{username}` - Get user by username
- `GET /users/{id}` - Get user by ID
- `GET /users` - Search users
- `POST /users/{id}/profile` - Update profile

For habits:
- `POST /habits` - Create a new habit
- `GET /habits/feed/{userId}` - Get the feed (habits from people you follow)
- `GET /habits/user/{userId}` - Get habits from a specific user
- `POST /habits/{id}/like/{userId}` - Like a habit
- `DELETE /habits/{id}/like/{userId}` - Unlike a habit

For followers:
- `POST /users/follow/{followerId}/{followingId}` - Follow someone
- `DELETE /users/unfollow/{followerId}/{followingId}` - Unfollow someone
- `GET /users/{userId}/followers` - Get followers
- `GET /users/{userId}/following` - Get who you follow

### Where is the API hosted?

The API is hosted on **Render**, which is a cloud platform for hosting web services. Render has a free tier that we are using for this project.

**Base Endpoint URL:**
```
https://bettr-g5yv.onrender.com/rest
```

So when you want to call the API, you use this URL as the base. For example:
- Users: `https://bettr-g5yv.onrender.com/rest/users`
- Habits: `https://bettr-g5yv.onrender.com/rest/habits`

**Important:** Because we are using the free plan of Render, the API takes a while to respond. The first time you call it (after some minutes of inactivity), it can take **about 5 to 10 seconds** to wake up. This is normal. Just wait a bit and try again. After the first call, it stays awake for a while and responds faster.

If you deploy your own version to Render, you will get a different URL.

---

## 2. Android Mobile App (Bettr_Mobile_App)

### What is this?
An Android app made with Java. You can install it on your phone and use it to post habits, see your feed, and follow other users.

### How it works
It uses Android Studio and Gradle to build. The app has different screens (Activities and Fragments). Each screen shows different things.

### Main Files
- `Login.java` and `Register.java` - Login and register screens
- `Intro.java` - The welcome screen when you open the app
- `Feed.java` - The main feed where you see habits
- `UserSession.java` - Saves the user data so you stay logged in
- `ApiRest/Api_Gets.java` - Makes GET requests to the API
- `ApiRest/Api_Inserts.java` - Makes POST requests to the API
- `Dao/Connection.java` - Database connection for local storage
- `Fragments/` - Different parts of the screen (Feed, Camera, Profile, etc.)
- `Adapters/` - These connect data to the screen (like showing a list of habits)

---

## 3. Windows Desktop App (Bettr_Desktop_App)

### What is this?
A desktop application made with C# and Windows Forms. It does the same things as the Android app but runs on a computer.

### How it works
It uses .NET Framework 4.7.2. The app connects to the same API as the other apps, so all the data is the same.

### Main Files
- `Login.cs` - Login screen
- `Register.cs` - Register screen
- `Dashboard.cs` - The main screen where you see feed, your habits, discover users
- `CompleteProfile.java` - Screen to complete your profile
- `Api/ApiService.cs` - Handles all API calls
- `Models/User.cs` and `Models/Habit.cs` - Data models

---

## 4. Web App (Bettr_Web_App)

### What is this?
A web application made with PHP. You can open it in a browser and use it like the other apps.

### How it works
It's a simple PHP application. It uses a basic MVC pattern (Model-View-Controller). The web app talks to the API to get and send data.

### Main Files
- `public/index.php` - The main entry point
- `public/login.php` and `public/registro.php` - Login and register pages
- `public/dashboard.php` - The main dashboard
- `public/api_proxy.php` - Acts as a bridge to the API
- `app/config/database.php` - Database configuration
- `app/services/AuthService.php` - Handles authentication
- `app/controllers/` - Controller files
- `app/models/` - Model files
- `app/helpers/session.php` - Manages user sessions

---

## 5. Database (Supabase/PostgreSQL)

### What is this?
All the data is stored in a PostgreSQL database on Supabase. This is in the cloud.

### Tables
- `users` - Stores user info (name, username, email, password, avatar, description)
- `habits` - Stores the habits (user_id, description, image, type, created_at)
- `followers` - Stores who follows who (follower_id, following_id)
- `habit_likes` - Stores who likes which habit

### Important
The passwords are not stored as plain text. They are hashed using BCrypt. This is for security.

---

## How the Apps Talk to Each Other

1. User opens an app (Android, Desktop, or Web)
2. User logs in
3. App sends username and password to the API
4. API checks if the password is correct using BCrypt
5. API returns the user data
6. App saves this data locally (so you stay logged in)
7. When user posts a habit, app sends it to the API
8. API saves it to the database
9. When user wants to see the feed, app asks API
10. API gets data from database and returns it
11. App shows the data to the user

All three apps use the same API, so if you post something from the web app, you can see it in the Android app too.

---

## Technologies Used

- **Java** - For the REST API
- **Kotlin/Java** - For Android
- **C#** - For Windows Desktop
- **PHP** - For Web
- **Gradle** - Build tool for Android
- **Maven** - Build tool for Java API
- **REST API** - Way for apps to communicate
- **JSON** - Format to exchange data
- **BCrypt** - To hash passwords securely
- **PostgreSQL** - Database
- **Supabase** - Cloud database service

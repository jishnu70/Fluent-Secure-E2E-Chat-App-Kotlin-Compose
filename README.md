# 🗨️ Fluent - Secure Chat App with End-to-End Encryption

**Fluent** is a secure, real-time Android chat application built with Jetpack Compose and powered by a Kotlin-based architecture. It features **end-to-end encrypted messaging**, **token-based authentication**, and **WebSocket-based real-time communication** — all backed by a FastAPI server.

---

## ✨ Features

- 🔐 **End-to-End Encrypted Messaging** (RSA)
- 🌐 **Real-Time Messaging** using Ktor WebSockets
- 🔑 **Secure JWT Authentication** with Refresh Tokens
- 📱 **Jetpack Compose UI** with smooth UX and animations
- 💬 **Chat List & Messaging Interface**
- 🔍 **User Search** (in progress, backend ready)
- 👤 **Profile Sidebar Drawer** (UI implemented, dynamic data coming)
- 🎨 **Custom Bottom Navigation Bar**
- ☁️ **Token Storage using EncryptedSharedPreferences**
- 🧠 **MVVM Architecture + Clean Layer Separation**
- 🛠️ **Dependency Injection using Koin**

---

## 📸 Screenshots

> _(Add screenshots or GIFs of chat, login, profile drawer, message list here when final polish is done)_

---

## 🧱 Architecture Overview

```
presentation/
    - authentication/
    - chatList/
    - message/
    - splashOpening/
    - profile/
data/
    - dto/
    - remote/
    - mapper/
    - repository/
    - network/
domain/
    - models/
    - repository/
    - utility/
di/
    - Koin Modules (Network, ViewModel, Repository)
```

- **Jetpack Compose** for UI
- **Ktor HTTP & WebSocket Client**
- **RSA Encryption** using Android Keystore
- **Flow & State Management** via StateFlow

---

## 🔐 End-to-End Encryption Details

Each user device generates an RSA keypair using Android's Keystore API. Messages are encrypted with the recipient's public key and decrypted with the private key on-device. Messages are never stored or transmitted in plain text.

---

## 🔧 Tech Stack

- **Kotlin** + **Jetpack Compose**
- **Ktor Client (HTTP/WebSocket)**
- **Koin** (Dependency Injection)
- **EncryptedSharedPreferences**
- **Android Keystore** (RSA Keypair generation)
- **FastAPI** (Backend)
- **JWT** (Access + Refresh Tokens)

---

## 🚧 Future Work (Already In Progress)

- [x] **Profile Screen Dynamic Content** (Logout, username, public key info)
- [x] **User Search Screen UI**
- [x] **User Search API Integration**
- [ ] **Push Notification Support (Firebase)**
- [ ] **Typing Indicator via WebSocket**
- [ ] **Media Sharing (Images, Voice, Files)**
- [ ] **Offline Mode with Room DB**

---

## 🧪 How to Run

1. Clone this repo:
   ```bash
   git clone https://github.com/jishnu70/Fluent-Secure-E2E-Chat-App-Kotlin-Compose.git
   ```

2. Open in **Android Studio** (Kotlin 1.9+, Compose Compiler required)

3. Run the FastAPI backend (see server repo or backend setup instructions)

4. Launch the app on emulator or physical device (Android 8.0+)

---

## 🤝 Contributing

This project is currently solo-built. Feel free to suggest features or improvements via issues or pull requests!

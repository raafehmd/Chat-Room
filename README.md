# Multi-Client Chat Application 🚀

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue)
![Sockets](https://img.shields.io/badge/Networking-Sockets-green)

A real-time chat application supporting multiple concurrent clients with both GUI (JavaFX) and console interfaces.

## ✨ Features
- **Multi-client support** - Many users can chat simultaneously
- **Two interface options**:
  - Modern JavaFX GUI
  - Lightweight console version
- **Unique usernames** - No duplicate names allowed
- **Message broadcasting** - All clients see all messages
- **Graceful disconnection** - Proper cleanup when users leave

## 🛠️ Prerequisites
- Java JDK 17 or later
- For GUI version: JavaFX SDK (included in JDK 8-11; [download separately](https://gluonhq.com/products/javafx/) for JDK 17+)

## 🚀 Quick Start

### 1. Clone the Repository
```
git clone https://github.com/your-username/multi-client-chat.git
cd multi-client-chat
```

### 2. Compile and Run the Server
```
javac ChatServer.java
java ChatServer
```
- Server starts on port 12345 (default)


### 3. Run Clients
#### Option A: JavaFX GUI Client (Recommended)
```
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls ChatClientFX.java
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls ChatClientFX
```

#### Option B: Console Client
```
javac ChatClient.java
java ChatClient
```

## 🖥️ Usage
1. When prompted, enter a unique username
2. Type messages and press Enter to send
3. Type quit to disconnect gracefully


## 📂 Project Structure
```
multi-client-chat/
├── ChatServer.java       # Server implementation
├── ChatClient.java       # Console client
├── ChatClientFX.java     # JavaFX GUI client
└── README.md            # This documentation
```


## 🛠️ Configuration
- Change server port in ChatServer.java (line with private static final int PORT)
- Update server IP in ChatClient.java/ChatClientFX.java if not running locally

## 🐛 Troubleshooting
Issue              |  Solution
Port in use	       |  Change port number or close other instances
JavaFX errors	     |  Verify module path points to JavaFX SDK
Connection refused |	Ensure server is running first
Username taken	   |  Choose a different name


## 🤝 Contributing
Contributions welcome! Please:
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Open a pull request

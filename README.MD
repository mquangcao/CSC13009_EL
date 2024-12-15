# Cấu trúc thư mục dự án
```
src/
└── main/
    ├── java/com/yourapp/
    │   ├── app/
    │   │   ├── data/                  # Tầng Data
    │   │   │   ├── local/             # Quản lý SQLite (Local Database)
    │   │   │   │   ├── dao/           # Các DAO (Data Access Objects)
    │   │   │   │   │   ├── UserDao.kt
    │   │   │   │   │   ├── WordDao.kt
    │   │   │   │   │   └── ChapterDao.kt
    │   │   │   │   ├── entity/        # Entity đại diện cho các bảng SQLite
    │   │   │   │   │   ├── UserEntity.kt
    │   │   │   │   │   ├── WordEntity.kt
    │   │   │   │   │   └── ChapterEntity.kt
    │   │   │   │   ├── AppDatabase.kt # Lớp quản lý SQLite Database
    │   │   │   │   └── migrations/    # Các file migration
    │   │   │   │       ├── Migration1to2.kt
    │   │   │   │       └── Migration2to3.kt
    │   │   │   ├── remote/            # Quản lý Firebase Realtime Database
    │   │   │   │   ├── FirebaseService.kt  # Class xử lý API với Firebase
    │   │   │   │   ├── FirebaseMapper.kt   # Mapper dữ liệu Firebase → Entity
    │   │   │   │   └── FirebaseUtils.kt    # Tiện ích hỗ trợ Firebase
    │   │   │   └── repository/        # Repository để gọi đến các dao, ví dụ cần gọi 2 3 DAO để kết hợp data
    │   │   │       ├── UserRepository.kt
    │   │   │       ├── ChapterRepository.kt
    │   │   │       └── WordRepository.kt
    │   │   ├── domain/                # Tầng Domain
    │   │   │   ├── model/             # Domain Models (Aggregate Root)
    │   │   │   │   ├── User.kt
    │   │   │   │   ├── Word.kt
    │   │   │   │   └── Chapter.kt
    │   │   │   └── repository/        # Interface cho repository (Dependency Inversion)
    │   │   │       ├── IUserRepository.kt
    │   │   │       ├── IChapterRepository.kt
    │   │   │       └── IWordRepository.kt
    │   │   ├── presentation/          # Tầng Presentation (UI Layer)
    │   │   │   ├── activity/         # Các Activity
    │   │   │   │   ├── LoginActivity.kt
    │   │   │   │   ├── SingUpActivity.kt
    │   │   │   │   └── DashboardActivity.kt
    |   |   |   ├── fragment/ 
    │   │   │   ├── service/        # các dịch vụ, ví dụ như lưu thông tin ngôn ngữ xuống SharedPreferences,... 
    │   │   │   │   ├── Button.kt
    │   │   │   │   ├── Dialog.kt
    │   │   │   │   └── ListView.kt
    │   │   ├── utils/                 # Tiện ích dùng chung
    │   │   │   ├── adapter/           # Chứa các adapter
    │   │   │   ├── extensions/        # Extension functions
    │   │   │   ├── constants/         # Hằng số
    │   │   │   │   ├── AppConstants.kt
    │   │   │   │   └── FirebaseKeys.kt
    │   │   │   └── mapper/            # Mapper từ Entity ↔ Domain Model
    │   │   │       ├── UserMapper.kt
    │   │   │       ├── WordMapper.kt
    │   │   │       └── ChapterMapper.kt
    │   └── MainActivity.kt            # Entry Point của ứng dụng
    ├── res/                           # Tài nguyên (Layout, String, v.v.)
    └── AndroidManifest.xml            # File cấu hình chính

```


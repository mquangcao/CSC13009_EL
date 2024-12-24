com.example.myapp
└── data
│   ├── local
│   │   ├── entity
│   │   └── ...
│   ├── remote      // For Firebase
│   │   ├── repository
│   │   │   ├── FirebaseAuthRepository.kt   // For FirebaseAuth method
│   │   │   └── FirestoreUserRepository.kt  // For Firestore method
│   │   └── model
│   │       ├── FirebaseUserModel.kt   //no need
│   │       └── FirestoreUserModel.kt  // Firestore class
│   └── repository
│       ├── UserRepository.kt          
├── domain
│   ├── model
│   │   └── UserModel.kt
│   └── repository
│       └── IUserRepository.kt
└── presentation
│   ├── viewmodel
│       └── UserViewModel.kt



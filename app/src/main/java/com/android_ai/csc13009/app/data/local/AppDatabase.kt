package com.android_ai.csc13009.app.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android_ai.csc13009.app.data.local.dao.*
import com.android_ai.csc13009.app.data.local.entity.*


@Database( entities  = [
        UserEntity::class,
        QuestionsEntity::class,
        AnswersEntity::class,
        ChapterEntity::class,
        LessonEntity::class,
        WordEntity::class,
        UserTagEntity::class,
        WordTagEntity::class,
        UserLessonLearnedEntity::class,
        UserChapterLearnedEntity::class,
        LearningDetailEntity::class,
        GameDataEntity::class
        LearningDetailEntity::class,
        GrammarLevelEntity::class,
        GrammarTopicEntity::class,
        GrammarSubtopicEntity::class
                       ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    // Định nghĩa các DAO
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun chapterDao(): ChapterDao
    abstract fun lessonDao(): LessonDao
    abstract fun wordDao(): WordDao
    abstract fun userTagDao(): UserTagDao
    abstract fun wordTagDao(): WordTagDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun learningDetailDao(): LearningDetailDao
   // abstract fun gameDataDao(): GameDataDao

    // grammar
    abstract fun grammarLevelDao(): GrammarLevelDao
    abstract fun grammarTopicDao(): GrammarTopicDao
    abstract fun grammarSubtopicDao(): GrammarSubtopicDao


    companion object {
        const val DATABASE_NAME = "app_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .createFromAsset("dictionary.db")
                    .fallbackToDestructiveMigration()
                    .build().also {
                        instance = it
                    }
                }
            }
        }
    }
}

package com.android_ai.csc13009.app.data.repository

import android.content.Context
import android.util.Log
import com.android_ai.csc13009.app.data.local.AppDatabase
import com.android_ai.csc13009.app.domain.models.Chapters
import com.android_ai.csc13009.app.domain.models.ListeningTopic
import com.android_ai.csc13009.app.domain.models.ListeningTopics
import com.android_ai.csc13009.app.domain.repository.IListeningTopicRepository

class ListeningTopicRepository(val context: Context) : IListeningTopicRepository {
    private val database = AppDatabase.getInstance(context)

    override suspend fun getTopicList(): List<ListeningTopics> {
        return try {
            val topics = database.listeningTopicDao().getAllTopic()

            topics.map {
                val details = getTopicDetails(it.id)

                ListeningTopics(
                    id = it.id,
                    chapterName = it.title,
                    totalLesson = details.totalLesson,
                    lessonFinished = details.lessonFinished,
                    thumbnailUrl = it.thumbnailUrl
                )
            }

        }
        catch (e: Exception) {
            Log.d("ChapterRepository", "getChapterList: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getTopicDetails(topicId: String): ListeningTopic {
        return try {
            val topic = database.listeningTopicDao().getTopicById(topicId) ?: throw Exception("topic not found")

            val lessonRepository = ListeningLessonRepository(context)

            ListeningTopic(
                id = topic.id,
                title = topic.title,
                totalLesson = lessonRepository.getLessonsByTopicId(topic.id).size,
                lessonFinished = lessonRepository.getLessonsByTopicId(topic.id).filter { it.isOpenByProgress }.size,
                thumbnailUrl = topic.thumbnailUrl,
                lessons = lessonRepository.getLessonsByTopicId(topic.id)
            )
        } catch (e: Exception) {
            Log.d("ChapterRepository", "getChapterDetail: ${e.message}")
            ListeningTopic(
                id = TODO(),
                title = TODO(),
                totalLesson = TODO(),
                lessonFinished = TODO(),
                thumbnailUrl = TODO(),
                lessons = TODO(),
            )
        }

    }
}
package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.domain.models.Answer
import com.android_ai.csc13009.app.domain.models.AnswerWord
import com.android_ai.csc13009.app.domain.models.Chapter
import com.android_ai.csc13009.app.domain.models.Chapters
import com.android_ai.csc13009.app.domain.models.Lesson
import com.android_ai.csc13009.app.domain.models.Question
import com.android_ai.csc13009.app.domain.repository.IChapterRepository
import kotlin.collections.ArrayList

class ChapterRepository : IChapterRepository {
    override fun getChapterList(): List<Chapters> {
        return listOf(
            Chapters(1, "Animal", 4, 2, 0, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"),
            Chapters(2, "Housework ", 56, 10, 2, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676124/mqc/title_bar.jpg"),
            Chapters(3, "Shopping", 12, 10, 1, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733675508/mqc/sk.jpg"),
            Chapters(4, "Food and drinks", 9, 10, 10, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733160086/mqc/uwp4374814.png"),
            Chapters(5, "Transportation ", 11, 10, 2, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1732696103/mqc/genz_it.jpg"),
            Chapters(6, "Workplace ", 34, 10, 2, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733155927/mqc/Screenshot_2024-10-17_164855.png"),
            Chapters(7, "Health ", 32, 15,10 , "https://res.cloudinary.com/dxz9cc66y/image/upload/v1732694685/cld-sample.jpg"),
            Chapters(8, "Travel and Tourism", 6, 11, 2, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1732694685/samples/woman-on-a-football-field.jpg"),
            Chapters(9, "Weather and Nature", 76, 10, 0, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1732694685/samples/dessert-on-a-plate.jpg")
        )
    }

    override fun getChapterDetail(chapterId: Int): Chapter {
        return Chapter(
            1,
            "Animal",
            10,
            5,
            24,
            "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png",
            listOf(
                Lesson(
                    id = 1,
                    lessonName = "Animal",
                    totalQuestion = 2,
                    questionSuccess = 1,
                    listOf(
                        Question(
                            1,
                            "Mèo",
                            "new_word",
                            ArrayList(
                                listOf(
                                    AnswerWord().apply {
                                        text = "Dog"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    },
                                    AnswerWord().apply {
                                        text = "Cat"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = true
                                    },
                                    AnswerWord().apply {
                                        text = "Bird"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    },
                                    AnswerWord().apply {
                                        text = "Fish"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    }
                                )
                            )
                        ),
                        Question(
                            2,
                            "Chó",
                            "new_word",
                            ArrayList(
                                listOf(
                                    AnswerWord().apply {
                                        text = "Dog"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = true
                                    },
                                    AnswerWord().apply {
                                        text = "Cat"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    },
                                    AnswerWord().apply {
                                        text = "Bird"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    },
                                    AnswerWord().apply {
                                        text = "Fish"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    }
                                )
                            )
                        ),
                        Question(
                            3,
                            "This is my cat",
                            "meaning",
                            ArrayList(
                                listOf(
                                    AnswerWord().apply {
                                        text = "Đây là con mèo của tôi"
                                        isCorrect = true
                                    },
                                    AnswerWord().apply {
                                        text = "Đây là con chó của tôi"
                                        isCorrect = false
                                    },
                                    AnswerWord().apply {
                                        text = "Đây là con chim của tôi"
                                        isCorrect = false
                                    },
                                    AnswerWord().apply {
                                        text = "Đây là con cá của tôi"
                                        isCorrect = false
                                    }
                                )
                            )
                        ),
                        Question(
                            4,
                            "Đây là Việt Nam",
                            "translate",
                            ArrayList(
                                listOf(
                                    AnswerWord().apply {
                                        text = "This is Vietnam"
                                    }
                                )
                            )
                        ),
                    )
                ),
                Lesson(
                    id = 2,
                    lessonName = "Bird",
                    totalQuestion = 2,
                    questionSuccess = 1,
                    listOf(
                        Question(
                            1,
                            "Mèo",
                            "new_word",
                            ArrayList(
                                listOf(
                                    AnswerWord().apply {
                                        text = "Dog"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    },
                                    AnswerWord().apply {
                                        text = "Cat"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = true
                                    },
                                    AnswerWord().apply {
                                        text = "Bird"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    },
                                    AnswerWord().apply {
                                        text = "Fish"
                                        imgUrl = "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"
                                        isCorrect = false
                                    }
                                )
                            )
                        )
                    )
                )
            )
        )
    }
}
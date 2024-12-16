package com.android_ai.csc13009.app.data.repository

import com.android_ai.csc13009.app.domain.models.Chapters
import com.android_ai.csc13009.app.domain.repository.IChapterRepository

class ChapterRepository : IChapterRepository {
    override fun getChapterList(): List<Chapters> {
        return listOf(
            Chapters(1, "Hello", 24, 10, 5, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676003/mqc/img_thumnail_entrypoint.png"),
            Chapters(2, "Goodbye", 56, 10, 2, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733676124/mqc/title_bar.jpg"),
            Chapters(3, "Thank you", 12, 10, 1, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733675508/mqc/sk.jpg"),
            Chapters(4, "Sorry", 9, 10, 10, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733160086/mqc/uwp4374814.png"),
            Chapters(5, "Please", 11, 10, 2, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1732696103/mqc/genz_it.jpg"),
            Chapters(6, "Yes", 34, 10, 2, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1733155927/mqc/Screenshot_2024-10-17_164855.png"),
            Chapters(7, "No", 32, 15,10 , "https://res.cloudinary.com/dxz9cc66y/image/upload/v1732694685/cld-sample.jpg"),
            Chapters(8, "What", 6, 11, 2, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1732694685/samples/woman-on-a-football-field.jpg"),
            Chapters(9, "When", 76, 10, 0, "https://res.cloudinary.com/dxz9cc66y/image/upload/v1732694685/samples/dessert-on-a-plate.jpg")
        )
    }
}
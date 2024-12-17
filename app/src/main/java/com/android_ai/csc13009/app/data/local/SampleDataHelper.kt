package com.android_ai.csc13009.app.data.local

import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase

object SampleDataHelper {

    fun insertSampleData(db: SupportSQLiteDatabase) {
        try {
            // Check if levels table is empty
            if (isTableEmpty(db, "levels")) {
                // Insert sample data for levels
                db.execSQL("INSERT INTO levels (id, name) VALUES (1, 'Beginner'), (2, 'Independent'), (3, 'Proficient')")

                // Insert sample data for topics including Tense
                db.execSQL("INSERT INTO topics " +
                        "(id, levelId, name) VALUES (1, 1, 'Nouns'), " +
                        "(2, 1, 'Pronouns'), (3, 2, 'Verbs'), " +
                        "(4, 2, 'Adjectives'), (5, 1, 'Tense'), (6, 2, 'Tense'), " +
                        "(7, 3, 'Tense'), (8, 3, 'Subjunctive Mood'), (9, 3, 'Passive Voice')"
                )

                // Insert sample data for subtopics including tenses for each level
                db.execSQL("INSERT INTO subtopics (id, topicId, name, content, structures, examples) VALUES " +
                        // Nouns, Pronouns, Verbs, Adjectives
                        "(1, 1, 'Common Nouns and Proper Nouns', 'Nouns are words that name people, places, things, or ideas. They can be common (general) or proper (specific).', 'Common Nouns: cat, city, book\\nProper Nouns: Tom, Paris, War and Peace', 'Example: ''The cat (common noun) named Tom (proper noun) is sleeping.'''), " +
                        "(2, 1, 'Concrete and Abstract Nouns', 'Concrete nouns name things that you can see, hear, touch, smell, or taste. Abstract nouns name ideas, qualities, or conditions.', 'Concrete: table, dog, flower\\nAbstract: love, bravery, sadness', 'Example: ''Love is an abstract noun.'''), " +
                        "(3, 1, 'Countable and Uncountable Nouns', 'Countable nouns refer to things that can be counted (one apple, two apples). Uncountable nouns refer to things that cannot be counted (milk, music).', 'Countable: apple, car, book\\nUncountable: milk, water, information', 'Example: ''There is some water in the bottle.'''), " +
                        "(4, 1, 'Collective Nouns', 'Collective nouns refer to a group of people, animals, or things considered as a single unit.', 'Examples: family, team, jury', 'Example: ''The team is winning.'''), " +
                        "(5, 1, 'Possessive Nouns', 'Possessive nouns show ownership or possession.', 'Formed by adding an apostrophe and an s (''s) or just an apostrophe (’’) in some cases.', 'Example: ''The cat''s toy.'''), " +
                        "(6, 2, 'Personal Pronouns', 'Pronouns are words that replace nouns in a sentence. Personal pronouns refer to specific people or things.', 'I, you, he, she, it, we, they', 'Example: ''She is reading a book.'''), " +
                        "(7, 2, 'Possessive Pronouns', 'Possessive pronouns show ownership.', 'mine, yours, his, hers, its, ours, theirs', 'Example: ''The book is mine.'''), " +
                        "(8, 3, 'Regular Verbs', 'Regular verbs follow a predictable pattern when changing tenses.', 'Present: talk, Past: talked, Past Participle: talked', 'Example: ''He talked to me.'''), " +
                        "(9, 3, 'Irregular Verbs', 'Irregular verbs do not follow a predictable pattern when changing tenses.', 'Present: go, Past: went, Past Participle: gone', 'Example: ''She went to the store.'''), " +
                        "(10, 4, 'Descriptive Adjectives', 'Descriptive adjectives describe the qualities of a noun.', 'Examples: beautiful, quick, tall', 'Example: ''She is a beautiful dancer.'''), " +
                        "(11, 4, 'Quantitative Adjectives', 'Quantitative adjectives describe the quantity of a noun.', 'Examples: some, many, few', 'Example: ''There are many apples.'''), " +
                        "(12, 5, 'Using the Subjunctive Mood', 'The subjunctive mood is used to express wishes, suggestions, or conditions contrary to fact.', 'If I were, I wish that, It is essential that', 'Example: ''If I were you, I would study harder.'''), " +
                        "(13, 6, 'Forming the Passive Voice', 'The passive voice is used to emphasize the action rather than who or what performed it.', 'be + past participle (e.g., is written, was eaten)', 'Example: ''The book was written by the author.'''), " +

                        // Tenses for Beginner
                        "(14, 5, 'Present Simple', 'The present simple tense is used to describe general truths, habits, and regular actions.', 'I eat, You eat, He/She/It eats', 'Example: ''I eat lunch at 12 PM.'''), " +
                        "(15, 5, 'Past Simple', 'The past simple tense is used to describe completed actions in the past.', 'I ate, You ate, He/She/It ate', 'Example: ''She went to the store.'''), " +
                        "(16, 5, 'Future Simple', 'The future simple tense is used to describe actions that will happen in the future.', 'I will eat, You will eat, He/She/It will eat', 'Example: ''They will travel to Paris next year.'''), " +

                        // Tenses for Independent
                        "(17, 6, 'Present Continuous', 'The present continuous tense is used to describe actions happening right now or around the present time.', 'I am eating, You are eating, He/She/It is eating', 'Example: ''She is studying for the exam.'''), " +
                        "(18, 6, 'Past Continuous', 'The past continuous tense is used to describe actions that were happening at a specific time in the past.', 'I was eating, You were eating, He/She/It was eating', 'Example: ''I was reading when she called.'''), " +
                        "(19, 6, 'Future Continuous', 'The future continuous tense is used to describe actions that will be happening at a specific time in the future.', 'I will be eating, You will be eating, He/She/It will be eating', 'Example: ''At 7 PM tomorrow, I will be studying.'''), " +

                        // Tenses for Proficient
                        "(20, 7, 'Present Perfect', 'The present perfect tense is used to describe actions that have happened at an unspecified time or actions that started in the past and continue to the present.', 'I have eaten, You have eaten, He/She/It has eaten', 'Example: ''I have seen that movie.'''), " +
                        "(21, 7, 'Past Perfect', 'The past perfect tense is used to describe an action that was completed before another action in the past.', 'I had eaten, You had eaten, He/She/It had eaten', 'Example: ''She had left before I arrived.'''), " +
                        "(22, 7, 'Future Perfect', 'The future perfect tense is used to describe an action that will be completed before another action or time in the future.', 'I will have eaten, You will have eaten, He/She/It will have eaten', 'Example: ''By next week, I will have finished my project.''')"
                )
            }
        } catch (e: Exception) {
            Log.e("DB_ERROR", "Error inserting sample data: ${e.message}")
        }
    }

    private fun isTableEmpty(db: SupportSQLiteDatabase, tableName: String): Boolean {
        db.query("SELECT COUNT(*) FROM $tableName").use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) == 0
            }
        }
        return false
    }
}


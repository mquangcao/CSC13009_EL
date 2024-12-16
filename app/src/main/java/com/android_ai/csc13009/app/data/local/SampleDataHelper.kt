package com.android_ai.csc13009.app.data.local

import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase

object SampleDataHelper {

    fun insertSampleData(db: SupportSQLiteDatabase) {
        try {
            // Check if levels table is empty
            if (isTableEmpty(db, "levels")) {
                // Insert sample
                //levels
                db.execSQL("INSERT INTO levels (id, name) VALUES (1, 'Beginner'), (2, 'Independent'), (3, 'Proficient')")

                //topics
                db.execSQL("INSERT INTO topics (id, levelId, name) VALUES (1, 1, 'Nouns'), (2, 1, 'Pronouns'), (3, 2, 'Verbs'), (4, 2, 'Adjectives'), (5, 3, 'Subjunctive Mood'), (6, 3, 'Passive Voice')")

                //subtopics
                db.execSQL("INSERT INTO subtopics (id, topicId, name, content, structures, examples) VALUES " +
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
                        "(13, 6, 'Forming the Passive Voice', 'The passive voice is used to emphasize the action rather than who or what performed it.', 'be + past participle (e.g., is written, was eaten)', 'Example: ''The book was written by the author.''')")
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

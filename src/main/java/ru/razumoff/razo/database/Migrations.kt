package ru.razumoff.razo.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Создаем таблицу users
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS users (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                age INTEGER,
                weight REAL,                height REAL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Создаем таблицу exercises
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS exercises (
                id TEXT PRIMARY KEY,
                userId TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                muscleGroups TEXT NOT NULL,
                bodyPart TEXT NOT NULL,
                movementType TEXT NOT NULL,
                equipment TEXT,
                notes TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                isCustom INTEGER NOT NULL
            )
        """)
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Создаем таблицу workout_templates
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS workout_templates (
                id TEXT PRIMARY KEY,
                userId TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)

        // Создаем таблицу template_exercises
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS template_exercises (
                id TEXT PRIMARY KEY,
                templateId TEXT NOT NULL,
                exerciseId TEXT NOT NULL,
                `order` INTEGER NOT NULL,
                defaultSets INTEGER NOT NULL,
                notes TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)

        // Создаем таблицу workout_sessions
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS workout_sessions (
                id TEXT PRIMARY KEY,
                userId TEXT NOT NULL,
                templateId TEXT NOT NULL,
                templateName TEXT NOT NULL,
                date INTEGER NOT NULL,
                duration INTEGER,
                notes TEXT,
                feeling INTEGER,
                isCompleted INTEGER NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)

        // Создаем таблицу session_exercises
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS session_exercises (
                id TEXT PRIMARY KEY,
                sessionId TEXT NOT NULL,
                templateExerciseId TEXT NOT NULL,
                exerciseId TEXT NOT NULL,
                exerciseName TEXT NOT NULL,
                `order` INTEGER NOT NULL,
                notes TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)

        // Создаем таблицу workout_sets
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS workout_sets (
                id TEXT PRIMARY KEY,
                sessionExerciseId TEXT NOT NULL,
                setNumber INTEGER NOT NULL,
                reps INTEGER NOT NULL,
                weight REAL NOT NULL,
                isWarmup INTEGER NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)
    }
}


val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN birthDate INTEGER")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            ALTER TABLE users DROP COLUMN age
        """)
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS user_measurements (
                id TEXT PRIMARY KEY NOT NULL,
                userId TEXT NOT NULL,
                value REAL NOT NULL,
                type TEXT NOT NULL,
                unit TEXT NOT NULL,
                dateTime INTEGER NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)
    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            ALTER TABLE workout_templates
            ADD COLUMN workoutType TEXT NOT NULL DEFAULT 'REGULAR'
            """.trimIndent()
        )

        db.execSQL(
            """
            ALTER TABLE workout_sessions
            ADD COLUMN workoutType TEXT NOT NULL DEFAULT 'REGULAR'
            """.trimIndent()
        )
    }
}
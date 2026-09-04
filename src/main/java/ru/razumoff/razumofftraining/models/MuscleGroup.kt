package ru.razumoff.razumofftraining.models

enum class MuscleGroup(val displayName: String) {
    CHEST("Грудь"),
    BACK("Спина"),
    SHOULDERS("Плечи"),
    BICEPS("Бицепс"),
    TRICEPS("Трицепс"),
    LEGS("Ноги"),
    GLUTES("Ягодицы"),
    CORE("Кор"),
    FOREARMS("Предплечья"),
    TRAPS("Трапеции"),
    LATS("Широчайшие"),
    CALVES("Икры"),
    HAMSTRINGS("Бицепс бедра"),
    QUADRICEPS("Квадрицепсы"),
    FULL_BODY("Все тело")
}

enum class BodyPart(val displayName: String) {
    UPPER("Верх"),
    LOWER("Низ"),
    FULL("Все тело")
}

enum class MovementType(val displayName: String) {
    PUSH("Толкать"),      // Жимовое движение
    PULL("Тянуть"),       // Тяговое движение
    SQUAT("Приседать"),   // Приседания
    LUNGE("Выпадать"),    // Выпады
    ROTATION("Вращать"),  // Вращение
    ISOLATION("Изолировать"), // Изолирующее
    COMPOUND("Базовое"),  // Базовое
    CARDIO("Кардио");     // Кардио
}
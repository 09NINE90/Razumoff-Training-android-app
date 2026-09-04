package ru.razumoff.razumofftraining.models

enum class MeasurementType(val displayName: String) {
    WEIGHT("Вес"),
    CHEST("Грудь"),
    WAIST("Талия"),
    HIPS("Бедра"),
    BICEPS("Бицепс"),
    FOREARM("Предплечье"),
    THIGH("Бедро"),
    CALF("Икра"),
    SHOULDERS("Плечи"),
    NECK("Шея");

    companion object {
        fun fromString(value: String): MeasurementType? {
            return entries.find { it.name == value }
        }
    }
}

enum class MeasurementUnit(val displayName: String) {
    KG("кг"),
    CM("см");

    companion object {
        fun fromString(value: String): MeasurementUnit? {
            return values().find { it.name == value }
        }
    }
}
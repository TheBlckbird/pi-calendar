package com.louisweigel.pi_calendar.utils

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.json.Json

// Source - https://stackoverflow.com/a/79773267
// Posted by BenjyTec
// Retrieved 2026-05-10, License - CC BY-SA 4.0

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write { putString(key, json.encodeToString(value)) }
    }

    override fun get(bundle: SavedState, key: String): T? {
        return json.decodeFromString<T?>(bundle.read { getString(key) })
    }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)
}

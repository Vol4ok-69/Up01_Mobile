package com.example.collegeschedule.data.local

import android.content.Context

class FavoritesManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        "favorites_prefs",
        Context.MODE_PRIVATE
    )

    fun getFavorites(): Set<String> {
        return prefs.getStringSet("favorite_groups", emptySet()) ?: emptySet()
    }

    fun addFavorite(groupName: String) {
        val updated = getFavorites().toMutableSet()
        updated.add(groupName)
        prefs.edit().putStringSet("favorite_groups", updated).apply()
    }

    fun removeFavorite(groupName: String) {
        val updated = getFavorites().toMutableSet()
        updated.remove(groupName)
        prefs.edit().putStringSet("favorite_groups", updated).apply()
    }

    fun isFavorite(groupName: String): Boolean {
        return getFavorites().contains(groupName)
    }
}

package com.reformistsinstitute.coredraft.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Editor : Screen("editor?noteId={noteId}") {
        const val ARG_NOTE_ID = "noteId"
        fun createRoute(noteId: Long?): String {
            return if (noteId != null && noteId > 0L) {
                "editor?noteId=$noteId"
            } else {
                "editor"
            }
        }
    }
}

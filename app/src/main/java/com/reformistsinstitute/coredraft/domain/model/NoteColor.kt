package com.reformistsinstitute.coredraft.domain.model

enum class NoteColor(
    val id: String,
    val displayName: String,
    val lightHex: Long,
    val darkHex: Long,
    val accentHex: Long
) {
    DEFAULT("default", "Classic", 0xFFFFFFFF, 0xFF1E2430, 0xFF6366F1),
    AZURE("azure", "Sky", 0xFFE0F2FE, 0xFF0C243B, 0xFF0284C7),
    MINT("mint", "Mint", 0xFFDCFCE7, 0xFF06331D, 0xFF16A34A),
    LAVENDER("lavender", "Lavender", 0xFFF3E8FF, 0xFF27153D, 0xFF9333EA),
    PEACH("peach", "Peach", 0xFFFFEDD5, 0xFF3D1D09, 0xFFEA580C),
    ROSE("rose", "Rose", 0xFFFFE4E6, 0xFF3B0B14, 0xFFE11D48),
    AMBER("amber", "Amber", 0xFFFEF3C7, 0xFF362104, 0xFFD97706),
    SLATE("slate", "Slate", 0xFFF1F5F9, 0xFF172033, 0xFF475569);

    companion object {
        fun fromId(id: String): NoteColor {
            return entries.find { it.id == id } ?: DEFAULT
        }
    }
}

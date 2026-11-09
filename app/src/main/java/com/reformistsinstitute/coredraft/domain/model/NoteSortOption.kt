package com.reformistsinstitute.coredraft.domain.model

enum class NoteSortOption(val title: String) {
    DATE_MODIFIED_DESC("Recently Edited"),
    DATE_MODIFIED_ASC("Oldest Edited"),
    DATE_CREATED_DESC("Newest Created"),
    TITLE_ASC("Title (A-Z)");
}

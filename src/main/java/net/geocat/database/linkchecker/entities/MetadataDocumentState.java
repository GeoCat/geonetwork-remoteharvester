package net.geocat.database.linkchecker.entities;

public enum MetadataDocumentState {
    IN_PROGRESS,
    NOT_APPLICABLE, //ie. not a service document
    LINKS_EXTRACTED,

    ERROR
}

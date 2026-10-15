package com.brakid.types;

import java.time.ZonedDateTime;

public record Todo(int id, String text, ZonedDateTime creationDateTime) {
    public String toGuiString() {
        return new StringBuilder().append(this.text).append(" - ").append(creationDateTime).toString();
    }
}

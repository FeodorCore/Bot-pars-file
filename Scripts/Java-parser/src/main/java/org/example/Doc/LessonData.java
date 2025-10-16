package org.example.Doc;

import java.util.Objects;

public class LessonData {
    private final String onSchedule;
    private final String changes;
    private final String auditorium;

    public LessonData(String onSchedule, String changes, String auditorium) {
        this.onSchedule = onSchedule != null ? onSchedule : "";
        this.changes = changes != null ? changes : "";
        this.auditorium = auditorium != null ? auditorium : "";
    }

    public String getOnSchedule() {
        return onSchedule;
    }

    public String getChanges() {
        return changes;
    }

    public String getAuditorium() {
        return auditorium;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonData that = (LessonData) o;
        return Objects.equals(onSchedule, that.onSchedule) &&
                Objects.equals(changes, that.changes) &&
                Objects.equals(auditorium, that.auditorium);
    }

    @Override
    public int hashCode() {
        return Objects.hash(onSchedule, changes, auditorium);
    }

    @Override
    public String toString() {
        return String.format("LessonData{onSchedule='%s', changes='%s', auditorium='%s'}",
                onSchedule, changes, auditorium);
    }
}
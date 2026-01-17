package net.koteczekui.appoapps.events;

import java.time.LocalDateTime;

public class SearchStateEvent {

    public enum Status {
        IDLE, SCANNING, SUCCESS, FAILED
    }

    private final Status status;
    private final String message;
    private final int itemsFound;
    private final LocalDateTime timestamp;

    public SearchStateEvent(Status status, String message, int itemsFound) {
        this.status = status;
        this.message = message;
        this.itemsFound = itemsFound;
        this.timestamp = LocalDateTime.now();
    }

    public static SearchStateEvent scanning(String folder) {
        return new SearchStateEvent(Status.SCANNING, "Searching in: " + folder, 0);
    }

    public static SearchStateEvent success(int count) {
        return new SearchStateEvent(Status.SUCCESS, "Search completed successfully.", count);
    }

    public Status getStatus() { return status; }
    public String getMessage() { return message; }
    public int getItemsFound() { return itemsFound; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] Status: %s - %s (Found: %d)",
                timestamp, status, message, itemsFound);
    }
}

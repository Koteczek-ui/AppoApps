package net.koteczekui.appoapps.events;

import net.koteczekui.appoapps.model.AppEntry;
import java.util.List;

public interface SearchEventListener {

    void onSearchStarted();

    void onResultsFound(List<AppEntry> results);

    void onSearchError(String errorMessage);

    void onSearchFinished(int totalCount);
}

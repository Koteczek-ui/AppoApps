package net.koteczekui.appoapps.core;

import net.koteczekui.appoapps.gui.*;
import net.koteczekui.appoapps.model.*;
import net.koteczekui.appoapps.events.*;
import net.koteczekui.appoapps.utils.*;
import java.util.List;

public class ApplicationCoordinator implements SearchEventListener {

    private final MainWindow mainWindow;
    private final AppConfig config;
    private final ThemeColors darkPalette;
    private final ThemeColors lightPalette;

    public ApplicationCoordinator(MainWindow window, AppConfig config) {
        this.mainWindow = window;
        this.config = config;

        this.darkPalette = ThemeColors.dark();
        this.lightPalette = ThemeColors.light();

        System.out.println("Starting " + AppConstants.APP_NAME + " v" + AppConstants.APP_VERSION);
    }

    public void updateSystemTheme() {
        ThemeColors selected = config.currentTheme.equals(AppConstants.THEME_DARK)
                ? darkPalette : lightPalette;

        ThemeManager.applyTheme(mainWindow.getContentPane(), config.currentTheme);
    }

    @Override
    public void onSearchStarted() {
        SearchStateEvent event = new SearchStateEvent(
                SearchStateEvent.Status.SCANNING, AppConstants.LABEL_SEARCH, 0);
        System.out.println(event.toString());
    }

    @Override
    public void onResultsFound(List<AppEntry> results) {
        if (!results.isEmpty()) {
            System.out.println("Found " + results.size() + " valid applications.");
        }
    }

    @Override
    public void onSearchError(String errorMessage) {
        SearchStateEvent errorEvent = new SearchStateEvent(
                SearchStateEvent.Status.FAILED, errorMessage, 0);
        System.err.println(errorEvent.getMessage());
    }

    @Override
    public void onSearchFinished(int totalCount) {
        List<String> verifiedFolders = PathValidator.getValidFolders(config.searchFolders);
        System.out.println("Scan finished across " + verifiedFolders.size() + " folders.");
    }

    public void safeLaunch(LauncherLogic launcher, String path) {
        try {
            launcher.launch(path);
        } catch (Exception e) {
            System.err.println(AppConstants.LABEL_LAUNCH + " FAILED: " + e.getMessage());
        }
    }
}

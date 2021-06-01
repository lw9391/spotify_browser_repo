package App.controller.strategies;

import App.controller.SpotifyBrowserApp;
import App.view.ViewController;

public class ExitApp implements AppStrategy {
    private final SpotifyBrowserApp app;
    private final ViewController view;

    public ExitApp(SpotifyBrowserApp app, ViewController view) {
        this.app = app;
        this.view = view;
    }

    @Override
    public void execute() {
        view.sendMessage("--Bye--");
        app.exitApp();
    }
}

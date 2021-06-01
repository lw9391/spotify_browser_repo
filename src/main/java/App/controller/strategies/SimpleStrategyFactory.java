package App.controller.strategies;

import App.controller.SpotifyBrowserApp;
import App.controller.requests.RequestType;
import App.controller.requests.GetConcretePlaylist;
import App.model.Manager;
import App.view.ViewController;

import java.util.HashMap;
import java.util.Map;

public class SimpleStrategyFactory implements StrategyFactory {
    private final SpotifyBrowserApp app;
    private final ViewController view;
    private final Map<RequestType, AppStrategy> requestStrategyMap;
    private Manager manager;

    public SimpleStrategyFactory(SpotifyBrowserApp app, Manager manager, ViewController controller) {
        this.app = app;
        this.manager = manager;
        this.view = controller;
        requestStrategyMap = new HashMap<>();
        initializeRequestMap();
    }

    private void initializeRequestMap() {
        requestStrategyMap.put(RequestType.NEW, new GetItemsFromAPI(view, () -> manager.accessNewReleases()));
        requestStrategyMap.put(RequestType.FEATURED, new GetItemsFromAPI(view, () -> manager.accessFeatured()));
        requestStrategyMap.put(RequestType.CATEGORIES, new GetItemsFromAPI(view, () -> manager.accessCategories()));
        requestStrategyMap.put(RequestType.PLAYLISTS, () -> view.sendMessage("Please provide category name with that command."));
        requestStrategyMap.put(RequestType.AUTH, new Authorize(manager, view, app));
        requestStrategyMap.put(RequestType.EXIT, new ExitApp(app, view));
        requestStrategyMap.put(RequestType.NEXT, new PrintNextPage(view));
        requestStrategyMap.put(RequestType.PREV, new PrintPrevPage(view));
        requestStrategyMap.put(RequestType.HELP, new PrintHelp(view));
    }

    @Override
    public AppStrategy newInstance(RequestType type) {
        return requestStrategyMap.get(type);
    }

    public AppStrategy newInstance(RequestType type, String additionalParams) {
        if (type.equals(RequestType.PLAYLISTS)) {
            return new GetItemsFromAPI(view, new GetConcretePlaylist(additionalParams, manager));
        }
        return () -> view.sendMessage("That operation doesn't support additional request params.");
    }

    public void setManager(Manager manager) {
        this.manager = manager;
        requestStrategyMap.clear();
        initializeRequestMap();
    }
}

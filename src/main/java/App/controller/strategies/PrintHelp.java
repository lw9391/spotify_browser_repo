package App.controller.strategies;

import App.view.ViewController;

public class PrintHelp implements AppStrategy {
    private final ViewController view;

    public PrintHelp(ViewController view) {
        this.view = view;
    }

    @Override
    public void execute() {
        String helpInfo = """
                List of currently supported commands:
                auth - authorize your spotify account for that session
                new - get a list of new album releases featured in Spotify
                featured - Get a list of Spotify featured playlists
                categories - Get a list of categories used to tag items in Spotify
                playlists "category_name" - Get a list of Spotify playlists tagged with a particular category. Requires "categories" command to be used first
                next - print next page of currently viewed objects(albums, playlists or categories)
                prev - print prev page of currently viewed objects(albums, playlists or categories)
                exit - exit app""";
        view.sendMessage(helpInfo);
    }
}

package App.controller;

import App.controller.requests.RequestType;
import App.controller.strategies.AppStrategy;
import App.controller.strategies.SimpleStrategyFactory;
import App.model.AuthorizedManagerDecorator;
import App.model.BasicManager;
import App.model.Manager;
import App.view.ViewController;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifyBrowserApp {
    private boolean isUsed;
    private final Scanner in;
    private Manager manager;
    private final SimpleStrategyFactory factory;
    private final ViewController view;
    private AppStrategy currentAction;

    public SpotifyBrowserApp() {
        this.view = new ViewController();
        this.manager = new BasicManager();
        this.factory = new SimpleStrategyFactory(this, manager, view);
    }

    public SpotifyBrowserApp(int page) {
        this.view = new ViewController(page);
        this.manager = new BasicManager();
        this.factory = new SimpleStrategyFactory(this, manager, view);
    }

    {
        this.in = new Scanner(System.in);
        this.isUsed = true;
    }

    public void run() {
        String welcomeMessage = "-- Welcome to Spotify Browser App --\n" +
                "Type \"help\" to check list of supported commands.";
        view.sendMessage(welcomeMessage);
        while (isUsed) {
            String commandName = in.nextLine();
            if (setStrategy(commandName)) {
                currentAction.execute();
            }
        }
    }

    private boolean setStrategy(String input) {
        String[] inputArray = input.split("[\\s]+");

        if (inputArray.length == 1) {
            try {
                currentAction = factory.newInstance(RequestType.valueOf(input.toUpperCase()));
                return true;
            } catch (IllegalArgumentException iae) {
                view.sendMessage("Wrong command.");
                return false;
            }
        } else {
            try {
                Pattern pattern = Pattern.compile("playlists" + "[\\s]+", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    int index = matcher.end();
                    String playlistName = input.substring(index);
                    currentAction = factory.newInstance(RequestType.valueOf(inputArray[0].toUpperCase()), playlistName);
                    return true;
                } else {
                    view.sendMessage("Wrong command.");
                    return false;
                }
            } catch (IllegalArgumentException | IllegalStateException iae) {
                view.sendMessage("Wrong command.");
                return false;
            }
        }
    }

    public void exitApp() {
        isUsed = false;
    }

    public void extendAccess() {
        this.manager = new AuthorizedManagerDecorator(manager);
        factory.setManager(manager);
    }
}

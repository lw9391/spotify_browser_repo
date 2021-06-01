package App.view;

import App.model.comm.spotify_objects.SpotifyObject;
import App.model.response.ModelResponse;
import App.model.response.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewController {
    private final Map<ResponseStatus, String> statusInfoMap;
    private final int numberOfEntries;
    private List<? extends SpotifyObject> currentlyViewedRequest;
    private int pages;
    private int currentPage;

    public ViewController() {
        this.numberOfEntries = 5;
    }

    public ViewController(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

    {
        this.statusInfoMap = new HashMap<>();
        statusInfoMap.put(ResponseStatus.AUTHORIZATION_ALREADY_GRANTED,"Authorization is already granted");
        statusInfoMap.put(ResponseStatus.AUTHORIZATION_CODE_NOT_FOUND,"Couldn't receive authorization code.");
        statusInfoMap.put(ResponseStatus.AUTHORIZATION_CODE_RECEIVED,"Code received.");
        statusInfoMap.put(ResponseStatus.AUTHORIZATION_GRANTED, "Success!");
        statusInfoMap.put(ResponseStatus.AUTHORIZATION_REQUIRED, "Please, provide access for application.");
        statusInfoMap.put(ResponseStatus.AUTHORIZATION_TOKENS_NOT_FOUND, "Couldn't receive authorization tokens.");
        statusInfoMap.put(ResponseStatus.CATEGORY_LIST_REQUIRED, "To access concrete playlist, first use \"categories\" command to receive list of categories.");
        statusInfoMap.put(ResponseStatus.CONNECTION_PROBLEM, "Problem during making http connections");
        statusInfoMap.put(ResponseStatus.OBJECTS_RECEIVED, "Requested objects received");
        statusInfoMap.put(ResponseStatus.RECEIVED_ERROR_API_RESPONSE, "Problem during requesting objects from api");
        statusInfoMap.put(ResponseStatus.UNKNOWN_CATEGORY_REQUESTED,"Unknown category name");
    }

    public void sendMessage(String message) {
        System.out.println(message);
    }

    public void printResponseStatusAndMessage(ModelResponse response) {
        printStatusInfo(response.getStatus());
        String additionalMessage = response.getMessage();
        if (!additionalMessage.isEmpty()) {
            System.out.println(additionalMessage);
        }
    }

    public void printStatusInfo(ResponseStatus status) {
        String info = statusInfoMap.get(status);
        System.out.println(Objects.requireNonNullElse(info, "other error"));
    }

    public void setCurrentlyViewedRequest(List<? extends SpotifyObject> currentlyViewedRequest) {
        this.currentlyViewedRequest = currentlyViewedRequest;
        this.pages = (int) Math.ceil((double)currentlyViewedRequest.size() / (double)numberOfEntries);
        this.currentPage = 0;
        printPage(currentPage);
    }

    public void printNext() {
        if (currentlyViewedRequest != null && currentPage < pages - 1) {
            currentPage++;
            printPage(currentPage);
        } else {
            System.out.println("No more pages.");
        }
    }

    public void printPrevious() {
        if(currentlyViewedRequest != null && currentPage > 0) {
            currentPage--;
            printPage(currentPage);
        } else {
            System.out.println("No more pages.");
        }
    }

    private void printPage(int page) {
        int startIndex = numberOfEntries * page;
        int numOfPagesToDisplay;
        if (numberOfEntries + startIndex > currentlyViewedRequest.size()) {
            numOfPagesToDisplay = currentlyViewedRequest.size();
        } else {
            numOfPagesToDisplay = numberOfEntries + startIndex;
        }
        for (int i = startIndex; i < numOfPagesToDisplay; i++) {
            System.out.println(currentlyViewedRequest.get(i));
        }
        System.out.printf("---PAGE %d OF %d---\n", page + 1, pages);
    }
}

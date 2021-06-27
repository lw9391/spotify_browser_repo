# SpotifyApiBrowser

Simple application that allows browsing through Spotify API from command line.
Created with plain java and Gson library. As a training project, its main goal 
was to learn basics of web technologies - Http protocol, REST API, OAuth 2.0 etc.

## Technologies
* Java 15
* Gradle 7.0.2
* Gson 2.8.6

## Launch
There are a few steps required to make everything up and run:
1. Create your Spotify account if you don't have one.
2. Register new application. 
   * Go through steps described in:
   https://developer.spotify.com/documentation/general/guides/app-settings/
   * For redirect URI set http://localhost:8080/
3. Navigate to project folder and build it with gradle wrapper using
   `./gradlew build` command
4. To run application you need your spotify client id and client secret, which you received
   after registration in point 2. Create folder app_data and insert 3 text files into it:
   * client_id.txt containing your id
   * client_secret.txt containing your app secret
   * host.txt with http://localhost:8080/ (the same redirect URI which you registered in point 2)
5. Go to build/distribution folder, take SpotifyApiBrowser-1.0-SNAPSHOT.zip and unzip it somewhere
   in your file system. Insert app_data folder with prepare files into bin folder. To run application, type 'SpotifyApiBrowser' 
   in console
6. Use 'help' command to see list of supported commands.

## Scope of functionality
As mentioned in description, application provide access to Spotify Api, however list of functionality is quite short and includes:
* Getting all new releases
* Getting all featured playlists
* Getting all categories
* Getting concrete category playlists

Before using any of browsing command authentication with spotify is required.

## Project status
Development suspended.

## Sources
Project topic and guidance thanks to https://hyperskill.org

Spotify authorization:
https://developer.spotify.com/documentation/general/guides/authorization-guide/

Spotify API reference:
https://developer.spotify.com/documentation/web-api/reference/#category-browse

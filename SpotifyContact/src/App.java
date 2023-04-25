import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse.*;
import java.util.Base64;
import java.util.Scanner;
import com.google.gson.Gson;


public class App {
    public static void main(String[] args) throws Exception {
        Gson gson = new Gson();
        String auth = "Authorization";
        String details = "";//replace with API key
        String encoded = Base64.getEncoder().encodeToString(details.getBytes());
        System.out.println(encoded);

        String ids = "Basic "+ encoded;
        HttpClient client = HttpClient.newHttpClient();


       HttpRequest postRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://accounts.spotify.com/api/token"))
        .headers(auth, ids, "Content-Type", "application/x-www-form-urlencoded")
        .POST(BodyPublishers.ofString("grant_type=client_credentials"))
        .build();
        
        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        
        System.out.println(postResponse.body());
        System.out.println(postResponse.statusCode());
        BearerAccess myAccess = gson.fromJson(postResponse.body(), BearerAccess.class);
        String token = "Bearer " + myAccess.getAccess_Token();
        System.out.println(token);



        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please enter the user ID:");
        String userId = keyboard.nextLine();
        keyboard.close();
        Playlists userPlaylists = gson.fromJson(getPlaylists(userId, token, client), Playlists.class);

        for (Playlist p : userPlaylists.getItems()){
            System.out.println( p.getName() + ":     " + p.getId());
        }
        System.out.println(userPlaylists.getTotal());
        
        

    }

    public static HttpResponse<String> getTrack(String link, String token, HttpClient client) throws IOException, InterruptedException{
        HttpRequest getTrackRequest = HttpRequest.newBuilder()
        .uri(URI.create(link)).headers("Authorization", token).GET().build();

        HttpResponse<String> getTrackResponse = client.send(getTrackRequest, BodyHandlers.ofString());
        System.out.println(getTrackResponse.statusCode());
        System.out.println(getTrackResponse.body());
        return getTrackResponse;
    }
    public static String getPlaylists(String userId, String token, HttpClient client) throws Exception{
        String playlistLink = "https://api.spotify.com/v1/users/"+userId+"/playlists";
        HttpRequest getPlaylistsRequest = HttpRequest.newBuilder()
        .uri(URI.create(playlistLink))
        .headers("Authorization", token).GET().build();
        HttpResponse<String> getPlaylistsResponse = client.send(getPlaylistsRequest, BodyHandlers.ofString());
        return getPlaylistsResponse.body();
    }
}

package Organiza;

import com.google.gson.JsonObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Store everything as string because no need for calculations. Only use is to
 * display at the end. Variables will be accessed directly.
 *
 * @author Daniel
 */
public class Movie
{
    protected JsonObject movieData;
    protected String posterURL;
    protected String playFile;

    protected String Title;
    protected String Year;
    protected String Rated;
    protected String Released;
    protected String Runtime;
    protected String Genre;
    protected String Director;
    protected String Writer;
    protected String Actors;
    protected String Plot;
    protected String Language;
    protected String Country;
    protected String Awards;
    protected String Poster;
    protected String Metascore;
    protected String imdbRating;
    protected String imdbVotes;
    protected String imdbID;
    protected String Type;
    protected String tomatoMeter;
    protected String tomatoImage;
    protected String tomatoRating;
    protected String tomatoReviews;
    protected String tomatoFresh;
    protected String tomatoRotten;
    protected String tomatoConsensus;
    protected String tomatoUserMeter;
    protected String tomatoUserRating;
    protected String tomatoUserReviews;
    protected String tomatoURL;
    protected String DVD;
    protected String BoxOffice;
    protected String Production;
    protected String Website;
    protected String Response;

    public Movie(JsonObject obj)
    {
        movieData = obj;
        setMovie(movieData);
    }

    public void setPosterUrl(String url)
    {
        posterURL = url;
    }

    public int getLength()
    {
        Pattern p = Pattern.compile("(^|\\s)([0-9]+)($|\\s)");
        Matcher m = p.matcher(Runtime);
        if (m.find())
        {
            return Integer.valueOf(m.group(2));
        }
        return 0;
    }
    
    public int getYear()
    {
        return Integer.valueOf(Year);
    }

    private void setMovie(JsonObject obj)
    {
        if (obj != null)
        {
            Title = obj.get("Title").getAsString();
            Year = obj.get("Year").getAsString();
            Rated = obj.get("Rated").getAsString();
            Released = obj.get("Released").getAsString();
            Runtime = obj.get("Runtime").getAsString();
            Genre = obj.get("Genre").getAsString();
            Director = obj.get("Director").getAsString();
            Writer = obj.get("Writer").getAsString();
            Actors = obj.get("Actors").getAsString();
            Plot = obj.get("Plot").getAsString();
            Language = obj.get("Language").getAsString();
            Country = obj.get("Country").getAsString();
            Awards = obj.get("Awards").getAsString();
            Poster = obj.get("Poster").getAsString();
            Metascore = obj.get("Metascore").getAsString();
            imdbRating = obj.get("imdbRating").getAsString();
            imdbVotes = obj.get("imdbVotes").getAsString();
            imdbID = obj.get("imdbID").getAsString();
            Type = obj.get("Type").getAsString();
            tomatoMeter = obj.get("tomatoMeter").getAsString();
            tomatoImage = obj.get("tomatoImage").getAsString();
            tomatoRating = obj.get("tomatoRating").getAsString();
            tomatoReviews = obj.get("tomatoReviews").getAsString();
            tomatoFresh = obj.get("tomatoFresh").getAsString();
            tomatoRotten = obj.get("tomatoRotten").getAsString();
            tomatoConsensus = obj.get("tomatoConsensus").getAsString();
            tomatoUserMeter = obj.get("tomatoUserMeter").getAsString();
            tomatoUserRating = obj.get("tomatoUserRating").getAsString();
            tomatoUserReviews = obj.get("tomatoUserReviews").getAsString();
            tomatoURL = obj.get("tomatoURL").getAsString();
            DVD = obj.get("DVD").getAsString();
            BoxOffice = obj.get("BoxOffice").getAsString();
            Production = obj.get("Production").getAsString();
            Website = obj.get("Website").getAsString();
            Response = obj.get("Response").getAsString();
        }
    }
}

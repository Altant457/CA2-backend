package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import dtos.*;
import entities.Anime;
import entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import facades.UserFacade;
import security.LoginEndpoint;
import utils.AnimeFetcher;
import utils.EMF_Creator;
import utils.FactFetcher;
import utils.PokemonFetcher;


/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
@DeclareRoles({"user", "admin"})
public class DemoResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    private static final UserFacade FACADE = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed({"user"})
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed({"admin"})
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("pokemon")
    @RolesAllowed({"user", "admin"})
    public String getPokeInfo(String pokemon) throws IOException, ExecutionException, InterruptedException {
        String query;
        PokemonDTO pokemonDTO;
        RandomFactDTO randomFactDTO;
        JsonObject json = JsonParser.parseString(pokemon).getAsJsonObject();
        query = json.get("query").getAsString();

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<PokemonDTO> futurePKMN = executor.submit(() -> PokemonFetcher.getData(query));
        Future<RandomFactDTO> futureRNDF = executor.submit(FactFetcher::getFact);
        pokemonDTO = futurePKMN.get();
        randomFactDTO = futureRNDF.get();
        return GSON.toJson(new ComboDTO(pokemonDTO, randomFactDTO));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("pokemondeck")
    public String getPokemonDeck(String deckSize) throws IOException, ExecutionException, InterruptedException {
        int size;
        PokemonDTO pokemonDTO;
        RandomFactDTO randomFactDTO;
        List<ComboDTO> comboDTOs = new ArrayList<>();

        JsonObject json = JsonParser.parseString(deckSize).getAsJsonObject();
        size = Integer.parseInt(json.get("deckSize").getAsString());

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<PokemonDTO>> futuresPKMN = new ArrayList<>();
        List<Future<RandomFactDTO>> futuresRNDF = new ArrayList<>();
        Future<PokemonDTO> futurePKMN;
        for (int i = 0; i <= size-1; i++) {
            String finalI = String.valueOf((int) (Math.random() * 904 + 1));
            futurePKMN = executor.submit(() -> PokemonFetcher.getData(finalI));
            futuresPKMN.add(futurePKMN);
            Future<RandomFactDTO> futureRNDF = executor.submit(FactFetcher::getFact);
            futuresRNDF.add(futureRNDF);
        }

        for (int i = 0; i <= size-1; i++) {
            pokemonDTO = futuresPKMN.get(i).get();
            randomFactDTO = futuresRNDF.get(i).get();
            comboDTOs.add(new ComboDTO(pokemonDTO, randomFactDTO));
        }
        return GSON.toJson(comboDTOs);
    }

    @POST
    @Path("signup")
    @Consumes("application/json")
    @Produces("application/json")
    public String createUser(String userJSON) throws JOSEException {
        JsonObject json = JsonParser.parseString(userJSON).getAsJsonObject();
        String username = json.get("userName").getAsString().toLowerCase();
        String password = json.get("userPass").getAsString();
        User user = new User(username, password);
        UserDTO createdUser = new UserDTO(FACADE.createUser(user));

        return GSON.toJson(createdUser);
    }

    @POST
    @Path("anime/multi")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"user", "admin"})
    public String getMultiAnime(String input) throws IOException, JOSEException {
        JsonObject json = JsonParser.parseString(input).getAsJsonObject();
        String query = json.get("query").getAsString();
        String username = json.get("username").getAsString();
        List<AnimeDTO> animeList = AnimeFetcher.getMultiData(query);
        return GSON.toJson(new AnimeDTOs(animeList, FACADE.updateToken(username)));
    }

    @POST
    @Path("user/watchlist/add")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"user", "admin"})
    public String addToWatchlist(String input) {
        JsonObject json = JsonParser.parseString(input).getAsJsonObject();
        String username = json.get("username").getAsString().toLowerCase();
        Anime anime = GSON.fromJson(json.get("anime").getAsJsonObject(), Anime.class);
        return GSON.toJson(FACADE.addAnimeToWatchList(username, anime));
    }

    @POST
    @Path("user/watchlist/remove")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"user", "admin"})
    public String removeFromWatchlist(String input) throws JOSEException {
        JsonObject json = JsonParser.parseString(input).getAsJsonObject();
        String username = json.get("username").getAsString().toLowerCase();
        Integer animeId = json.get("animeId").getAsInt();
        return GSON.toJson(FACADE.removeAnimeFromWatchList(username, animeId));
    }

    @POST
    @Path("user/watchlist")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"user", "admin"})
    public String getWatchlist(String input) throws JOSEException {
        JsonObject json = JsonParser.parseString(input).getAsJsonObject();
        String username = json.get("username").getAsString().toLowerCase();
        return GSON.toJson(FACADE.getWatchlist(username));
    }

}
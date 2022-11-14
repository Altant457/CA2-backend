package facades;

import com.nimbusds.jose.JOSEException;
import dtos.WatchlistTokenDTO;
import entities.Anime;
import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

import entities.Watchlist;
import security.LoginEndpoint;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User createUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Role userRole = new Role("user");
            Watchlist watchlist = new Watchlist();
            user.setWatchlist(watchlist);
            user.addRole(userRole);
            em.persist(watchlist);
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }

    public WatchlistTokenDTO addAnimeToWatchList(String username, Anime anime) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, username);
        try {
            user.getWatchlist().addAnimeToList(anime);
            if (em.find(Anime.class, anime.getId()) == null) {
                em.persist(anime);
            } else {
                em.merge(anime);
            }
            em.merge(user);
            em.merge(user.getWatchlist());
            em.getTransaction().commit();
            return new WatchlistTokenDTO(user.getWatchlist(), LoginEndpoint.createToken(username, user.getRolesAsStrings()));
        } catch (NullPointerException | JOSEException e) {
            throw new WebApplicationException("Internal Server Error", 500);
        } finally {
            em.close();
        }
    }

    public WatchlistTokenDTO removeAnimeFromWatchList(String username, Integer animeID) throws JOSEException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, username);
            Anime anime = em.find(Anime.class, animeID);
            if (anime == null) throw new WebApplicationException("Watchlist item does not exist", 404);
            user.getWatchlist().removeAnimeFromList(anime);
            em.merge(user);
            em.merge(anime);
            em.merge(user.getWatchlist());
            em.getTransaction().commit();
            return new WatchlistTokenDTO(user.getWatchlist(), updateToken(username));
        } finally {
            em.close();
        }
    }

    public String updateToken(String username) throws JOSEException {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, username);
            return LoginEndpoint.createToken(username, user.getRolesAsStrings());
        } finally {
            em.close();
        }
    }

    public WatchlistTokenDTO getWatchlist(String username) throws JOSEException {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, username);
            return new WatchlistTokenDTO(user.getWatchlist(), updateToken(username));
        } finally {
            em.close();
        }
    }
}

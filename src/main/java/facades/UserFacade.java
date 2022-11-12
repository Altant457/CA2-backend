package facades;

import entities.Anime;
import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

import entities.Watchlist;
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
        em.getTransaction().begin();
        Role userRole = new Role("user");
        Watchlist watchlist = new Watchlist();
        user.setWatchlist(watchlist);
        user.addRole(userRole);
        em.persist(watchlist);
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

    public User addAnimeToWatchList(String username, Anime anime) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, username);
            user.getWatchlist().addAnimeToList(anime);
            if (em.find(Anime.class, anime.getId()) == null) {
                em.persist(anime);
            } else {
                em.merge(anime);
            }
            em.merge(user);
            em.merge(user.getWatchlist());
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public User removeAnimeFromWatchList(String username, Integer animeID) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, username);
        Anime anime = em.find(Anime.class, animeID);
        if (anime == null) throw new WebApplicationException("Watchlist item does not exist", 404);
        user.getWatchlist().removeAnimeFromList(anime);
        em.merge(user);
        em.merge(anime);
        em.merge(user.getWatchlist());
        em.getTransaction().commit();
        return user;
    }
}

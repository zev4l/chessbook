package persist;

import domain.ChessPlayer;
import domain.Main;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class ChessPlayerDM extends ADataMapper<ChessPlayer> {
    private EntityManager em = null;
    private static ChessPlayerDM instance = null;

    public ChessPlayerDM() {
        super(ChessPlayer.class);
    }

    public static ChessPlayerDM getInstance() {
        if (instance == null)
            instance = new ChessPlayerDM();

        return instance;
    }

    public void deleteAllPlayers() {
        try {
            em = Main.emf.createEntityManager();
            em.getTransaction().begin();
            Query q = em.createNamedQuery("deleteAllPlayers");
            q.executeUpdate();
            em.getTransaction().commit();
        } // TODO: Adicionar excepções

        finally {
            em.close();
        }
    }

    public List<ChessPlayer> chessPlayersList() {
        List<ChessPlayer> result = null;
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            TypedQuery<ChessPlayer> query = em.createNamedQuery("listChessPlayers", ChessPlayer.class);
            result = query.getResultList();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return result;
    }

    public List<ChessPlayer> findByName(String name) {
        List<ChessPlayer> result = null;
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            TypedQuery<ChessPlayer> query = em.createNamedQuery("findByName", ChessPlayer.class);
            query.setParameter(1, name);
            result = query.getResultList();
        } catch (NoResultException e) {
            result = null;
        } finally {
            em.close();
        }
        return result;
    }

    public Optional<ChessPlayer> findByEmail(String email) {
        Optional<ChessPlayer> result;
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            TypedQuery<ChessPlayer> query = em.createNamedQuery("findByEmail", ChessPlayer.class);
            query.setParameter(1, email);
            result = Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            result = null;
        } finally {
            em.close();
        }
        return result;
    }
}

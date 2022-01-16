package persist;

import domain.ChessGame;
import domain.ChessPlayer;
import domain.Main;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class ChessGameDM extends ADataMapper<ChessGame>{
    // Deverá ser criado ao instanciar ou em cada método?
    private EntityManager em = null;
    private static ChessGameDM instance = null;

    public ChessGameDM(){
        super(ChessGame.class);
    }

    public static ChessGameDM getInstance() {
        if (instance == null)
            instance = new ChessGameDM();

        return instance;
    }

    public void deleteAllGames() {
        try {
            em = Main.emf.createEntityManager();
            List<ChessGame> games = chessGamesList();
            for (ChessGame game : games) {
                em.getTransaction().begin();
                game = em.merge(game);
                em.remove(game);
                em.getTransaction().commit();
            }
        } // TODO: Adicionar excepções

        finally {
            em.close();
        }
    }

    public List<ChessGame> findByPlayerName(String name) {
        List<ChessGame> result = null;
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            TypedQuery<ChessGame> query = em.createNamedQuery("findByPlayerName", ChessGame.class);
            query.setParameter(1, name);
            result = query.getResultList();
            for(ChessGame chessGame: result){
                em.refresh(chessGame);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return result;
    }

    public List<ChessGame> findByPlayerEmail(String email) {
        List<ChessGame> result = null;
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            TypedQuery<ChessGame> query = em.createNamedQuery("findByPlayerEmail", ChessGame.class);
            query.setParameter(1, email);
            result = query.getResultList();
            for(ChessGame chessGame: result){
                em.refresh(chessGame);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return result;
    }

    public List<ChessGame> chessGamesList() {
        List<ChessGame> result = null;
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            TypedQuery<ChessGame> query = em.createNamedQuery("listChessGames", ChessGame.class);
            result = query.getResultList();
            for(ChessGame chessGame: result){
                em.refresh(chessGame);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }

        return result;
    }
}

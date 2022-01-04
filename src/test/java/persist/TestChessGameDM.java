package persist;

import domain.ChessGame;
import domain.ChessPlayer;
import domain.Main;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestChessGameDM {
    private static ChessPlayerDM cpdm;
    private static ChessGameDM cgdm;
    private static int testGameID;

    @BeforeAll
    static void beforeAll() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CSSDB");
        } catch (Exception e) {
            System.out.println("An error occured while creating the EntityManagerFactory");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        cgdm = ChessGameDM.getInstance();
        cgdm.deleteAllGames();
        cpdm = ChessPlayerDM.getInstance();
        cpdm.deleteAllPlayers();

        ChessPlayer flynn   = new ChessPlayer("Kevin Flynn", "flynn@encom.com");
        ChessPlayer clu     = new ChessPlayer("CLU", "clu@grid.com");

        cpdm.insert(flynn);
        cpdm.insert(clu);

        ChessGame testGame = new ChessGame(flynn, clu, new Date());

        testGameID = cgdm.insert(testGame);
    }

    @AfterAll
    static void afterAll() {
        cgdm.deleteAllGames();
        cpdm.deleteAllPlayers();
    }

    @Test
    @Order(1)
    @DisplayName("Find Game by ID Test")
    void testFind() {
        ChessGame testGame = cgdm.find(testGameID).get();
        assertEquals(testGameID, testGame.getId());
    }

    @Test
    @Order(2)
    @DisplayName("List Chess Games Test")
    void testListChessGames() {
        List<ChessGame> games = cgdm.chessGamesList();
        assertSame(games.size(), 1);
    }

    @Test
    @Order(3)
    @DisplayName("Remove Games Test")
    void testRemovePlayers() {
        cgdm.deleteAllGames();
        List<ChessGame> players = cgdm.chessGamesList();
        assertSame(players.size(), 0);
    }
}

package persist;

import org.junit.jupiter.api.*;
import persist.ChessGameDM;
import persist.ChessPlayerDM;

import domain.ChessPlayer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestChessPlayerDM {
    private static ChessPlayerDM cpdm;
    private static ChessGameDM cgdm;
    private static ChessPlayer testPlayer;
    private static int testPlayerID;


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

        testPlayerID = cpdm.insert(new ChessPlayer("Kevin Flynn", "flynn@encom.com"));
    }

    @AfterAll
    static void afterAll() {
        cgdm.deleteAllGames();
        cpdm.deleteAllPlayers();
    }

    @Test
    @Order(1)
    @DisplayName("Find Player by ID Test")
    void testFind() {
        testPlayer = cpdm.find(testPlayerID).get();
        assertEquals(testPlayerID, testPlayer.getId());
    }

    @Test
    @Order(2)
    @DisplayName("List Chess Players Test")
    void testListChessPlayers() {
        List<ChessPlayer> players = cpdm.chessPlayersList();
        assertSame(players.size(), 1);
    }

    @Test
    @Order(3)
    @DisplayName("Remove Players Test")
    void testRemovePlayers() {
        cgdm.deleteAllGames();
        cpdm.deleteAllPlayers();
        List<ChessPlayer> players = cpdm.chessPlayersList();
        assertSame(players.size(), 0);
    }
}

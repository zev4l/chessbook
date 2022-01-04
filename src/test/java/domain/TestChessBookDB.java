package domain;

import org.junit.jupiter.api.*;
import persist.ChessGameDM;
import persist.ChessPlayerDM;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestChessBookDB {
    private static ChessPlayerDM cpdm;
    private static ChessGameDM cgdm;

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
    }

    @AfterAll
    static void afterAll() {
        cgdm.deleteAllGames();
        cpdm.deleteAllPlayers();
    }

    @Test
    @DisplayName("Singleton Test")
    void testGetInstance() {
        ChessPlayerDM cpdm2 = ChessPlayerDM.getInstance();
        assertSame(cpdm, cpdm2);
    }

    @Test
    @Order(1)
    @DisplayName("Create Chess Players Test")
    void testCreateChessPlayers() {
        // Creating Players
        ChessPlayer flynn   = new ChessPlayer("Kevin Flynn", "flynn@encom.com");
        ChessPlayer clu     = new ChessPlayer("CLU", "clu@grid.com");

        cpdm.insert(flynn);
        cpdm.insert(clu);
    }

    @Test
    @Order(2)
    @DisplayName("Create Chess Game Test / Find players by name and email")
    void testCreateChessGame() {
        ChessPlayer flynn   = cpdm.findByName("Kevin Flynn").get(0);
        ChessPlayer clu     = cpdm.findByEmail("clu@grid.com").get();

        ChessGame testGame = new ChessGame(flynn, clu, new Date());

        cgdm.insert(testGame);
    }

    @Test
    @Order(3)
    @DisplayName("Move test (Italian Game)")
    void testMoves() {
        ChessGame testGame = cgdm.findByPlayerEmail("flynn@encom.com").get(0);
        ChessPlayer flynn = testGame.getWhite();
        ChessPlayer clu = testGame.getBlack();

        // Creating moves: Kevin Flynn and CLU play the Giuoco Piano Italian Game opening
        // e4
        ChessMove move1w = new ChessMove(flynn, new ChessPosition(1, 4), new ChessPosition(3,4));

        //e5
        ChessMove move1b = new ChessMove(clu, new ChessPosition(6, 4), new ChessPosition(4,4));

        //Nf3
        ChessMove move2w = new ChessMove(flynn, new ChessPosition(0, 6), new ChessPosition(2,5));

        //Nc6
        ChessMove move2b = new ChessMove(clu, new ChessPosition(7, 1), new ChessPosition(5,2));

        //Bc4
        ChessMove move3w = new ChessMove(flynn, new ChessPosition(0, 5), new ChessPosition(3,2));

        //Bc5
        ChessMove move3b = new ChessMove(clu, new ChessPosition(7, 5), new ChessPosition(4,2));

        //Nxe5
        ChessMove move4w = new ChessMove(flynn, new ChessPosition(2,5), new ChessPosition(4,4));

        //f6
        ChessMove move4b = new ChessMove(clu, new ChessPosition(6,5), new ChessPosition(5,5));

        // loop and list for efficiency and testing purposes only
        List<ChessMove> testMoves = new ArrayList<ChessMove>();
        testMoves.add(move1w);
        testMoves.add(move1b);
        testMoves.add(move2w);
        testMoves.add(move2b);
        testMoves.add(move3w);
        testMoves.add(move3b);
        testMoves.add(move4w);
        testMoves.add(move4b);


        for (ChessMove move : testMoves) {

                // Adding move to game
            try {
                testGame.addMove(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
            }

            // Persisting on every move
                cgdm.update(testGame);

        }

        assertSame(testGame.getMoves().size(), 8);
    }

    @Test
    @Order(4)
    @DisplayName("Check Test")
    void testCheck() {
        ChessGame testGame = cgdm.findByPlayerEmail("flynn@encom.com").get(0);

        //Qh5
        ChessMove move5w = new ChessMove(testGame.getWhite(), new ChessPosition(0,3), new ChessPosition(4,7));

        try {
            testGame.addMove(move5w);
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }

        cgdm.update(testGame);

        System.out.println(testGame.getBoard());

        assertSame(Validation.isCheck(testGame.getBoard(), Color.BLACK), true);
    }

    @Test
    @Order(5)
    @DisplayName("Illegal Move Test")
    void testIllegalMove() {
        ChessGame testGame = cgdm.findByPlayerName("CLU").get(0);

        //D6 - Jogada inválida, pois é necessário sair do cheque
        ChessMove move5b = new ChessMove(testGame.getBlack(), new ChessPosition(6,3), new ChessPosition(5,3));

        Throwable exception = assertThrows(IllegalMoveException.class, () -> testGame.addMove(move5b));
    }

}

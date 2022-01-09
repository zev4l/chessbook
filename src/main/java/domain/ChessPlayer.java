package domain;

import javax.persistence.*;
import static javax.persistence.GenerationType.AUTO;

@Entity
@NamedQueries({
        @NamedQuery(name="listChessPlayers",
                query="SELECT cp FROM ChessPlayer cp"),
        @NamedQuery(name="findByName",
                query="SELECT cp FROM ChessPlayer cp WHERE cp.name = ?1"),
        @NamedQuery(name="findByEmail",
                query="SELECT cp FROM ChessPlayer cp WHERE cp.email = ?1"),
        @NamedQuery(name="deleteAllPlayers",
                query="DELETE FROM ChessPlayer")
})
public class ChessPlayer {
    @Id @GeneratedValue(strategy=AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(unique=true)
    private String email;

    public ChessPlayer() {}

    public ChessPlayer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // TODO: Implement methods

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean equals(ChessPlayer other) {
        return this.getId() == other.getId() && this.getName() == other.getName() && this.getEmail() == other.getEmail();
    }

    @Override
    public String toString() {
        return "[" + getId() + "] " + getName() + ", " + getEmail();
    }
}

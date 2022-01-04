package domain;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("CSSDB");

    public static void main(String[] args) throws IOException {

    }
}

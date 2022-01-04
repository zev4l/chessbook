package persist;

import domain.Main;
import javax.persistence.EntityManager;
import java.util.Optional;

abstract class ADataMapper<E> implements IDataMapper<E>{
    private Class<E> c;

    public ADataMapper(Class<E> c) {
        this.c = c;
    }

    public int insert(E e) {
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(e);
            em.getTransaction().commit();
            return (int) Main.emf.getPersistenceUnitUtil().getIdentifier(e);
        }
        // TODO: Falta excepcionar
        finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void update(E e) {
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            em.getTransaction().begin();
            if(!em.contains(e)) {
                em.merge(e);
            }
            em.getTransaction().commit();
        }
        // TODO: Falta excepcionar
        finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Optional<E> find(int id) {
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            return Optional.of(em.find(c, id));
        }
            //TODO: lidar com excepções
        finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void remove(E e) {
        EntityManager em = null;
        try {
            em = Main.emf.createEntityManager();
            em.getTransaction().begin();
            em.remove(e);
            em.getTransaction().commit();
        }
        // TODO: Falta excepcionar
        finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

package persist;

import java.util.Optional;

public interface IDataMapper<E> {

    // TODO: Retificar return types para métodos que eventualmente necessitem de retornar int ou boolean
    public Optional<E> find(int id);
    public void update(E e);
    public void remove(E e);
    public int insert (E e);
}


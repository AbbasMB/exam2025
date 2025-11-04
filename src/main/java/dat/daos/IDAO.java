package dat.daos;

import java.util.List;

public interface IDAO<T, I> {
    T create(T entity);
    T update(T entity);
    void delete(I id);
    T getById(I id);
    List<T> getAll();
}

package carsharing.db;

import java.util.List;

public interface Dao<T> {
    List<T> findAll();
    T findById(int id);
    void add(T t);
    void update(T t);
    void deleteById(int id);
}

package com.project.database_2b2t_org_ru.service.interfaces;

import java.util.List;

public interface MainService<T> {
    public List<T> getAllObjects();

    public void saveObject(T object);

    public T getObjectById(long id);

    public void deleteObjectById(long id);
}

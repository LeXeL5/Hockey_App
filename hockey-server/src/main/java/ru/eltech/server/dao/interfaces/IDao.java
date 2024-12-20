package ru.eltech.server.dao.interfaces;

import org.springframework.jdbc.core.RowMapper;
import ru.eltech.api.entity.IEntity;

/**
 * Интерфейс всех DAO-классов.
 *
 * @param <E> тип entity конкретного dao-класса
 */
public interface IDao<E extends IEntity> {
    RowMapper<E> rowMapper();
}

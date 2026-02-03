package io.github.dudupuci.appdespesas.models.entities.base;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;

public interface BaseRepository<T> {
    void salvar(T entidade);
    T buscarPorId(Object id);
    void deletar(T entidade);

    default void throwEntityNotFound(String nomeEntidade, Object id) {
        throw new EntityNotFoundException(
            AppDespesasMessages.getMessage("entity.not.found", new Object[]{nomeEntidade, id})
        );
    }
}

package io.github.dudupuci.appdespesas.models.entities.base;

import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;

public interface BaseRepository<T> {
    void salvar(T entidade);
    T buscarPorId(Long id);
    void deletar(T entidade);

    default void throwEntityNotFound(String nomeEntidade, Long id) {
        throw new EntityNotFoundException(
            AppDespesasMessages.getMessage("entity.not.found", new Object[]{nomeEntidade, id})
        );
    }
}

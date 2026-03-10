package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.application.usecases.base.UseCase;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;

import java.util.UUID;

public abstract class BuscarAssinaturaUsuarioUseCase extends UseCase<UUID, Assinatura> {}


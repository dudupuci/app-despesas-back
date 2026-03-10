package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.application.usecases.base.UseCase;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;

import java.util.List;
import java.util.UUID;

public abstract class BuscarOutrasAssinaturasUsuarioUseCase extends UseCase<UUID, List<Assinatura>> {}


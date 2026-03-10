package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import io.github.dudupuci.appdespesas.application.usecases.base.UseCase;

public abstract class AssinarPlanoUseCase extends UseCase<AssinarAssinaturaCommand, ObterQrCodePixResponseDto> {}


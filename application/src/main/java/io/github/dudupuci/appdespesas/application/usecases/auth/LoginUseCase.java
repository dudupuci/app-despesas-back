package io.github.dudupuci.appdespesas.application.usecases.auth;

import io.github.dudupuci.appdespesas.application.responses.auth.AuthResult;
import io.github.dudupuci.appdespesas.application.usecases.base.UseCase;

public abstract class LoginUseCase extends UseCase<LoginCommand, AuthResult> {}

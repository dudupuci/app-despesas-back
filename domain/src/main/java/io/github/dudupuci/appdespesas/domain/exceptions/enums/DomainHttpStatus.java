package io.github.dudupuci.appdespesas.domain.exceptions.enums;

import lombok.Getter;

/**
 * Enum que representa códigos HTTP no domínio, sem depender do Spring.
 * Permite que as exceptions de domínio carreguem o status HTTP sem acoplar ao framework.
 */
@Getter
public enum DomainHttpStatus {

    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409),
    UNPROCESSABLE_ENTITY(422),
    INTERNAL_SERVER_ERROR(500);

    private final int value;

    DomainHttpStatus(int value) {
        this.value = value;
    }

}


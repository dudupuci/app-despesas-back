package io.github.dudupuci.appdespesas.services.webservices.dtos.request;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.apache.commons.lang3.StringUtils;

import static io.github.dudupuci.appdespesas.utils.AppDespesasUtils.validaString;

public record CriarCustomerAsaasRequestDto(
        String name,
        String cpfCnpj,
        String email,
        String phone,
        String mobilePhone,
        String address,
        String addressNumber,
        String complement,
        String province,
        String postalCode,
        String externalReference,
        Boolean notificationDisabled,
        String observations,
        String groupName,
        String company,
        Boolean foreignCustomer
) {
    public static CriarCustomerAsaasRequestDto fromUsuarioSistema(UsuarioSistema usuarioSistema) {
        boolean isContatoNotNull = AppDespesasUtils.isEntidadeNotNull(usuarioSistema.getContato());
        boolean isEnderecoNotNull = AppDespesasUtils.isEntidadeNotNull(usuarioSistema.getEndereco());

        if (StringUtils.isEmpty(usuarioSistema.getNomeCompleto())) {
            throw new IllegalArgumentException("O nome completo do usuário é obrigatório para criar um cliente no Asaas.");
        }

        if (StringUtils.isEmpty(usuarioSistema.getCpfCnpj())) {
            throw new IllegalArgumentException("O CPF/CNPJ do usuário é obrigatório para criar um cliente no Asaas.");
        }

        return new CriarCustomerAsaasRequestDto(
                usuarioSistema.getNomeCompleto(),
                usuarioSistema.getCpfCnpj(),
                validaString(isContatoNotNull ? usuarioSistema.getContato().getEmail() : null),
                validaString(isContatoNotNull ? usuarioSistema.getContato().getTelefoneFixo() : null),
                validaString(isContatoNotNull ? usuarioSistema.getContato().getCelular() : null),
                validaString(isEnderecoNotNull ? usuarioSistema.getEndereco().getLogradouro() : null),
                validaString(isEnderecoNotNull ? usuarioSistema.getEndereco().getNumero() : null),
                validaString(isEnderecoNotNull ? usuarioSistema.getEndereco().getComplemento() : null),
                validaString(isEnderecoNotNull ? usuarioSistema.getEndereco().getBairro() : null),
                validaString(usuarioSistema.getEndereco().getCep()),
                String.valueOf(usuarioSistema.getId()),
                false,
                null,
                null,
                null,
                false
        );
    }
}

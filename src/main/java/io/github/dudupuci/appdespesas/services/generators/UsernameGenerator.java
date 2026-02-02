package io.github.dudupuci.appdespesas.services.generators;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.UsuariosService;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {

    private final UsuariosService usuariosService;

    public UsernameGenerator(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    public String gerarUsernameParaUsuarioSistema(UsuarioSistema usuarioSistema) {
        String nomeCompleto = usuarioSistema.getNomeCompleto();
        String nomeUsuarioBase = gerarUsernameBase(nomeCompleto);

        // Se não existe, retorna o username gerado
        if (!this.usuariosService.existePorNomeUsuario(nomeUsuarioBase)) {
            return nomeUsuarioBase;
        }

        // Se existe, adiciona sufixo numérico até encontrar um disponível
        int sufixo = 1;
        String novoUsername;
        do {
            novoUsername = nomeUsuarioBase + sufixo;
            sufixo++;
        } while (this.usuariosService.existePorNomeUsuario(novoUsername));

        return novoUsername;
    }

    private String gerarUsernameBase(String nomeCompleto) {
        // Remove espaços extras, normaliza e converte para minúsculas
        String nome = getNomeCompletoValidado(nomeCompleto);

        // Divide o nome em partes
        String[] partes = nome.split("\\s+");

        // Se tiver apenas um nome, retorna ele
        if (partes.length == 1) {
            return partes[0];
        }

        // Retorna primeiro nome + último sobrenome no formato nome.sobrenome
        String primeiroNome = partes[0];
        String ultimoSobrenome = partes[partes.length - 1];

        return primeiroNome + "." + ultimoSobrenome;
    }

    private static String getNomeCompletoValidado(String nomeCompleto) {
        String nome = nomeCompleto.trim().toLowerCase();

        // Remove acentos
        nome = nome.replaceAll("[áàâãäå]", "a")
                   .replaceAll("[éèêë]", "e")
                   .replaceAll("[íìîï]", "i")
                   .replaceAll("[óòôõö]", "o")
                   .replaceAll("[úùûü]", "u")
                   .replaceAll("[ç]", "c")
                   .replaceAll("[ñ]", "n");

        // Remove caracteres especiais, mantendo apenas letras e espaços
        nome = nome.replaceAll("[^a-z\\s]", "");

        // Remove espaços múltiplos
        nome = nome.replaceAll("\\s+", " ").trim();
        return nome;
    }

}

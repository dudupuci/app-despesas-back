package io.github.dudupuci.appdespesas.controllers.users.dtos.responses.categoria;

import io.github.dudupuci.appdespesas.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CategoriaCriadaResponseDto extends ResponseDto {
    private final UUID id;
    private final String nome;
    private final String descricao;
    private final String status;

   public CategoriaCriadaResponseDto(
           UUID id,
           String nome,
           String descricao,
           String status,
           String mensagemKey
   ) {
       super(AppDespesasMessages.getMessage(mensagemKey, null));
       this.id = id;
       this.nome = nome;
       this.descricao = descricao;
       this.status = status;
   }

   public static CategoriaCriadaResponseDto fromEntityCriada(Categoria categoria) {
       return fromEntity(categoria, "categoria.criada.sucesso");
   }

   private static CategoriaCriadaResponseDto fromEntity(Categoria categoria, String mensagemKey) {
         return new CategoriaCriadaResponseDto(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getStatus().name(),
                mensagemKey
         );
   }
}

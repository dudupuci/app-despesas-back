package io.github.dudupuci.appdespesas.controllers.dtos.response;

import io.github.dudupuci.appdespesas.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import lombok.Getter;

@Getter
public class CategoriaCriadaDto extends ResponseDto {
    private final String nome;
    private final String descricao;
    private final String status;

   public CategoriaCriadaDto(
           String nome,
           String descricao,
           String status,
           String mensagemKey
   ) {
       super(AppDespesasMessages.getMessage(mensagemKey, null));
       this.nome = nome;
       this.descricao = descricao;
       this.status = status;
   }

   public static CategoriaCriadaDto fromEntityCriada(Categoria categoria) {
       return fromEntity(categoria, "categoria.criada.sucesso");
   }

   private static CategoriaCriadaDto fromEntity(Categoria categoria, String mensagemKey) {
         return new CategoriaCriadaDto(
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getStatus().name(),
                mensagemKey
         );
   }
}

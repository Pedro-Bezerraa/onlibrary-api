package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Entity
@Immutable
@Table(name = "vw_table_reserva")
@Getter
@Setter
public class VwTableReserva {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Username\"")
    private String username;

    @Column(name = "\"Livro\"")
    private String livro;

    @Column(name = "\"Exemplares\"")
    private String exemplares;

    @Column(name = "\"Data de Emissão\"")
    private String dataEmissao;

    @Column(name = "\"Data de Retirada\"")
    private String dataRetirada;

    @Column(name = "\"Situação\"")
    private String situacao;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "\"Quantidade Total\"")
    private Integer quantidadeTotal;

    @Column(name = "\"Quantidade Pendente\"")
    private Integer quantidadePendente;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;

    @Column(name = "fk_id_usuario")
    private UUID fkIdUsuario;
}

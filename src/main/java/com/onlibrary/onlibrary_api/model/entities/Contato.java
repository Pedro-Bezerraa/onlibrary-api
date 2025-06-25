package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_contato")
@Getter
@Setter
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "data_emissao", nullable = false, updatable = false)
    private OffsetDateTime dataEmissao;

    @ManyToOne()
    @JoinColumn(name = "fk_id_usuario", nullable = false)
    private Usuario usuario;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(name = "marcado_lido")
    private Boolean marcadoLido = false;

    @Column(nullable = false)
    private Boolean deletado = false;

    @Column(nullable = false)
    private Boolean concluido = false;
}

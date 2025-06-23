package com.onlibrary.onlibrary_api.model.views;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "vw_emprestimo_tratamento")
@Getter
@Setter
public class VwEmprestimoTratamento {
    @Id
    @Column(name = "id_emprestimo")
    private UUID idEmprestimo;

    @Column(name = "id_biblioteca")
    private UUID idBiblioteca;

    @Column(name = "id_usuario")
    private UUID idUsuario;

    @Column(name = "situacao")
    @JsonProperty("Situação")
    private String situacao;

    @Column(name = "multar")
    @JsonProperty("Multar")
    private Boolean multar;

    @Column(name = "bloquear")
    @JsonProperty("Bloquear")
    private Boolean bloquear;
}

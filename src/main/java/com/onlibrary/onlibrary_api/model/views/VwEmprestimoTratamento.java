package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vw_emprestimo_tratamento")
@Getter
@Setter
public class VwEmprestimoTratamento {
    @Id
    @Column(name = "id_emprestimo")
    private UUID idEmprestimo;

    @Column(name = "id_usuario")
    private UUID idUsuario;

    @Column(name = "situacao")
    private String situacao;

    @Column(name = "multar")
    private Boolean multar;

    @Column(name = "bloquear")
    private Boolean bloquear;

}

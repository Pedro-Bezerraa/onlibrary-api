package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwEmprestimoTratamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VwEmprestimoTratamentoRepository extends JpaRepository<VwEmprestimoTratamento, UUID> {
}

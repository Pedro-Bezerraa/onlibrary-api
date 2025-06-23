package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTabelaPerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwTabelaPerfilUsuarioRepository extends JpaRepository<VwTabelaPerfilUsuario, UUID> {
    List<VwTabelaPerfilUsuario> findByFkIdBiblioteca(UUID fkIdBiblioteca);

    @Query(value = "SELECT * FROM vw_table_perfil_usuario p WHERE p.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(p.\"Nome\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaPerfilUsuario> searchByNomeInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_perfil_usuario p WHERE p.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(p.\"Valor da Multa\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaPerfilUsuario> searchByValorDaMultaInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_perfil_usuario p WHERE p.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(p.\"Prazo de devolução\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaPerfilUsuario> searchByPrazoDeDevolucaoInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_perfil_usuario p WHERE p.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(p.\"Prazo de Multa\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaPerfilUsuario> searchByPrazoDeMultaInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_perfil_usuario p WHERE p.fk_id_biblioteca = :bibliotecaId AND (" +
            "LOWER(p.\"Nome\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(p.\"Valor da Multa\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(p.\"Prazo de devolução\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(p.\"Prazo de Multa\") ILIKE LOWER(CONCAT('%', :value, '%')))", nativeQuery = true)
    List<VwTabelaPerfilUsuario> searchByAllInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);
}

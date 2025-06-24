package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTabelaUsuarioBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwTabelaUsuarioBibliotecaRepository extends JpaRepository<VwTabelaUsuarioBiblioteca, UUID> {
    List<VwTabelaUsuarioBiblioteca> findByFkIdBiblioteca(UUID fkIdBiblioteca);

    @Query(value = "SELECT * FROM vw_table_usuario_biblioteca v WHERE v.fk_id_biblioteca = :bibliotecaId AND (" +
            "LOWER(v.username) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.nome) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.email) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.cpf) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.perfil) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.situacao) ILIKE LOWER(CONCAT('%', :value, '%'))" +
            ")", nativeQuery = true)
    List<VwTabelaUsuarioBiblioteca> searchByAllInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_usuario_biblioteca v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.username) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaUsuarioBiblioteca> searchByUsernameInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_usuario_biblioteca v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.nome) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaUsuarioBiblioteca> searchByNomeInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_usuario_biblioteca v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.perfil) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaUsuarioBiblioteca> searchByPerfilInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_usuario_biblioteca v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.email) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaUsuarioBiblioteca> searchByEmailInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_usuario_biblioteca v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.cpf) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaUsuarioBiblioteca> searchByCpfInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_usuario_biblioteca v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.situacao) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTabelaUsuarioBiblioteca> searchBySituacaoInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);
}

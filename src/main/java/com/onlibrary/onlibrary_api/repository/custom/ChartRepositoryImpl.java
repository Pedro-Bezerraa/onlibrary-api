package com.onlibrary.onlibrary_api.repository.custom;

import com.onlibrary.onlibrary_api.dto.graficos.ChartLastDataDTO;
import com.onlibrary.onlibrary_api.dto.graficos.ChartWeekDataDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ChartRepositoryImpl implements ChartRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<ChartWeekDataDTO> getWeekData(String nomeTabela, UUID bibliotecaId) {
        String sql = "SELECT * FROM get_week_data(:nomeTabela, :bibliotecaId)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("nomeTabela", nomeTabela);
        query.setParameter("bibliotecaId", bibliotecaId);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(row -> new ChartWeekDataDTO(
                        ((Number) row[0]).longValue(),
                        (String) row[2],
                        ((Date) row[1]).toLocalDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ChartLastDataDTO> getLastData(String nomeTabela, UUID bibliotecaId) {
        String sql = "SELECT * FROM get_last_data(:nomeTabela, :bibliotecaId)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("nomeTabela", nomeTabela);
        query.setParameter("bibliotecaId", bibliotecaId);

        List<Object[]> results = query.getResultList();
        return results.stream()
                .map(row -> new ChartLastDataDTO(
                        (String) row[0],
                        (String) row[1]
                ))
                .collect(Collectors.toList());
    }
}
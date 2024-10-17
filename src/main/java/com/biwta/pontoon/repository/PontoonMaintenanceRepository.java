package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonMaintenance;
import com.biwta.pontoon.dto.MaitenanceListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author nasimkabir
 * ৩/১২/২৩
 */
public interface PontoonMaintenanceRepository extends JpaRepository<PontoonMaintenance,Long> {
    Page<PontoonMaintenance> findAllByIsActiveTrue(Pageable pageable);
    @Query("select pm from PontoonMaintenance pm where pm.pontoon.pontoonId = ?1 or pm.pontoon.id=?1 and pm.isActive = true")
    Page<PontoonMaintenance> findAllByPontoonIOrPontoonNamedAndIsActiveTrue(String searchKey,Pageable pageable);

    @Query(value = "\n" +
            "select\n" +
            "    p.id as id,\n" +
            "    p.pontoon_id as pontoonId\n" +
            "        from\n" +
            "    pontoon_status_update psu\n" +
            "join public.pontoon p on psu.pontoon_id = p.id\n" +
            "join (SELECT pontoon_id,\n" +
            "             MAX(last_modified_date) as max_date\n" +
            "      FROM pontoon_status_update\n" +
            "      GROUP BY pontoon_id) latest_status ON p.id = latest_status.pontoon_id\n" +
            "        JOIN\n" +
            "     pontoon_status pss ON psu.status_id = pss.id\n" +
            "WHERE psu.last_modified_date = latest_status.max_date\n" +
            " and pss.status_name='Need Repair'\n" +
            "order by psu.created_date desc",nativeQuery = true)
    List<MaitenanceListProjection> findAllMaintenanceList();
}

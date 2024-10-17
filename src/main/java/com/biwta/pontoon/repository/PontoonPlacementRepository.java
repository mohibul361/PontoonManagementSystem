package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonPlacement;
import com.biwta.pontoon.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
public interface PontoonPlacementRepository extends JpaRepository<PontoonPlacement, Long> {
    Optional<PontoonPlacement> findByIsActiveTrueAndId(Long id);

    Optional<PontoonPlacement> findByEmployee_Id(Long loshkorId);

    Page<PontoonPlacement> findAllByIsActiveTrue(Pageable pageable);

    @Query("SELECT p FROM PontoonPlacement p WHERE " +
            "(:keyword IS NULL OR LOWER(p.pontoon.pontoonId) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "AND p.isActive = true " +
            "ORDER BY p.id DESC")
    Page<PontoonPlacement> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<PontoonPlacement> findByIsActiveTrueAndPontoonId(Long pontoonPId);

    @Query(value = "select pp.pontoon_id      as id,\n" +
            "       p.pontoon_id       as pontoonId,\n" +
            "       pt.type_name       as pontoonType,\n" +
            "       g.ghat_name        as ghatName,\n" +
            "       s.section_name     as sectionName,\n" +
            "       r.route_name       as routeName,\n" +
            "       p.build_year       as buildYear,\n" +
            "       p.manufactured_by  as manufacturedBy,\n" +
            "       p.build_cost       as buildCost,\n" +
            "       p.procuring_entity as procuringEntity,\n" +
            "       p.budget_source    as budgetSource,\n" +
            "       p.remarks          as remarks,\n" +
            "       p.is_assigned      as isAssigned,\n" +
            "       pss.status_name    as status\n" +
            "from pontoon_placement pp\n" +
            "         join pontoon p on p.id = pp.pontoon_id\n" +
            "         join pontoon_size ps on p.pontoon_size_id = ps.id\n" +
            "         join pontoon_type pt on ps.pontoon_type_id = pt.id\n" +
            "         join ghat g on pp.ghat_id = g.id\n" +
            "         join route r on pp.route_id = r.id\n" +
            "         JOIN pontoon_section s ON g.section_id = s.id\n" +
            "         JOIN pontoon_status_update psu on pp.pontoon_id = psu.pontoon_id\n" +
            "         join (SELECT pontoon_id,\n" +
            "                      MAX(last_modified_date) as max_date\n" +
            "               FROM pontoon_status_update\n" +
            "               GROUP BY pontoon_id) latest_status ON pp.pontoon_id = latest_status.pontoon_id\n" +
            "         JOIN\n" +
            "     pontoon_status pss ON psu.status_id = pss.id\n" +
            "WHERE psu.last_modified_date = latest_status.max_date\n" +
            "order by pp.last_modified_date desc",nativeQuery = true)
    Page<PontoonPlacementProjection> findByIsActiveTrueAndPontoonIsAssignedTrueOrderByCreatedDate(Pageable pageable);
    @Query(value = "select pp.pontoon_id      as id,\n" +
            "       p.pontoon_id       as pontoonId,\n" +
            "       pt.type_name       as pontoonType,\n" +
            "       g.ghat_name        as ghatName,\n" +
            "       s.section_name     as sectionName,\n" +
            "       r.route_name       as routeName,\n" +
            "       p.build_year       as buildYear,\n" +
            "       p.manufactured_by  as manufacturedBy,\n" +
            "       p.build_cost       as buildCost,\n" +
            "       p.procuring_entity as procuringEntity,\n" +
            "       p.budget_source    as budgetSource,\n" +
            "       p.remarks          as remarks,\n" +
            "       p.is_assigned      as isActive,\n" +
            "       pss.status_name    as status\n" +
            "from pontoon_placement pp\n" +
            "         join pontoon p on p.id = pp.pontoon_id\n" +
            "         join pontoon_size ps on p.pontoon_size_id = ps.id\n" +
            "         join pontoon_type pt on ps.pontoon_type_id = pt.id\n" +
            "         join ghat g on pp.ghat_id = g.id\n" +
            "         join route r on pp.route_id = r.id\n" +
            "         JOIN pontoon_section s ON g.section_id = s.id\n" +
            "         JOIN pontoon_status_update psu on pp.pontoon_id = psu.pontoon_id\n" +
            "         join (SELECT pontoon_id,\n" +
            "                      MAX(last_modified_date) as max_date\n" +
            "               FROM pontoon_status_update\n" +
            "               GROUP BY pontoon_id) latest_status ON pp.pontoon_id = latest_status.pontoon_id\n" +
            "         JOIN\n" +
            "     pontoon_status pss ON psu.status_id = pss.id\n" +
            "WHERE psu.last_modified_date = latest_status.max_date\n" +
            "  AND (\n" +
            "    LOWER(pss.status_name) LIKE '%' || COALESCE(TRIM(LOWER(:keyword)), '') || '%' OR\n" +
            "    LOWER(r.route_name) LIKE '%' || COALESCE(TRIM(LOWER(:keyword)), '') || '%' OR\n" +
            "    CAST(s.section_name AS TEXT) LIKE '%' || COALESCE(:keyword, '') || '%' OR\n" +
            "    LOWER(pt.type_name) LIKE '%' || COALESCE(TRIM(LOWER(:keyword)), '') || '%'\n" +
            "    )\n" +
            "order by pp.last_modified_date desc",nativeQuery = true)
    Page<PontoonPlacementProjection> findByIsActiveTrueAndPontoonIsAssignedTrueSearchKeyword(Pageable pageable, @Param("keyword") String keyword);
    boolean existsByPontoonId(long pontoonId);

    @Query(value = "SELECT\n" +
            "                p.pontoon_id AS pontoonId,\n" +
            "                g.ghat_name AS ghatName,\n" +
            "                pp.latitude AS latitude,\n" +
            "                pp.longitude AS longitude,\n" +
            "                s.section_name AS sectionName,\n" +
            "                ps.status_name AS status\n" +
            "            FROM\n" +
            "                pontoon_placement pp\n" +
            "             JOIN\n" +
            "                pontoon p ON p.id = pp.pontoon_id\n" +
            "             JOIN\n" +
            "                ghat g ON pp.ghat_id = g.id\n" +
            "             JOIN\n" +
            "                pontoon_section s ON g.section_id = s.id\n" +
            "             JOIN\n" +
            "                pontoon_status_update psu ON p.id = psu.pontoon_id\n" +
            "             JOIN\n" +
            "                pontoon_status ps ON psu.status_id = ps.id\n" +
            "            WHERE\n" +
            "                psu.last_modified_date = (\n" +
            "                    SELECT MAX(last_modified_date)\n" +
            "                    FROM pontoon_status_update\n" +
            "                    WHERE pontoon_id = p.id\n" +
            "                )\n" +
            "            GROUP BY\n" +
            "                p.pontoon_id, g.ghat_name, s.section_name, ps.status_name, psu.last_modified_date, pp.latitude, pp.longitude", nativeQuery = true)
    List<PontoonGoogleMapProjection> findAllPontoonGoogleMapProjectionWithoutSearch();

    @Query(value = "SELECT p.pontoon_id   AS pontoonId,\n" +
            "       g.ghat_name    AS ghatName,\n" +
            "       pp.latitude    AS latitude,\n" +
            "       pp.longitude   AS longitude,\n" +
            "       s.section_name AS sectionName,\n" +
            "       ps.status_name AS status\n" +
            "FROM pontoon_placement pp\n" +
            "         JOIN pontoon p ON p.id = pp.pontoon_id\n" +
            "         JOIN ghat g ON pp.ghat_id = g.id\n" +
            "         JOIN pontoon_section s ON g.section_id = s.id\n" +
            "         JOIN pontoon_status_update psu ON p.id = psu.pontoon_id\n" +
            "         JOIN pontoon_status ps ON psu.status_id = ps.id\n" +
            "WHERE psu.last_modified_date = (SELECT MAX(last_modified_date)\n" +
            "                                FROM pontoon_status_update\n" +
            "                                WHERE pontoon_id = p.id)\n" +
            "    and (TRIM(LOWER(ps.status_name)) LIKE '%' || COALESCE(TRIM(LOWER(:keyword)), '') || '%'\n" +
            "        OR :keyword IS NULL OR :keyword = '' OR :keyword = null)\n" +
            "   OR (TRIM(LOWER(s.section_name)) LIKE '%' || COALESCE(TRIM(LOWER(:keyword)), '') || '%'\n" +
            "    OR :keyword IS NULL OR :keyword = '' OR :keyword = null)\n" +
            "   OR (TRIM(LOWER(g.ghat_name)) LIKE '%' || COALESCE(TRIM(LOWER(:keyword)), '') || '%'\n" +
            "    OR :keyword IS NULL OR :keyword = '' OR :keyword = null)\n" +
            "   OR (CAST(pp.pontoon_id AS TEXT) LIKE '%' || COALESCE(:keyword, '') || '%'\n" +
            "    OR :keyword IS NULL OR :keyword = '' OR :keyword = null)", nativeQuery = true)
    List<PontoonGoogleMapProjection> findAllPontoonGoogleMapProjectionWithBySearch(@Param("keyword") String keyword);


    @Query(value = "select  (select count(distinct p.id) from pontoon p )as                                                totalPontoon,\n" +
            "  count(case when ps.status_name = 'Operational' then 1 else null end) totalOperational,\n" +
            "       count(case when ps.status_name = 'Need Repair' then 1 else null end) totalNeedRepair,\n" +
            "       count(case when ps.status_name = 'Sunken' then 1 else null end)      totalSunkin,\n" +
            "       count(case when ps.status_name = 'Idle' then 1 else null end)        totalIdle,\n" +
            "       (select count(distinct id) from employee where is_active = true)     totalEmployee\n" +
            "    from pontoon_placement pp\n" +
            "join pontoon_status_update psu on pp.pontoon_id = psu.pontoon_id\n" +
            "join pontoon_status ps on psu.status_id = ps.id\n" +
            "where psu.last_modified_date = (select max(last_modified_date) from pontoon_status_update where pontoon_id = psu.pontoon_id)", nativeQuery = true)
    List<DashboardData> findAllDashboardData();

    @Query(value = "select distinct p.pontoon_id as pontoonId,\n" +
            "                g.ghat_name as ghatName,\n" +
            "                g.latitude as latitude,\n" +
            "                g.longitude as longitude,\n" +
            "                s.section_name as sectionName,\n" +
            "                ps.status_name as statusName\n" +
            "From pontoon_placement pp\n" +
            "    join pontoon p on pp.pontoon_id = p.id\n" +
            "    join ghat g on pp.ghat_id = g.id\n" +
            "    join pontoon_section s on g.section_id = s.id\n" +
            "join pontoon_status_update psu on pp.pontoon_id = psu.pontoon_id\n" +
            "join pontoon_status ps on psu.status_id = ps.id\n" +
            "where psu.last_modified_date = (select max(last_modified_date) from pontoon_status_update where pontoon_id = psu.pontoon_id)", nativeQuery = true)
    List<GoogleMapData> findAllGoogleMapData();

    @Query(value = "select distinct p.pontoon_id as pontoonId,\n" +
            "                g.ghat_name as ghatName,\n" +
            "                g.latitude as latitude,\n" +
            "                g.longitude as longitude,\n" +
            "                s.section_name as sectionName,\n" +
            "                ps.status_name as statusName\n" +
            "From pontoon_placement pp\n" +
            "    join pontoon p on pp.pontoon_id = p.id\n" +
            "    join ghat g on pp.ghat_id = g.id\n" +
            "    join pontoon_section s on g.section_id = s.id\n" +
            "join pontoon_status_update psu on pp.pontoon_id = psu.pontoon_id\n" +
            "join pontoon_status ps on psu.status_id = ps.id\n" +
            "where psu.last_modified_date = (select max(last_modified_date) from pontoon_status_update where pontoon_id = psu.pontoon_id)\n" +
            "and s.section_name like '%' || :keyword || '%'  \n" +
            "   or g.ghat_name like '%' || :keyword || '%' \n" +
            "   or p.pontoon_id like '%' || :keyword || '%' \n" +
            "   or ps.status_name like '%' || :keyword || '%'", nativeQuery = true)
    List<GoogleMapData> findAllGoogleMapDataWithFiltering(@Param("keyword") String keyword);

    @Query(value = "SELECT DISTINCT\n" +
            "    p.pontoon_id AS pontoonId,\n" +
            "    pt.type_name AS pontoonType,\n" +
            "    ps.status_name AS pontoonStatus,\n" +
            "    p.build_year AS buildYear,\n" +
            "    p.build_cost AS buildCost,\n" +
            "    p.budget_source AS budgetSource,\n" +
            "    pe.procuring_name AS procuringEntity,\n" +
            "    p.manufactured_by AS manufacturedBy,\n" +
            "    pp.latitude AS latitude,\n" +
            "    pp.longitude AS longitude,\n" +
            "    ps1.section_name AS sectionName,\n" +
            "    g.ghat_name AS ghatName,\n" +
            "    r.route_name AS routeName,\n" +
            "    pi.image_url AS pontoonImage\n" +
            "FROM\n" +
            "    pontoon_placement pp\n" +
            "JOIN\n" +
            "    pontoon p ON pp.pontoon_id = p.id\n" +
            "JOIN\n" +
            "    pontoon_size psz ON p.pontoon_size_id = psz.id\n" +
            "JOIN\n" +
            "    pontoon_type pt ON psz.pontoon_type_id = pt.id\n" +
            "JOIN\n" +
            "    pontoon_status_update psu ON pp.pontoon_id = psu.pontoon_id\n" +
            "JOIN\n" +
            "    pontoon_status ps ON psu.status_id = ps.id\n" +
            "JOIN\n" +
            "    ghat g ON pp.ghat_id = g.id\n" +
            "JOIN\n" +
            "    pontoon_section ps1 ON g.section_id = ps1.id\n" +
            "JOIN\n" +
            "    route r ON g.route_id = r.id\n" +
            "LEFT JOIN\n" +
            "    (SELECT pontoon_id, MIN(id) AS min_image_id\n" +
            "     FROM pontoon_image\n" +
            "     GROUP BY pontoon_id) min_image_ids\n" +
            "ON pp.pontoon_id = min_image_ids.pontoon_id\n" +
            "LEFT JOIN\n" +
            "    pontoon_image pi ON min_image_ids.pontoon_id = pi.pontoon_id AND min_image_ids.min_image_id = pi.id\n" +
            "join procuring_entry pe on p.procuring_entity = pe.id\n" +
            "WHERE\n" +
            "    pp.pontoon_id = :pontoonId\n" +
            "AND\n" +
            "    psu.last_modified_date = (\n" +
            "    SELECT\n" +
            "        MAX(last_modified_date)\n" +
            "    FROM\n" +
            "        pontoon_status_update\n" +
            "    WHERE\n" +
            "        pontoon_id = pp.pontoon_id\n" +
            ")", nativeQuery = true)
    PontoonDetailsProjection findByPontoonIdWithPontoonDetails(Long pontoonId);

    @Query(value = "select distinct\n" +
            "    ppm.employee_id as employeeId,\n" +
            "    employee.employee_name as employeeName,\n" +
            "    employee.employee_image as employeeImage\n" +
            "from pontoon_placement pp\n" +
            "join pontoon_placement_employee ppm on pp.id = ppm.pontoon_placement_id\n" +
            "join employee on ppm.employee_id = employee.id\n" +
            "where pp.pontoon_id = :pontoonId", nativeQuery = true)
    List<LoshkarDetailsProjection> findAllByPontoonIdWithLoshkarDetails(Long pontoonId);


}

package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Pontoon;
import com.biwta.pontoon.dto.PontoonListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface PontoonRepository extends JpaRepository<Pontoon,Long> {
    Boolean existsByPontoonId(String pontoonId);
    Optional<Pontoon>findByIsActiveTrueAndId(Long id);
    @Query("SELECT p FROM Pontoon p WHERE " +
            "(:keyword IS NULL OR LOWER(p.pontoonId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.manufacturedBy) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.buildYear) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "AND p.isActive = true " +
            "ORDER BY p.id DESC")
    Page<Pontoon> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<Pontoon> findAllByIdIn(List<Long> pontoonIdList);

    Page<Pontoon> findAllByIsActiveTrueOrderByIdDesc(Pageable pageable);

    List<Pontoon> findAllByIsAssignedTrueAndIsActiveTrueOrderByIdDesc();
    List<Pontoon> findAllByIsAssignedFalseAndIsActiveTrueOrderByIdDesc();

    @Query(value = "select t1.pontoonId as id, pt.type_name as pontoonType,t1.pontoon_id as pontoonId,  t1.build_year as buildYear, t1.manufactured_by as manufacturedBy,\n" +
            "                               t1.build_cost as buildCost, t1.procuring_entity as procuringEntity, t1.budget_source as budgetSource,\n" +
            "                               t1.remarks as remarks, t1.is_assigned as isAssigned, ps.status_name as pontoonStatusName, t1.pontoon_image as pontoonImage\n" +
            "            from (select a.pontoonId, a.pontoon_id, a.build_year, a.manufactured_by, a.build_cost, a.procuring_entity, a.budget_source,\n" +
            "                   a.remarks, a.is_assigned, a.ponton_type_id, a.status_id,a.pontoon_image\n" +
            "            from(\n" +
            "            select  p.id pontoonId, p.pontoon_id, p.build_year, p.manufactured_by, p.build_cost, p.procuring_entity, p.budget_source,\n" +
            "                   p.remarks, p.is_assigned, pso.status_id, p.ponton_type_id,p.pontoon_image,\n" +
            "                DENSE_RANK() OVER (partition by p.id ORDER BY pso.created_date DESC) rnk\n" +
            "            from pontoon p\n" +
            "            left join pontoon_status_update pso on p.id=pso.pontoon_id)a\n" +
            "            where rnk=1\n" +
            "            ) t1\n" +
            "            left join pontoon_status ps on t1.status_id=ps.id\n" +
            "            left join pontoon_type pt on t1.ponton_type_id=pt.id",nativeQuery = true)
    List<PontoonListProjection>findAllPontoonList();

    @Query(value = "select t1.pontoonId as id, pt.type_name as pontoonType,t1.pontoon_id as pontoonId,  t1.build_year as buildYear, t1.manufactured_by as manufacturedBy,\n" +
            "                   t1.build_cost as buildCost, t1.procuring_entity as procuringEntity, t1.budget_source as budgetSource,\n" +
            "                   t1.remarks as remarks, t1.is_assigned as isAssigned, ps.status_name as pontoonStatusName,t1.pontoon_image as pontoonImage\n" +
            "from (select a.pontoonId, a.pontoon_id, a.build_year, a.manufactured_by, a.build_cost, a.procuring_entity, a.budget_source,\n" +
            "       a.remarks, a.is_assigned, a.ponton_type_id, a.status_id,a.pontoon_image\n" +
            "from(\n" +
            "select  p.id pontoonId, p.pontoon_id, p.build_year, p.manufactured_by, p.build_cost, p.procuring_entity, p.budget_source,\n" +
            "       p.remarks, p.is_assigned, pso.status_id, p.ponton_type_id,p.pontoon_image,\n" +
            "    DENSE_RANK() OVER (partition by p.id ORDER BY pso.created_date DESC) rnk\n" +
            "from pontoon p\n" +
            "left join pontoon_status_update pso on p.id=pso.pontoon_id)a\n" +
            "where rnk=1\n" +
            ") t1\n" +
            "left join pontoon_status ps on t1.status_id=ps.id\n" +
            "left join pontoon_type pt on t1.ponton_type_id=pt.id",
            countQuery ="select count(*) from (select t1.pontoonId as id, pt.type_name as pontoonType,t1.pontoon_id as pontoonId,  t1.build_year as buildYear, t1.manufactured_by as manufacturedBy,\n" +
                    "                   t1.build_cost as buildCost, t1.procuring_entity as procuringEntity, t1.budget_source as budgetSource,\n" +
                    "                   t1.remarks as remarks, t1.is_assigned as isAssigned, ps.status_name as pontoonStatusName\n" +
                    "from (select a.pontoonId, a.pontoon_id, a.build_year, a.manufactured_by, a.build_cost, a.procuring_entity, a.budget_source,\n" +
                    "       a.remarks, a.is_assigned, a.ponton_type_id, a.status_id\n" +
                    "from(\n" +
                    "select  p.id pontoonId, p.pontoon_id, p.build_year, p.manufactured_by, p.build_cost, p.procuring_entity, p.budget_source,\n" +
                    "       p.remarks, p.is_assigned, pso.status_id, p.ponton_type_id,\n" +
                    "    DENSE_RANK() OVER (partition by p.id ORDER BY pso.created_date DESC) rnk\n" +
                    "from pontoon p\n" +
                    "left join pontoon_status_update pso on p.id=pso.pontoon_id)a\n" +
                    "where rnk=1\n" +
                    ") t1\n" +
                    "left join pontoon_status ps on t1.status_id=ps.id\n" +
                    "left join pontoon_type pt on t1.ponton_type_id=pt.id)",nativeQuery = true)
    Page<PontoonListProjection>findAllPontoonListWithPagination(Pageable pageable);

    @Query(value = "SELECT\n" +
            "    t1.pontoonId AS id,\n" +
            "    pt.type_name AS pontoonType,\n" +
            "    t1.pontoon_id AS pontoonId,\n" +
            "    t1.build_year AS buildYear,\n" +
            "    t1.manufactured_by AS manufacturedBy,\n" +
            "    t1.build_cost AS buildCost,\n" +
            "    t1.procuring_entity AS procuringEntity,\n" +
            "    t1.budget_source AS budgetSource,\n" +
            "    t1.remarks AS remarks,\n" +
            "    t1.is_assigned AS isAssigned,\n" +
            "    ps.status_name AS pontoonStatusName\n" +
            "FROM\n" +
            "    (\n" +
            "        SELECT\n" +
            "            a.pontoonId,\n" +
            "            a.pontoon_id,\n" +
            "            a.build_year,\n" +
            "            a.manufactured_by,\n" +
            "            a.build_cost,\n" +
            "            a.procuring_entity,\n" +
            "            a.budget_source,\n" +
            "            a.remarks,\n" +
            "            a.is_assigned,\n" +
            "            a.ponton_type_id,\n" +
            "            a.status_id\n" +
            "        FROM\n" +
            "            (\n" +
            "                SELECT\n" +
            "                    p.id AS pontoonId,\n" +
            "                    p.pontoon_id,\n" +
            "                    p.build_year,\n" +
            "                    p.manufactured_by,\n" +
            "                    p.build_cost,\n" +
            "                    p.procuring_entity,\n" +
            "                    p.budget_source,\n" +
            "                    p.remarks,\n" +
            "                    p.is_assigned,\n" +
            "                    pso.status_id,\n" +
            "                    p.ponton_type_id,\n" +
            "                    DENSE_RANK() OVER (PARTITION BY p.id ORDER BY pso.created_date DESC) rnk\n" +
            "                FROM\n" +
            "                    pontoon p\n" +
            "                LEFT JOIN pontoon_status_update pso ON p.id = pso.pontoon_id\n" +
            "            ) a\n" +
            "        WHERE\n" +
            "            rnk = 1\n" +
            "                                 AND (:pontoonId IS NULL OR LOWER(a.pontoon_id) LIKE LOWER(CONCAT('%', :pontoonId, '%')))\n" +
            "\n" +
            "    ) t1\n" +
            "LEFT JOIN pontoon_status ps ON t1.status_id = ps.id\n" +
            "LEFT JOIN pontoon_type pt ON t1.ponton_type_id = pt.id",nativeQuery = true)
    Page<PontoonListProjection>findAllPontoonListWithPaginationFilter(String pontoonId,Pageable pageable);

}

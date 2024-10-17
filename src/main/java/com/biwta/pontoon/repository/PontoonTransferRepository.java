package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.PontoonTransfer;
import com.biwta.pontoon.dto.OldPontoonTransferInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author nasimkabir
 * ৩০/১১/২৩
 */
public interface PontoonTransferRepository extends JpaRepository<PontoonTransfer, Long> {
    @Query("SELECT pt FROM PontoonTransfer pt WHERE pt.tillDate < :tillDate AND pt.pontoon.id = :pontoonId")
    List<PontoonTransfer> findExistingTransfersBeforeTillDate(@Param("tillDate") Date tillDate, @Param("pontoonId") Long pontoonId);

    List<PontoonTransfer> findByPontoon_Id(Long pontoonId);

    boolean existsByTillDateBeforeAndPontoon_Id(Date transferDate, Long pontoonId);

    List<PontoonTransfer> findAllByOrderByIdDesc();

    @Query(value = "select g.ghat_name as ghatName,\n" +
            "       pt.latitude,\n" +
            "       pt.longitude,\n" +
            "       string_agg(concat(e.pms_id), ',') as employeeId,\n" +
            "       string_agg(concat(e.employee_name), ',') as employeeName\n" +
            "from pontoon_transfer pt\n" +
            "         join pontoon_transfer_employee pte on pt.id = pte.pontoon_transfer_id\n" +
            "    join public.employee e on e.id = pte.employee_id\n" +
            "         left join pontoon_placement p on pt.pontoon_id = p.id\n" +
            "         join public.ghat g on pt.ghat_id = g.id\n" +
            "join pontoon pont on pt.pontoon_id = pont.id\n" +
            "where pt.till_date > now()\n" +
            "  and pt.pontoon_id = :pontoonId\n" +
            "group by pt.longitude, pt.latitude, g.ghat_name", nativeQuery = true)
    List<OldPontoonTransferInfo> oldPontoonTransferInfo(long pontoonId);
}

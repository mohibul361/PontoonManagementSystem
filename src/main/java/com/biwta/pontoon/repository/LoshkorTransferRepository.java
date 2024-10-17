package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.LoshkorTransfer;
import com.biwta.pontoon.dto.OldLoshkarTransferInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author nasimkabir
 * ২/১২/২৩
 */
public interface LoshkorTransferRepository extends JpaRepository<LoshkorTransfer, Long> {
    List<LoshkorTransfer> findAllByEmployee_Id(long loshkarId);

    @Query(value = "select p.pontoon_id as pontoonName,\n" +
            "       g.latitude,\n" +
            "       g.longitude\n" +
            "from pontoon_placement pp\n" +
            "join pontoon p on pp.pontoon_id = p.id\n" +
            "join pontoon_placement_employee ppe on pp.id = ppe.pontoon_placement_id\n" +
            "join employee e on ppe.employee_id = e.id\n" +
            "join ghat g on pp.ghat_id = g.id\n" +
            "where e.id = :employeeId", nativeQuery = true)
    List<OldLoshkarTransferInfo> oldLoshkarTransferInfo(long employeeId);
}

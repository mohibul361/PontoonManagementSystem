package com.biwta.pontoon.controller;

import com.biwta.pontoon.domain.PontoonSize;
import com.biwta.pontoon.domain.PontoonType;
import com.biwta.pontoon.dto.AddPontoonTypeDTO;
import com.biwta.pontoon.service.PontoonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author nasimkabir
 * ২৯/১১/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pontoonType")
public class PontoonTypeController {
    private final PontoonTypeService pontoonTypeService;

    @PostMapping("/addPontoonType")
    public ResponseEntity<String> addPontoonType(@Valid @RequestBody AddPontoonTypeDTO addPontoonTypeDTO,
                                                 HttpServletRequest request) {
        if (pontoonTypeService.addPontoonType(addPontoonTypeDTO, request))
            return ResponseEntity.ok("PontoonType Saved Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("PontoonType Not Saved Successfully Inserted");
    }

    @GetMapping("/getPontoonType")
    public ResponseEntity<PontoonSize> getPontoonType(@RequestParam long pontoonTypeId) {
        return ResponseEntity.ok(pontoonTypeService.getPontoonType(pontoonTypeId));
    }

    @GetMapping("/pontoonSizeList")
    public ResponseEntity<Page<PontoonSize>> getAllPontoonType(@RequestParam(defaultValue = "0") int pageNo,
                                                               @RequestParam(defaultValue = "10") int pageSize,
                                                               @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(pontoonTypeService.findAllPontoonType(pageNo, pageSize, searchKeyword));
    }

    @GetMapping("/pontoonTypeListWithoutPagination")
    public ResponseEntity<List<PontoonSize>> pontoonTypeListWithoutPagination() {
        return ResponseEntity.ok(pontoonTypeService.findAllPontoonTypeWithoutPagination());
    }

    @PutMapping("updatePontoonType")
    public ResponseEntity<?> updatePontoonType(@RequestBody AddPontoonTypeDTO addPontoonTypeDTO,
                                               @RequestParam long pontoonTypeId,
                                               HttpServletRequest request) {
        if (pontoonTypeService.updatePontoonType(pontoonTypeId, addPontoonTypeDTO, request)) {
            return ResponseEntity.ok("PontoonType Updated Successfully");
        } else {
            return ResponseEntity.badRequest().body("PontoonType Not Updated Successfully");
        }
    }

    @DeleteMapping("deletePontoonType")
    public ResponseEntity<?> deletePontoonType(@RequestParam long pontoonTypeId,
                                               HttpServletRequest request) {
        if (pontoonTypeService.deletePontoonType(pontoonTypeId, request)) {
            return ResponseEntity.ok("PontoonType Deleted Successfully");
        } else {
            return ResponseEntity.badRequest().body("PontoonType Not Deleted Successfully");
        }
    }

    @GetMapping("/pontoonTypeList")
    public ResponseEntity<List<PontoonType>> pontoonTypeListAll() {
        return ResponseEntity.ok(pontoonTypeService.findAllPontoonType());
    }
}

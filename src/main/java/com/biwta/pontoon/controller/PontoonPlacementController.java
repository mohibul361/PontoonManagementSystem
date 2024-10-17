package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.AddPontoonPlacementDTO;
import com.biwta.pontoon.dto.PontoonGoogleMapProjection;
import com.biwta.pontoon.dto.PontoonPlacementListDto;
import com.biwta.pontoon.service.PontoonPlacementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author nasimkabir
 * ৩০/১১/২৩
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/pontoon-placement")
public class PontoonPlacementController {
    private final PontoonPlacementService pontoonPlacementService;

    @PostMapping("/addPontoonPlacement")
    public ResponseEntity<String> addPontoonPlacement(@Valid @RequestBody AddPontoonPlacementDTO addPontoonPlacementDTO,
                                                      HttpServletRequest request) {
        if (pontoonPlacementService.addPontoonPlacement(addPontoonPlacementDTO, request))
            return ResponseEntity.ok("Pontoon Placement Saved Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Pontoon Placement Not Saved Successfully Inserted");
    }

    @GetMapping("/getAllPontoonPlacement")
    public ResponseEntity<Page<PontoonPlacementListDto>> getAllPontoonPlacement(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                                @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                                                @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(pontoonPlacementService.findAllPontoonPlacement(pageNo, pageSize, searchKeyword));
    }

   /* @GetMapping("/customerPontoonList")
    public ResponseEntity<?> customerPontoon(@RequestParam (defaultValue = "0", required = false) int pageNo,
                                             @RequestParam(defaultValue = "10", required = false) int pageSize) {
        return ResponseEntity.ok(pontoonPlacementService.currentPontoonList(pageNo, pageSize));
    }*/

    @GetMapping("/getDashboardData")
    public ResponseEntity<?> getDashboardData() {
        return ResponseEntity.ok(pontoonPlacementService.findAllDashboardData());
    }

   /* @GetMapping("/getGoogleMapData")
    public ResponseEntity<?> getGoogleMapData(@RequestParam (required = false) String searchKeyword) {
        return ResponseEntity.ok(pontoonPlacementService.findAllGoogleMapData(searchKeyword));
    }*/

    @GetMapping("/getGoogleMapData")
    public ResponseEntity<?> getGoogleMapData(@RequestParam(required = false) String searchKeyword) {
        log.info("Received request with searchKeyword: {}", searchKeyword);
        List<PontoonGoogleMapProjection> data = pontoonPlacementService.findAllGoogleMapData(searchKeyword);
        log.info("Returning {} records", data.size());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/assaignPontoonList")
    public ResponseEntity<?> assaignPontoonList(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(pontoonPlacementService.assaignPontoonList(pageNo, pageSize, searchKeyword));
    }

    @GetMapping("/getPontoonDetails")
    public ResponseEntity<?> getPontoonDetails(@RequestParam Long pontoonId) {
        return ResponseEntity.ok(pontoonPlacementService.findByPontoonIdWithPontoonDetails(pontoonId));
    }

}

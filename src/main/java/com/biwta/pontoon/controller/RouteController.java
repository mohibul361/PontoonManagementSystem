package com.biwta.pontoon.controller;

import com.biwta.pontoon.dto.AddRoute;
import com.biwta.pontoon.service.PortService;
import com.biwta.pontoon.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author nasimkabir
 * ৪/১২/২৩
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/route")
public class RouteController {
    private final RouteService routeService;
    private final PortService portService;

    @PostMapping("/addRoute")
    public ResponseEntity<String> addRoute(@Valid @RequestBody AddRoute route,
                                           HttpServletRequest request) {
        if (routeService.addRoute(route, request))
            return ResponseEntity.ok("Route Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Route Not Inserted");
    }

    @GetMapping("/routeList")
    public ResponseEntity<?> routeList(@RequestParam long sectionId) {
        return ResponseEntity.ok(routeService.routeList(sectionId));
    }

    @GetMapping("/withoutSectionRouteList")
    public ResponseEntity<?> withoutSectionRouteList() {
        return ResponseEntity.ok(routeService.withoutSectionRouteList());
    }

    @GetMapping("/portList")
    public ResponseEntity<?> portList(@RequestParam long sectionId) {
        return ResponseEntity.ok(portService.routeList(sectionId));
    }

    @GetMapping("/getPortById")
    public ResponseEntity<?> getPortById(@RequestParam long id) {
        return ResponseEntity.ok(portService.getPortById(id));
    }

    @PutMapping("/updatePort")
    public ResponseEntity<?> updatePort(@RequestParam long id, @RequestParam String portName, HttpServletRequest request) {
        if (portService.updatePort(id, portName, request))
            return ResponseEntity.ok("Port Successfully Updated");
        else
            return ResponseEntity.badRequest().body("Port Not Updated");
    }

    @PostMapping("/addPort")
    public ResponseEntity<?> addPort(@RequestParam String portName, HttpServletRequest request) {
        if (portService.addPort(portName, request))
            return ResponseEntity.ok("Port Successfully Inserted");
        else
            return ResponseEntity.badRequest().body("Port Not Inserted");
    }
}

package kigali.duka.rw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.duka.rw.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService service;

    // bonus - dashboard
    @GetMapping("/summary")
    public ResponseEntity<?> summary() {
        return new ResponseEntity<>(service.summary(), HttpStatus.OK);
    }
}

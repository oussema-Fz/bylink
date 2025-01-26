package com.bylink.controller;

import com.bylink.service.DeliveryService;
import com.bylink.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final TransactionService transactionService;

    @PostMapping("/schedule/{transactionId}")
    public ResponseEntity<Map<String, String>> scheduleDelivery(@PathVariable Long transactionId) {
        try {
            var transaction = transactionService.getTransaction(transactionId);
            String trackingId = deliveryService.scheduleDelivery(transaction);
            transaction.setTrackingId(trackingId);
            transactionService.updateTransaction(transaction);
            
            return ResponseEntity.ok(Map.of(
                "trackingId", trackingId,
                "message", "Delivery scheduled successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Failed to schedule delivery: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/status/{trackingId}")
    public ResponseEntity<Map<String, Object>> getDeliveryStatus(@PathVariable String trackingId) {
        try {
            Map<String, Object> status = deliveryService.getDeliveryStatus(trackingId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Failed to get delivery status: " + e.getMessage()
            ));
        }
    }
}

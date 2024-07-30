package com.workeache.precionline.api.demo.controller;

import com.workeache.precionline.api.demo.persistence.entities.DataApiRee;
import com.workeache.precionline.api.demo.services.ApiReeService;
import com.workeache.precionline.api.demo.services.DataApiReeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@CrossOrigin(origins = {
        "https://precionline.com",
        "https://www.precionline.com",
        "http://precionline.com",
        "http://www.precionline.com"
}) // Permite CORS para este origen
@RestController
@RequestMapping("/prices")
public class PricesController {

    @Autowired
    private ApiReeService apiReeService;

    @Autowired
    private DataApiReeService dataApiReeService;

    @Value("${precionline.server.ip}")
    private String SERVER_IP;


    @GetMapping("/actual")
    public ResponseEntity<?> getActualPrices(HttpServletRequest request) {
        if (!isRequestFromHost(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado.");
        }

        try {
            DataApiRee dataApiRee = dataApiReeService.getActualDayPrices();
            return ResponseEntity.ok(dataApiRee.getData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios del día actual: " + e.getMessage());
        }
    }

    @GetMapping("/nextday")
    public ResponseEntity<?> getNextDayPrices(HttpServletRequest request) {
        if (!isRequestFromHost(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado.");
        }

        try {
            DataApiRee dataApiRee = dataApiReeService.getNextDayPrices();
            return ResponseEntity.ok(dataApiRee.getData());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios del siguiente día: " + e.getMessage());
        }
    }

    @GetMapping("/query")
    public ResponseEntity<?> getPricesByDate(@RequestParam String date, HttpServletRequest request) {
        if (!isRequestFromHost(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado.");
        }

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, dtf);
            DataApiRee dataApiRee = dataApiReeService.getPricesByDate(localDate);
            return ResponseEntity.ok(dataApiRee.getData());
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Fecha inválida: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los precios por fecha: " + e.getMessage());
        }
    }

    @GetMapping("/updateActualPrice")
    public ResponseEntity<?> updateActualPrice(HttpServletRequest request) {
        if (!isRequestFromHost(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado.");
        }

        try {
            LocalDate actualDate = LocalDate.now();
            LocalDate nextDayDate = actualDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));
            return ResponseEntity.ok("Precios día actual actualizados");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios del día actual: " + e.getMessage());
        }
    }

    @GetMapping("/updateNextDayPrices")
    public ResponseEntity<?> updateNextDayPrices(HttpServletRequest request) {
        if (!isRequestFromHost(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado.");
        }

        try {
            LocalDate actualDate = LocalDate.now().plusDays(1);
            LocalDate nextDayDate = actualDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

            actualDate = actualDate.plusDays(1);
            nextDayDate = nextDayDate.plusDays(1);
            dataApiReeService.save(apiReeService.updatePrices(actualDate, nextDayDate));

            return ResponseEntity.ok("Precios día siguiente actualizados");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios del día actual: " + e.getMessage());
        }
    }

    @GetMapping("/updateAllMonth")
    public ResponseEntity<?> updateActualMonth(@RequestParam int month, @RequestParam int year, HttpServletRequest request) {
        if (!isRequestFromHost(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado.");
        }

        try {
            LocalDate intitalDate = LocalDate.of(year, month, 1);
            LocalDate nextDayDate = intitalDate.plusDays(1);
            do {
                dataApiReeService.save(apiReeService.updatePrices(intitalDate, nextDayDate));
                intitalDate = nextDayDate;
                nextDayDate = intitalDate.plusDays(1);
            } while (intitalDate.getMonth().getValue() == month);

            return ResponseEntity.ok("Precios del mes actualizados");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar los precios de todo el mes: " + e.getMessage());
        }
    }

    private boolean isRequestFromHost(HttpServletRequest request) {



        // Obtener la IP del cliente
        String clientIp = request.getRemoteAddr();

        System.out.println("IP SERVIDOR: " + clientIp);

        // Comparar la IP del cliente con la IP del hosting permitida
        return SERVER_IP.substring(0, 6).equals(clientIp.substring(0,6));
    }
}

package tipone.buone.api.vehiclemanagement.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("production")
public class SetupController {
    @GetMapping("/awake")
    public String awake() { return "Server wake-up request received successfully"; }
}
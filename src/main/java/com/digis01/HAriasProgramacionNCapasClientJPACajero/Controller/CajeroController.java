package com.digis01.HAriasProgramacionNCapasClientJPACajero.Controller;

import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/atm")
public class CajeroController {

    private final String END_POINT_BASE = "http://localhost:8081/api/cajero";
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String saldo(Model model) {
        ResponseEntity<Double> responseSaldo = restTemplate.exchange(END_POINT_BASE + "/total",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Double>() {
        });
        model.addAttribute("saldo", responseSaldo.getBody());
        return "Cajero";

    }
    
    @PostMapping("/retirar")
    public String retirarEfectivo(@RequestParam("monto") double monto, Model model){
        try {
            ResponseEntity<Map<Double,Integer>> responseRetirar = restTemplate.exchange(END_POINT_BASE+"/retirar"
                    , HttpMethod.POST, 
                    new HttpEntity<>(monto),
                    new ParameterizedTypeReference<Map<Double, Integer>>(){});
        } catch (Exception e) {
        }
        
        return " Cajero";
    }

}

package com.digis01.HAriasProgramacionNCapasClientJPACajero.Controller;

import com.digis01.HAriasProgramacionNCapasClientJPACajero.ML.Result;
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
        try {
            ResponseEntity<Double> responseSaldo = restTemplate.exchange(END_POINT_BASE + "/total",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Double>() {
            });
            ResponseEntity<Map<Double, Integer>> response = restTemplate.exchange(
                    END_POINT_BASE + "/inventario",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Map<Double, Integer>>() {
            }
            );
            model.addAttribute("inventario", response.getBody());
            model.addAttribute("saldo", responseSaldo.getBody());

        } catch (Exception e) {
            model.addAttribute("error", "Error al acceder al api" + e.getLocalizedMessage());
        }

        return "Cajero";
    }

    @PostMapping("/retirar")
    public String retirarEfectivo(@RequestParam("monto") double monto, Model model) {
        try {
            ResponseEntity<Map<Double, Integer>> responseRetirar = restTemplate.exchange(END_POINT_BASE + "/retirar?monto=" + monto,
                    HttpMethod.POST,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Map<Double, Integer>>() {
            });

            if (responseRetirar.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("desglose", responseRetirar.getBody());
                model.addAttribute("mensaje", "Retiro exitoso");
            }

             ResponseEntity<Double> responseSaldo = restTemplate.exchange(END_POINT_BASE + "/total",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Double>() {
            });
            model.addAttribute("saldo", responseSaldo.getBody());
        } catch (Exception e) {
            ResponseEntity<Double> responseSaldo = restTemplate.exchange(END_POINT_BASE + "/total",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Double>() {
            });
            model.addAttribute("saldo", responseSaldo.getBody());
            model.addAttribute("error", e.getLocalizedMessage());
            return "Cajero";
        }

        return "Cajero";
    }
    @PostMapping("/rellenar")
    public String rellenarCajero(Model model){
        try {
            ResponseEntity<Map<Double, Integer>> responseRellenar = restTemplate.exchange(END_POINT_BASE+"/rellenar" 
                    , HttpMethod.POST
                    , HttpEntity.EMPTY, 
                    new ParameterizedTypeReference<Map<Double, Integer>>(){});
            if (responseRellenar.getStatusCode().is2xxSuccessful()) {
                Map<Double, Integer> resultado = responseRellenar.getBody();
                if(resultado != null && !resultado.isEmpty()){
                    model.addAttribute("rellenar", resultado);
                    model.addAttribute("mensaje", "Cajero rebastecido");
                }else{
                    model.addAttribute("mensaje", "El cajero ya se encuentra lleno");
                }
            }
            ResponseEntity<Double> responseTotal = restTemplate.exchange(END_POINT_BASE+"/total" 
                    , HttpMethod.GET, 
                    HttpEntity.EMPTY, 
                    new ParameterizedTypeReference<Double>(){});
            model.addAttribute("saldo", responseTotal.getBody());
        } catch (Exception e) {
            ResponseEntity<Double> responseTotal = restTemplate.exchange(END_POINT_BASE+"/total" 
                    , HttpMethod.GET, 
                    HttpEntity.EMPTY, 
                    new ParameterizedTypeReference<Double>(){});
            
            model.addAttribute("saldo", responseTotal.getBody());
            model.addAttribute("error", "Error al rellenar :"+e.getMessage());
        }
        return "Cajero";
    }

}

package br.com.hsd.catraca.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("protocolo")
public class CatracaResource {

    private final CatracaService catracaService;

    @PostMapping("{ip}")
    public ResponseEntity<?> request(@RequestBody String request,@PathVariable("ip")String ip){
        var resp = catracaService.sendMenssage(request,ip);
        return ResponseEntity.ok(resp);
    }
}

package Web.Application.Application.сontroller;



import Web.Application.Application.domain.Orderer;
import Web.Application.Application.repo.OrdererRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@RestController
@RequestMapping("orderer")
public class OrdererController {
    private final OrdererRepo ordererRepo;

    public OrdererController(OrdererRepo ordererRepo) {
        this.ordererRepo = ordererRepo;
    }

    @GetMapping
    public List<Orderer> list() {
        return ordererRepo.findAll();
    }

    @GetMapping("{id}")
    public Orderer getOne(@PathVariable("id") Orderer orderer) {
        return orderer;
    }

    @PostMapping
    public Orderer create(@RequestBody Orderer orderer) {
        orderer.setCreationTime(LocalDateTime.now());
        return ordererRepo.save(orderer);
    }

    @PutMapping("{id}")
    public Orderer update(@RequestBody Orderer orderer,
                          @PathVariable("id") Orderer ordererFromDB){
        BeanUtils.copyProperties(orderer, ordererFromDB,  "id");
        return ordererRepo.save(ordererFromDB);
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Orderer orderer){ ordererRepo.delete(orderer);}


//    @Scheduled(fixedRate = 6000)
//    public void PutScheduleInCollection() {
//        count++;
//        orderDatabase.add(new HashMap<String, String>() {{
//            put("id", String.valueOf(count));
//            put("Name", "Name_5");
//            put("Product", "Product_5");
//            put("Time", String.valueOf(LocalDateTime.now()));
//        }});
//    }
}

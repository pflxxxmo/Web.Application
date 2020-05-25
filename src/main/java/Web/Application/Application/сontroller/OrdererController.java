package Web.Application.Application.сontroller;



import Web.Application.Application.domain.Orderer;
import Web.Application.Application.repo.OrdererRepo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;


@RestController
@RequestMapping("orderer")

@EnableCaching
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
    @Cacheable(cacheNames = "name")
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
    @org.springframework.transaction.annotation.Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void delete(@PathVariable("id") Orderer orderer){
        Logger log = null;
        try {
           ordererRepo.delete(orderer);
           doExpensiveWork();
       }catch(EmptyResultDataAccessException | InterruptedException e)
       {
            e.getMessage();
       }
    }

    private void doExpensiveWork() throws InterruptedException{
        Thread.sleep(5000);
        throw new RuntimeException();
    }


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

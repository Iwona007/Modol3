package pl.iwona.listapojazdowasmodol3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iwona.listapojazdowasmodol3.model.Car;
import pl.iwona.listapojazdowasmodol3.model.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/cars")
public class CarApi {

    private List<Car> cars;

    public CarApi() {
        this.cars = new ArrayList<>();
        cars.add(new Car(1L, "Ferrari", "599 GTB Fiorano", Color.RED));
        cars.add(new Car(2L, "Audi", "A6", Color.NAVY_BLUE));
        cars.add(new Car(3L, "Aston Martin", "DB5", Color.RED));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getById(@PathVariable Long id) {
        Optional<Car> findCar = cars.stream().filter(car -> car.getId() == id).findFirst();
        if (findCar.isPresent()) {
            return new ResponseEntity<>(findCar.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Car>> getByColor(@PathVariable String color) {
        List<Car> findColor = cars.stream().filter(car -> color.equalsIgnoreCase(car.getColor().name()))
                .collect(Collectors.toList());
        if (!findColor.isEmpty()) {
           return new ResponseEntity<>(findColor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/new")
    public ResponseEntity addCar(@RequestBody Car car) {
        boolean add = cars.add(car);
        if (add) {
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity modifyCar(@RequestBody Car modifyCar) {
        Optional<Car> findCar = cars.stream().filter(car -> car.getId() == modifyCar.getId()).findFirst();
        if (findCar.isPresent()) {
            cars.remove(findCar.get());
            cars.add(modifyCar);
            return new ResponseEntity(modifyCar, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/update")
    public ResponseEntity updateByID(@RequestBody Car updatedCar){
     Optional<Car> findCar = cars.stream().filter(car -> car.getId() == updatedCar.getId()).findFirst();
        if(findCar.isPresent()){
            cars.remove(findCar.get());
            cars.add(updatedCar);
            return new ResponseEntity(updatedCar, HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        Optional<Car> toRemove = cars.stream().filter(car -> car.getId() == id).findFirst();
        if (toRemove.isPresent()) {
            cars.remove(toRemove.get());
            return new ResponseEntity(toRemove.get(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}

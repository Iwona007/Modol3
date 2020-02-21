package pl.iwona.listapojazdowasmodol3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iwona.listapojazdowasmodol3.model.Car;
import pl.iwona.listapojazdowasmodol3.converColor.ConvertColor;
import pl.iwona.listapojazdowasmodol3.service.CarServiceInter;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(value="/api/cars", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,})
public class CarApi {

    private CarServiceInter carServiceInter;
    private ConvertColor convertColor;

    @Autowired
    public CarApi(CarServiceInter carServiceInter, ConvertColor convertColor) {
        this.carServiceInter = carServiceInter;
        this.convertColor = convertColor;
    }

    @GetMapping
    public ResponseEntity<Resources<Car>> getAllCars() {  //Liste zamieniam na Resources
        List<Car> carsList = carServiceInter.getAll();
        carsList.forEach(car -> car.add(linkTo(CarApi.class).slash(car.getCarId()).withSelfRel()));
        Link link = linkTo(CarApi.class).withSelfRel();
        Resources<Car> carResources = new Resources<>(carsList, link);
        return new ResponseEntity<>(carResources, HttpStatus.OK);
    }

    @GetMapping ("/{carId}") //(value = ("/{carId}"), produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Resource<Car>> getById(@PathVariable Long carId) {
        Link link = linkTo(CarApi.class).slash(carId).withSelfRel();
        Optional<Car> findCar = carServiceInter.carById(carId);

        Resource<Car> carResource = new Resource<>(findCar.get(), link);
        return findCar.map(car -> new ResponseEntity<>(carResource, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 500Internal Server Error gdy mam Resource
//        return findCar.map(car -> new ResponseEntity<>(findCar.get(), HttpStatus.OK)).orElseGet(() ->
//                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping ("/color/{color}")
    public ResponseEntity<Resources<Car>> getByColor(@PathVariable String color) { // zamieniam liste na na Resources
        List<Car> findColor = carServiceInter.carByColor(color);
        findColor.forEach(car -> car.add(linkTo(CarApi.class).slash(car.getCarId()).withSelfRel()));
        findColor.forEach(car -> car.add(linkTo(CarApi.class).withRel("all colors")));
        Link link = linkTo(CarApi.class).withSelfRel();
        Resources<Car> carResources = new Resources<>(findColor, link);
        if (!findColor.isEmpty()) {
            return new ResponseEntity<>(carResources, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/new")
    public ResponseEntity addCar(@RequestBody Car car) {
        if (carServiceInter.save(car)) {
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // sprawdzic bad request
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Resource<Car>> modifyCar(@PathVariable Long id, @RequestBody Car modifyCar) {
        Link link = linkTo(CarApi.class).slash(id).withSelfRel();

        if (carServiceInter.changeCar(id, modifyCar)) {
            Resource<Car> carResource = new Resource(carServiceInter.changeCar(id, modifyCar), link);
            return new ResponseEntity(carResource,HttpStatus.CREATED);
        } else if (!carServiceInter.changeCar(id, modifyCar)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{id}/color/{newColor}}") // tu nie działa convertowanie z enum
    public ResponseEntity<Car> updateColor(@PathVariable Long id, @PathVariable String newColor) {
        boolean change = carServiceInter.changeColor(id, convertColor.convertToEnum(newColor));
        if (change) {
            return new ResponseEntity(HttpStatus.CREATED);
        }else if(!change){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // mam odpowwiedz not founded ale bez wyjatku
    }

    @PatchMapping("/{id}/mark/{newMark}")
    public ResponseEntity updateModel(@PathVariable Long id, @PathVariable String newMark){
        boolean changedMark = carServiceInter.changeMark(id, newMark);
        if(changedMark){
            return new ResponseEntity<>(HttpStatus.OK);
        }else if(!changedMark){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Car> deleteById(@PathVariable Long id) {
//        Link link = linkTo(CarApi.class).slash(id).withSelfRel();
        boolean remove = carServiceInter.removeById(id);
//        Resource<Car> carResource = new Resource(carServiceInter.removeById(id), link);
        if (remove) {
            return new ResponseEntity<>( HttpStatus.OK);
        }else if(!remove){
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);// 500Internal Server Error gdy mam Resource
    }
}


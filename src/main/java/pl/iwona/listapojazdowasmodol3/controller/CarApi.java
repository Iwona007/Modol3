package pl.iwona.listapojazdowasmodol3.controller;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.iwona.listapojazdowasmodol3.converColor.ConvertColor;
import pl.iwona.listapojazdowasmodol3.model.Car;
import pl.iwona.listapojazdowasmodol3.service.CarServiceInter;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/cars", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class CarApi {

    private CarServiceInter carServiceInter;
    private ConvertColor convertColor;

    @Autowired
    public CarApi(CarServiceInter carServiceInter, ConvertColor convertColor) {
        this.carServiceInter = carServiceInter;
        this.convertColor = convertColor;
    }

    @ApiOperation(value = "Get all cars")
    @GetMapping
    public ResponseEntity<CollectionModel<Car>> getAllCars() {  //Liste zamieniam na Resources
        List<Car> carsList = carServiceInter.getAll();
        carsList.forEach(car -> car.addIf(!car.hasLinks(), () -> linkTo(CarApi.class).slash(car.getCarId()).withSelfRel()));
        Link link = linkTo(CarApi.class).withSelfRel();
        CollectionModel<Car> carResources = new CollectionModel<>(carsList, link);
        return new ResponseEntity<>(carResources, HttpStatus.OK);
    }

    @ApiOperation(value = "Get car by Id")
    @GetMapping("/{carId}")
    //(value = ("/{carId}"), produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<EntityModel<Car>> getById(@PathVariable @NotNull Long carId) {   //
        Link link = linkTo(CarApi.class).slash(carId).withSelfRel();
        Optional<Car> findCar = carServiceInter.carById(carId);
        if (findCar.isPresent()) {
            EntityModel<Car> carEntity = new EntityModel(findCar.get(), link);
            return new ResponseEntity<>(carEntity, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Get car by color")
    @GetMapping("/color/{color}")
    public ResponseEntity<CollectionModel<Car>> getByColor(@PathVariable @NotNull String color) { // zamieniam liste na na Resources
        List<Car> findColor = carServiceInter.carByColor(color);
        findColor.forEach(car -> car.addIf(!car.hasLinks(), ()->linkTo(CarApi.class).slash(car.getCarId()).withSelfRel()));
        findColor.forEach(car -> car.addIf(car.hasLinks(),()-> linkTo(CarApi.class).slash("/color/"+ car.getColor())
                .withRel("all colors")));
        Link link = linkTo(CarApi.class).withSelfRel();
        CollectionModel<Car> carCollection = new CollectionModel<>(findColor, link);
            return new ResponseEntity<>(carCollection, HttpStatus.OK);
        }

    @ApiOperation(value = "Add a new car")
    @PostMapping("/new")
    public ResponseEntity<Car> addCar(@Valid @RequestBody Car car) {
        if (carServiceInter.save(car)) {
            return new ResponseEntity(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Modify car  ")
    @PutMapping("/modify/{id}")
    public ResponseEntity<EntityModel<Car>> modifyCar(@PathVariable Long id, @Valid @RequestBody Car modifyCar) {
        Link link = linkTo(CarApi.class).slash(id).withSelfRel();
        if (carServiceInter.changeCar(id, modifyCar)) {
            EntityModel<Car> carResource = new EntityModel(carServiceInter.changeCar(id, modifyCar), link);
            return new ResponseEntity<>(carResource, HttpStatus.CREATED);
        } else if (!carServiceInter.changeCar(id, modifyCar)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Change car color")
    @PatchMapping("/{carId}/color/{newColor}")
    public ResponseEntity<Car> updateColor(@PathVariable Long carId, @PathVariable @NotNull String newColor) {
        boolean changeColor = carServiceInter.changeColor(carId, convertColor.convertToEnum(newColor));
        if (changeColor) {
            return new ResponseEntity(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Change car brand")
    @PatchMapping("/{id}/mark/{newMark}")
    public ResponseEntity<EntityModel<Car>> updateMark(@PathVariable Long id, @PathVariable String newMark) {
        Link link = linkTo(CarApi.class).slash(id).withSelfRel();
        boolean changedMark = carServiceInter.changeMark(id, newMark);
        if (changedMark) {
            EntityModel<Car> carResource = new EntityModel(changedMark, link);
            return new ResponseEntity<>(carResource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "delete car by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteById(@PathVariable @NotNull Long id) {
        boolean remove = carServiceInter.removeById(id);
        if (remove) {
            return new ResponseEntity(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

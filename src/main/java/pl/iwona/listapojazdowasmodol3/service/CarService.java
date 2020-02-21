package pl.iwona.listapojazdowasmodol3.service;


import org.springframework.stereotype.Service;
import pl.iwona.listapojazdowasmodol3.exception.CarNotExist;
import pl.iwona.listapojazdowasmodol3.exception.ColorNotFound;
import pl.iwona.listapojazdowasmodol3.model.Car;
import pl.iwona.listapojazdowasmodol3.model.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService implements CarServiceInter {

    private List<Car> cars;

    public CarService() {
        this.cars = new ArrayList<>();
        cars.add(new Car(1L, "Ferrari", "599 GTB Fiorano", Color.RED));
        cars.add(new Car(2L, "Audi", "A6", Color.NAVY_BLUE));
        cars.add(new Car(3L, "Aston Martin", "DB5", Color.RED));
    }

    @Override
    public List<Car> getAll() {
        return cars;
    }

    public List<Car> getCars() {
        return cars;
    }


    @Override  //get
    public Optional<Car> carById(Long carId) {
        return cars.stream().filter(car -> car.getCarId() == carId).findFirst();
//        if(findCarById.isPresent()){
//             findCarById.get();
//        }
//        throw new CarNotExist(carId); / tez nie dzial
    }

    @Override //get by color
    public List<Car> carByColor(String color) {
        return getCars().stream().filter(car -> color.equalsIgnoreCase(car.getColor().name()))
                .collect(Collectors.toList());
    }

    @Override //post
    public boolean save(Car car) {
        return cars.add(car);
    }

    @Override //put
    public boolean changeCar(Long carId, Car changedCar) {
        Optional<Car> findCar = cars.stream().filter(car -> car.getCarId() == changedCar.getCarId()).findFirst();
        if (findCar.isPresent()) {
            Car car = findCar.get();
            car.setMark(changedCar.getMark());
            car.setModel(changedCar.getModel());
            car.setColor(changedCar.getColor());
        } else {
            changedCar.setCarId(carId);
            cars.add(changedCar);
        }
        return false;
    }

//        if (findCar.isPresent()) {
//            findCar.map(element -> {
//                element.setModel(updCar.getModel());
//                element.setMark(updCar.getMark());
//                element.setColor(updCar.getColor());
//                return element;
//            });
//            findCar.orElseGet(() -> {
//                updCar.setId(id);
//
//                boolean save = save(updCar);
//                return (Car) save(updCar);
//            });
//        }
//        return false;
//    }

    @Override //    patch
    public boolean changeColor(Long carId, Color color) {
        Optional<Car> first = cars.stream().filter(car -> car.getCarId() == carId).findFirst();
        if (first.isPresent()) {
            Car carColor = first.get();
            carColor.setColor(color);
        }
        throw new ColorNotFound(String.format("Invalid enum type of enum Color: %s", color));
    }

    @Override
    public boolean changeMark(Long id, String newMark) {
        Optional<Car> findMark = cars.stream().filter(car -> car.getCarId() == id).findFirst();
        if (findMark.isPresent()) {
            Car carModel = findMark.get();
            carModel.setMark(newMark);
            return true;
        }
        return false;
    }

    @Override //delete
    public boolean removeById(Long carId) {
        Optional<Car> first = cars.stream().filter(car -> car.getCarId() == carId).findFirst();
        if (first.isPresent()) {
            cars.remove(first.get());
            return true;
        }
        throw new CarNotExist(carId); // nie wyzuca wyjatku ale dlaczego?
    }


//    public boolean removeByID(Long id) {
//        Optional<Car> first = cars.stream().filter(car -> car.getId() == id).findFirst();
//     return first.map(car -> cars.remove(car)).orElseThrow(() -> new CarNotExist(id));
//    } tutak wyzyca blad 500
}

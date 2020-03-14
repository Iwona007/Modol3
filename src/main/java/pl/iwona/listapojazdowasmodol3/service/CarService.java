package pl.iwona.listapojazdowasmodol3.service;


import org.springframework.stereotype.Service;
import pl.iwona.listapojazdowasmodol3.exception.CarNotExist;
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

    @Override  //get
    public Optional<Car> carById(Long carId) {
        Optional<Car> findCarById = cars.stream().filter(car -> car.getCarId() == carId).findFirst();
        findCarById.orElseThrow(() -> new CarNotExist(carId));
        return findCarById;
    }

    @Override //get by color
    public List<Car> carByColor(String color) {
        return getAll().stream().filter(car -> color.equalsIgnoreCase(car.getColor().name()))
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
            return true;
        }
        return false;
    }

    @Override
    public boolean changeMark(Long id, String newMark) {
        Optional<Car> findMark = cars.stream().filter(car -> car.getCarId() == id).findFirst();
        if (findMark.isPresent()) {
            Car carModel = findMark.get();
            carModel.setMark(newMark);
            return true;
        }
        throw new CarNotExist(id);
    }

    @Override //delete
    public boolean removeById(Long carId) {
        Optional<Car> first = cars.stream().filter(car -> car.getCarId() == carId).findFirst();
        if (first.isPresent()) {
            cars.remove(first.get());
            return true;
        }
        throw new CarNotExist(carId);
    }
}

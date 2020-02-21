package pl.iwona.listapojazdowasmodol3.service;

import pl.iwona.listapojazdowasmodol3.model.Car;
import pl.iwona.listapojazdowasmodol3.model.Color;

import java.util.List;
import java.util.Optional;

public interface CarServiceInter {

    List<Car> getAll();

    Optional<Car> carById(Long carId);

    List<Car> carByColor(String color);

    boolean save(Car car);

    boolean changeCar(Long carId, Car changedCar);

    boolean changeColor(Long carId, Color color);

    boolean changeMark(Long id, String newMark);

    boolean removeById(Long carId);
}

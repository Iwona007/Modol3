package pl.iwona.listapojazdowasmodol3.model;

import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;

public class Car extends ResourceSupport {
    private Long carId;
    private String mark;
    private String model;
    private Color color;

    public Car() {
    }

    public Car(Long carId, String mark, String model, Color color) {
        this.carId = carId;
        this.mark = mark;
        this.model = model;
        this.color = color;
    }

     public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            Car car = (Car) obj;
            return Objects.equals(carId, car.carId);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + carId+
                ", mark='" + mark + '\'' +
                ", model='" + model + '\'' +
                ", color=" + color +
                '}';
    }
}

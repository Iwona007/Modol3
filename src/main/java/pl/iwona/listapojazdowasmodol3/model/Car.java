package pl.iwona.listapojazdowasmodol3.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;

public class Car extends RepresentationModel {

    @NotNull
    private Long carId;
    @NotBlank
    private String mark;
    @NotBlank
    private String model;
    @NotNull
    @JsonEnumDefaultValue
//    @Pattern(regexp ="^RED$|^BLUE$|^NAVY_BLUE$|^SILVER$")
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
                "id=" + carId +
                ", mark='" + mark + '\'' +
                ", model='" + model + '\'' +
                ", color=" + color +
                '}';
    }
}

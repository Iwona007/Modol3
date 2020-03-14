package pl.iwona.listapojazdowasmodol3.converColor;

import org.springframework.stereotype.Component;
import pl.iwona.listapojazdowasmodol3.exception.ColorNotFound;
import pl.iwona.listapojazdowasmodol3.model.Color;

import java.util.EnumSet;

@Component
public class ConvertColor {

    public Color convertToEnum(String color) {
        return EnumSet.allOf(Color.class).stream()
                .filter(color1 -> color1.name().equalsIgnoreCase(color))
                .findAny()
                .orElseThrow(() -> new ColorNotFound(color));
    }

}

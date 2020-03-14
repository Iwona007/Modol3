package pl.iwona.listapojazdowasmodol3.exception;

import pl.iwona.listapojazdowasmodol3.model.Color;

public class ColorNotFound extends RuntimeException {

    public ColorNotFound(String color) {
        super(String.format("Invalid enum type of enum %s", color));

    }
}

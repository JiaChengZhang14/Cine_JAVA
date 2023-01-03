package models;

import enums.ESTADOS;

public class Butaca {
    private static ESTADOS estados;

    @Override
    public String toString() {
        return switch(new Butaca()){
            ESTADOS.LIBRE: System.out.println("💺");
            Butaca(estados = ESTADOS.RESERVADO) -> "❌"
            Butaca(estados = ESTADOS.OCUPADO) -> "🍿"

        }
    }
}

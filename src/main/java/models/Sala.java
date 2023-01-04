package models;

import enums.ESTADOS;

import java.util.Scanner;

import static java.lang.Integer.parseInt;

public record Sala(String nombre) {
    static Butaca FREE_SEAT = new Butaca(ESTADOS.LIBRE);
    static Butaca RESERVED_SEAT = new Butaca(ESTADOS.RESERVADO);
    static Butaca SOLD_SEAT = new Butaca(ESTADOS.OCUPADO);


    static Double totalCash = 0.0;
    static int soldSeatsCount = 0;
    static int reservedSeatsCount = 0;
    static Scanner sc = new Scanner(System.in);

    public static void generateReport(String roomName) {
        System.out.println("ESTE ES EL INFORME DEL CINE");
        System.out.println(" - La película reproducida es ${Pelicula()}");
        System.out.println(" - Está siendo reproducida en la sala ${Sala(nombre = roomName)}");
        System.out.println(" - El dinero total recolectado es de $totalCash €");
        System.out.println(" - En este momento hay un total de $reservedSeatsCount asientos reservados y un total de $soldSeatsCount asientos vendidos");
    }

    public static int selectOption() {

        System.out.println("***¡¡BIENVENIDO AL CINE!!***");
        System.out.println("Seleccione la opción que necesite");
        System.out.println();
        System.out.println("1 -> RESERVAR ASIENTO (PALOMITAS DE REGALO)");
        System.out.println("2 -> FORMALIZAR RESERVA (RESERVA REQUERIDA)");
        System.out.println("3 -> CANCELAR RESERVA (RESERVA REQUERIDA)");
        System.out.println("4 -> COMPRAR ASIENTO");
        System.out.println("5 -> GENERAR INFORME DEL CINE");
        System.out.println("6 -> SALIR");
        String option;
        do {
            System.out.println("Opción seleccionada: ");
            option = sc.nextLine();
            if (parseInt(option) > 6 || parseInt(option) < 1) {
                System.out.println("Opción no valida");
            } else {
                return parseInt(option);
            }
        } while (parseInt(option) > 6 || parseInt(option) < 1);
        return 0;
    }

    public static void processPucharse(String soldSeat, seatsMatrix:Array<Array<Butaca?>>) {
        String[] pucharsedSeat = soldSeat.split(":");
        String rowLetter = pucharsedSeat[0];
        int processedRow = rowLetterToNumber(rowLetter);
        String selectedColumn = pucharsedSeat[1];
        changeSeatStatusToOccupied(seatsMatrix, selectedColumn, processedRow);
    }

    public static String buySeat(seatsMatrix:Array<Array<Butaca?>>, int row, int column) {
        System.out.println();
        var soldSeat = "";
        val regex = """[A-Z][:][0-9]+""".toRegex();

        System.out.println("Hola! Bienvenido al cine! Estos son los asientos disponibles (Los que aparecen con una L) ");
        printSeats(seatsMatrix);
        do {

            do {
                soldSeat = sc.nextLine();
                if (!regex.matches(soldSeat.toUpperCase())) {
                    System.out.println("Debes el introducir la LETRA de la fila y el NÚMERO de la columna. Ejemplo: A:1 ");
                }
                if (regex.matches(soldSeat.toUpperCase())) {
                    if (!doesColumnExist(soldSeat, column)) {
                        System.out.println("La columna que has elegido no existe, elige otro asiento.");
                    }
                }
                if (regex.matches(soldSeat.toUpperCase())) {
                    if (!doesRowExist(soldSeat, row)) {
                        System.out.println("La fila que has elegido no existe, elige otro asiento.");
                    }
                }

            } while (!regex.matches(soldSeat.toUpperCase()) || !doesColumnExist(soldSeat, column));


            if (isSeatReserved(soldSeat, seatsMatrix)) {
                System.out.println("El asiento que has elegido, ha sido reservado anteriormente, elige otro: ");
            }

        } while (isSeatReserved(soldSeat, seatsMatrix));
        System.out.println("El asiento ha sido comprado correctamente");
        System.out.println("Se te ha hecho el cobre de 5.25€ automáticamente.");
        totalCash += 5.25;
        soldSeatsCount++;
        return soldSeat;
    }

    public static void processFormalization(String formalizedReservation, seatsMatrix:Array<Array<Butaca?>>) {
        String[] processedFormalization = formalizedReservation.split(":");
        String selectedRow = processedFormalization[0];
        String selectedColumn = processedFormalization[1];
        int processedRow = rowLetterToNumber(selectedRow);


        changeSeatStatusToOccupied(seatsMatrix, selectedColumn, processedRow);
        printSeats(seatsMatrix);
    }

    public static void changeSeatStatusToOccupied(seatsMatrix:Array<Array<Butaca?>>, String selectedColumn, int processedRow) {
        seatsMatrix[processedRow][selectedColumn.toInt() - 1] = SOLD_SEAT;

    }


    public static String formalizeReservation(seatsMatrix:Array<Array<Butaca?>>, int column, int row) {
        System.out.println();
        var toBeFormalizedReservation = "";
        val regex = """[A-Z][:][0-9]+""".toRegex();

        System.out.println("Introduce el asiento que has reservado, para que podamos formalizar la reserva por usted y finalizar la compra de esta: ");
        do {

            do {
                toBeFormalizedReservation = String.valueOf(sc);
                if (!regex.matches(toBeFormalizedReservation.toUpperCase())) {
                    System.out.println("Debes el introducir la LETRA de la fila y el NÚMERO de la columna. Ejemplo: A:1 ");
                }
                if (regex.matches(toBeFormalizedReservation.toUpperCase())) {
                    if (!doesColumnExist(toBeFormalizedReservation, column)) {
                        System.out.println("La columna que has elegido no existe, elige otro asiento.");
                    }
                }
                if (regex.matches(toBeFormalizedReservation.toUpperCase())) {
                    if (!doesRowExist(toBeFormalizedReservation, row)) {
                        System.out.println("La fila que has elegido no existe, elige otro asiento.");
                    }
                }

            } while (!regex.matches(toBeFormalizedReservation.toUpperCase()) || !doesColumnExist(toBeFormalizedReservation, column))

            // Desde aquí hacia arriba, nos aseguramos de que el asiento que el usuario ha elegido está escrito de la manera que queremos y que está dentro de los límites de la matriz de asientos.
            if (!isSeatReserved(toBeFormalizedReservation, seatsMatrix)) {
                System.out.println("El asiento que has elegido, no ha sido reservado anteriormente, elige otro: ");
            }

        } while (!isSeatReserved(toBeFormalizedReservation, seatsMatrix));
        System.out.println("Has formalizado la reserva correctamente! Ya se te ha hecho el cobro de los 4€ restantes. Muchas gracias! ");
        totalCash += 4;
        reservedSeatsCount--;
        soldSeatsCount++;
        return toBeFormalizedReservation;

    }


    public static void processCancellation(toBeCancelledSeat:String, seatsMatrix:Array<Array<Butaca?>>) {
        val processedCancellation = toBeCancelledSeat.split(":").toTypedArray();
        val selectedRow = processedCancellation[0];
        val selectedColumn = processedCancellation[1];
        val processedRow = rowLetterToNumber(selectedRow);


        changeSeatStatusToFree(seatsMatrix, selectedColumn, processedRow);
        printSeats(seatsMatrix);
    }

    public static void changeSeatStatusToFree(seatsMatrix:Array<Array<Butaca?>>, String selectedColumn ,int processedRow):Array<Array<Butaca?>>

    {

        seatsMatrix[processedRow][selectedColumn.toInt() - 1] = FREE_SEAT;
        return seatsMatrix;
    }

    public static String cancelReservation(seatsMatrix:Array<Array<Butaca?>>,int column, int row) {
        System.out.println();
        var toBeCancelledSeat = "";
        val regex = """[A-Z][:][0-9]+""".toRegex();

        System.out.println("Introduce el asiento que has reservado, para que podamos cancelar la reserva por usted: ");
        do {

            do {
                toBeCancelledSeat = sc.nextLine();
                if (!regex.matches(toBeCancelledSeat.toUpperCase())) {
                    System.out.println("Debes el introducir la LETRA de la fila y el NÚMERO de la columna. Ejemplo: A:1 ");
                }
                if (regex.matches(toBeCancelledSeat.toUpperCase())) {
                    if (!doesColumnExist(toBeCancelledSeat, column)) {
                        System.out.println("La columna que has elegido no existe, elige otro asiento.");
                    }
                }
                if (regex.matches(toBeCancelledSeat.toUpperCase())) {
                    if (!doesRowExist(toBeCancelledSeat, row)) {
                        System.out.println("La fila que has elegido no existe, elige otro asiento.");
                    }
                }

            } while (!regex.matches(toBeCancelledSeat.toUpperCase()) || !doesColumnExist(toBeCancelledSeat, column));

            // Desde aquí hacia arriba, nos aseguramos de que el asiento que el usuario ha elegido está escrito de la manera que queremos y que está dentro de los límites de la matriz de asientos.
            if (!isSeatReserved(toBeCancelledSeat, seatsMatrix)) {
                System.out.println("El asiento que has elegido, no ha sido reservado anteriormente, elige otro: ");
            }

        } while (!isSeatReserved(toBeCancelledSeat, seatsMatrix));
        System.out.println("Has cancelado la reserva correctamente");
        totalCash -= 1.25;
        System.out.println("Se te ha devuelto 1.25€");
        reservedSeatsCount--;
        return toBeCancelledSeat;
    }

    public static boolean isSeatReserved(String toBeCancelledSeat, seatsMatrix:Array<Array<Butaca?>>) {
        String[] aux = toBeCancelledSeat.split(":");
        String auxRow = aux[0];
        int processedRow = rowLetterToNumber(auxRow);
        String auxColumn = aux[1];
        return seatsMatrix[processedRow][parseInt(auxColumn) - 1] == RESERVED_SEAT;

    }


    public static void processReservation(String reservation, seatsMatrix:Array<Array<Butaca?>>) {
        val processedReservation = reservation.split(":").toTypedArray();
        val selectedRow = processedReservation[0];
        val selectedColumn = processedReservation[1];
        val processedRow = rowLetterToNumber(selectedRow);
        changeSeatStatusToReserved(seatsMatrix, selectedColumn, processedRow);
        printSeats(seatsMatrix);


    }

    public static void changeSeatStatusToReserved(seatsMatrix:Array<Array<Butaca?>>, String selectedColumn, int processedRow):Array<Array<Butaca?>>

    {

        seatsMatrix[processedRow][selectedColumn.toInt() - 1] = RESERVED_SEAT;
        return seatsMatrix;
    }

    public static int rowLetterToNumber(String selectedRow) {
        return switch (selectedRow.toUpperCase()) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            case "E" -> 4;
            case "F" -> 5;
            case "G" -> 6;
            case "H" -> 7;
            case "I" -> 8;
            case "J" -> 9;
            case "K" -> 10;
            case "L" -> 11;
            case "M" -> 12;
            case "N" -> 13;
            case "O" -> 14;
            case "P" -> 15;
            case "Q" -> 16;
            case "R" -> 17;
            case "S" -> 18;
            case "T" -> 19;
            case "U" -> 20;
            case "V" -> 21;
            case "W" -> 22;
            case "X" -> 23;
            case "Y" -> 24;
            case "Z" -> 25;
            default -> -1;
        };
    }

    public static String reverseSeat(seatsMatrix:Array<Array<Butaca?>>, int column, int row) {
        System.out.println();
        var reservedSeat = "";
        val regex = """[A-Z][:][0-9]+""".toRegex();
        printSeats(seatsMatrix);
        System.out.println("Hola! ¿Que asiento quieres reservar? Estas son las butacas disponibles.");
        System.out.println("Las filas se ordenan con las letras del abecedario y las columnas con números, un ejemplo de asiento seria B:4");
        System.out.println("que representaría el asiento de la segunda fila y la cuarta columna");
        System.out.println("Introduce el asiento: ");

        do {
            reservedSeat = sc.nextLine();
            if (!regex.matches(reservedSeat.toUpperCase())) {
                System.out.println("Debes el introducir la LETRA de la fila y el NÚMERO de la columna. Ejemplo: A:1 ")
            }
            if (regex.matches(reservedSeat.toUpperCase())) {
                if (!doesColumnExist(reservedSeat, column)) {
                    System.out.println("La columna que has elegido no existe, elige otro asiento.");
                }
            }
            if (regex.matches(reservedSeat.toUpperCase())) {
                if (!doesRowExist(reservedSeat, row)) {
                    System.out.println("La fila que has elegido no existe, elige otro asiento.");
                }
            }

        } while (!regex.matches(reservedSeat.toUpperCase()) || !doesColumnExist(reservedSeat, column))
        totalCash += 1.25;
        reservedSeatsCount++;
        System.out.println("El precio de la reserva es de 1.25€, cuando formalice la reserva se le cobrarán 4 euros para completar el precio de la entrada que es de 5.25")
        System.out.println("Se te ha cobrado $totalCash");
        return reservedSeat;
    }

    public static boolean doesRowExist(String reservedSeat,int row) {
        String[] filteredRow = reservedSeat.split(":");
        int rowNumber = rowLetterToNumber(filteredRow[0]);
        return rowNumber <= row;
    }

    public static boolean doesColumnExist(String reservedSeat, int  column){
        String[] filteredColumn = reservedSeat.split(":");
        return parseInt(filteredColumn[1]) <= column;

    }


    public static void placeSeats(seatsMatrix:Array<Array<Butaca?>>) {
        for (i in seatsMatrix.indices) {
            for (j in seatsMatrix[i].indices) {
                seatsMatrix[i][j] = Butaca();
            }
        }
    }

    public static void printSeats(seatsMatrix:Array<Array<Butaca?>>) {
        for (i in seatsMatrix.indices) {
            System.out.println(seatsMatrix[i].contentToString());
        }

    }

    public static String requestRoomName() {
        System.out.println("Introduce el nombre de la sala de cine:");
        var roomName = "";
        roomName = sc.nextLine();
        return roomName;
    }


    public static int requestColumnSize() {
        val regexColumn = """\d+""".toRegex();
        val minColumnSize = 1;
        var column = "";
        do {
            System.out.println("¿Cuántas columnas de butacas quieres?");
            column = sc.nextLine();
            if (!regexColumn.matches(column) || column.toInt() < minColumnSize) {
                System.out.println("¡EL VALOR INTRODUCIDO DEBER SER UN NUMERO ENTERO");
            }
        } while (!regexColumn.matches(column) || column.toInt() < minColumnSize);
        return column.toInt();
    }

    public static int requestRowSize():

    Int {
        val regexRow = """\d+""".toRegex();
        /*
        Vamos a nombrar las butacas con letras en la fila, por lo que el máximo valor permitido en la fila es de 26
        por ejemplo, el asiento A:1 sería la posición 1:1 en la matriz de las butacas.
         */
        val maxRowSize = 26;
        val minRowSize = 1;
        var fila = "";
        do {
            System.out.println("¿Cuántas filas de butacas quieres?");
            fila = sc.nextLine();
            if (!regexRow.matches(fila) || fila.toInt() > maxRowSize || fila.toInt() < minRowSize) {
                System.out.println("¡EL VALOR INTRODUCIDO DEBER SER UN NUMERO ENTERO ENTRE 1 Y 26!");
            }
        } while (!regexRow.matches(fila) || fila.toInt() > maxRowSize || fila.toInt() < minRowSize);
        return fila.toInt();

    }

}
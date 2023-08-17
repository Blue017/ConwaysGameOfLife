import java.util.Random;

/**
 * Simulación del juego de la vida de Conway.
 * Proyecto final para la clase Programación I en Jala University
 */
public class GameOfLife {

    /**
     * Metodo principal encargado de la asignación de valores y llamado a la ejecución del juego
     * @param args Argumentos tomados desde la linea de comandos
     */
    public static void main(String[] args){
        int WIDTH = 0;
        int HEIGHT = 0;
        int SPEED = 0;
        int GENERATIONS = 0;
        boolean run;
        String pattern = "RND";

        for (String arg : args){
            String[] keyValue = arg.split("=");
            if (keyValue.length == 2){
                String key = keyValue[0];
                String value = keyValue[1];

                switch (key) {
                    case "w" -> WIDTH = Integer.parseInt(value);
                    case "h" -> HEIGHT = Integer.parseInt(value);
                    case "s" -> SPEED = Integer.parseInt(value);
                    case "g" -> GENERATIONS = Integer.parseInt(value);
                    case "p" -> pattern = value;
                    default -> {
                        System.out.println("Clave desconocida" + key);
                        return;
                    }
                }
            }
        }
        run = (validateValues(WIDTH, HEIGHT, SPEED, GENERATIONS, pattern));
        boolean[][] board = createBoard(WIDTH, HEIGHT);
        fillBoard(board, pattern);
        runGame(board, GENERATIONS, SPEED, run);
    }

    /**
     * Metodo encargado de la ejecución del juego, itera hasta cumplir el numero de generaciones
     * @param board Tablero con el arreglo de celdas booleanas
     * @param GENERATIONS Numero de repeticiones que se debe alcanzar (0 se repetirá indefinidamente)
     * @param SPEED Velocidad en milisegundos que se detendrá entre la impresión de un tablero y otro
     * @param run Indicador booleano que controla el inicio y final del juego
     */
    private static void runGame(boolean[][] board, int GENERATIONS, int SPEED, boolean run){
        int gen = 1;
        while (run) {
            System.out.println("Generación: " + gen);
            printBoard(board);
            board = updateBoard(board);
            gen++;

            try {
                Thread.sleep(SPEED);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            if(GENERATIONS > 0 && gen > GENERATIONS){
                run = false;
            }
            if (!run){
                break;
            }
        }
    }

    /**
     * Metodo encargado de crear el primer tablero del tamaño especificado por linea de comandos
     * @param width Ancho del tablero (O numero de columnas)
     * @param height Altura del tablero (O numero de filas)
     * @return Regresa el tablero creado
     */
    private static boolean[][] createBoard(int width, int height) {
        return new boolean[height][width];
    }

    /**
     * Metodo encargado de llenar el tablero, puede ser con valores aleatorios o siguiendo un patrón indicado por linea de comandos
     * @param board Tablero que será llenado
     * @param pattern Patrón para llenar el tablero ("RND" para llenar de manera aleatoria)
     */
    private static void fillBoard(boolean[][] board, String pattern){
        Random random = new Random();
        if (pattern.equals("RND")){
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[0].length; col++) {
                    board[row][col] = random.nextBoolean();
                }
            }
        } else {
            String[] rows = pattern.split("#");
            for (int row = 0; row < rows.length; row++){
                String rowPattern = rows[row];
                for (int col = 0; col < rowPattern.length(); col++){
                    if (row < board.length && col < board[0].length) {
                        char cellChar = rowPattern.charAt(col);
                        board[row][col] = cellChar == '1';
                    } else {
                        System.out.println("El patrón ingresado no cumple con los requerimientos de tamaño");
                        return;
                    }
                }
            }
        }
    }

    /**
     * Metodo encargado de imprimir el tablero en pantalla
     * @param board Tablero con los valores a imprimir
     */
    public static void printBoard(boolean[][] board){
        for (boolean[] row : board) {
            for (boolean cell : row) {
                char cellChar = cell ?  '0' : 'X';
                System.out.print(cellChar + " ");
            }
            System.out.println();
        }
    }

    /**
     * Metodo encargado de actualizar el tablero con las reglas del GOL
     * @param board Tablero con los valores usados como referencia para aplicar las reglas
     * @return Regresa el tablero actualizado
     */
    private static boolean[][] updateBoard(boolean[][] board) {
        int rows = board.length;
        int cols = board[0].length;
        boolean[][] newBoard = new boolean[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int liveNeighbors = countLiveNeighbors(board, row, col);

                if (board[row][col]) {
                    newBoard[row][col] = liveNeighbors == 2 || liveNeighbors == 3;
                } else {
                    newBoard[row][col] = liveNeighbors == 3;
                }
            }
        }

        return newBoard;
    }

    /**
     * Metodo encargado de contar el numero de vecinos vivos de cada celula
     * @param board Tablero con valores booleanos usado para contar
     * @param row Fila de la celula que está llamando el metodo y que servirá de epicentro para el conteo
     * @param col Columna de la celula que está llamando el metodo y que servirá de epicentro para el contro
     * @return Regresa el numero de celulas vivas contadas
     */
    private static int countLiveNeighbors(boolean[][] board, int row, int col) {
        int count = 0;
        int rows = board.length;
        int cols = board[0].length;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0){
                    continue;
                }
                int neighborRow = row + x;
                int neighborCol = col + z;

                if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols) {
                    if (board[neighborRow][neighborCol] && !(x == 0 && z == 0)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * Metodo encargado de validar los datos ingresados por linea de comandos
     * @param WIDTH Ancho del tablero, valor entero que debe ser 10, 20, 40 u 80
     * @param HEIGHT Altura del tablero, valor entero que debe ser 10, 20 o 40
     * @param SPEED Velocidad en milisegundos que se esperará entre impresiones, valor entero que debe estar entre 250 y 1000
     * @param GENERATIONS Numero de generaciones que se imprimirá el tablero, valor entero que no puede ser negativo
     * @param pattern Patrón inicial para el llenado del tablero, cadena de caracteres que puede ser "RND" o un codigo utilizando los valores 0, 1 y #, el ancho y alto del patrón no pueden exceder el tamaño del tablero
     * @return Regresa el valor booleano indicador de que se cumplen o no las validaciones
     */
    private static boolean validateValues(int WIDTH, int HEIGHT, int SPEED, int GENERATIONS, String pattern){
        if (!(WIDTH == 10 || WIDTH == 20 || WIDTH == 40 || WIDTH == 80)){
            System.out.println("Valor ancho no valido");
            return false;
        }
        if (!(HEIGHT == 10 || HEIGHT == 20 || HEIGHT == 40)){
            System.out.println("Valor de altura no valido");
            return false;
        }
        if (SPEED < 250 || SPEED > 1000){
            System.out.println("Valor de velocidad no valido");
            return false;
        }
        if (GENERATIONS < 0){
            System.out.println("Valor de generaciones no valido");
            return false;
        }
        if (!(pattern.equals("RND") || pattern.matches("[01#]+"))){
            System.out.println("Valores de patrón invalidos");
            return false;
        }
        if (!pattern.equals("RND")) {
            String[] rows = pattern.split("#");
            int numRows = rows.length;

            if (numRows > HEIGHT){
                return false;
            }

            for (String row : rows) {
                if (row.length() > WIDTH) {
                    return false;
                }
            }
        }
        return true;
    }
}
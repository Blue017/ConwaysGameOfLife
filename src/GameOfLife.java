import java.util.Random;

public class GameOfLife {
    public static void main(String[] args){
        int WIDTH = 0;
        int HEIGHT = 0;
        int SPEED = 0;
        int GENERATIONS = 0;
        String pattern = "RND";

        for (String arg:args){
            String[] keyValue = arg.split("=");
            if (keyValue.length == 2){
                String key = keyValue[0];
                String value = keyValue[1];

                switch (key) {
                    case "w" -> WIDTH = Integer.parseInt(value);
                    case "h" -> HEIGHT = Integer.parseInt(value);
                    case "s" -> SPEED = Integer.parseInt(value);
                    case "g" -> GENERATIONS = Integer.parseInt(value);
                    case "p" -> pattern = arg.substring(2);
                    default -> {
                        System.out.println("Clave desconocida" + key);
                        return;
                    }
                }
            }
        }
        validateValues(WIDTH, HEIGHT, SPEED, GENERATIONS, pattern);
        boolean[][] board = createBoard(WIDTH, HEIGHT);
        fillBoard(board, pattern);
        runGame(board, GENERATIONS, SPEED);
    }

    private static void runGame(boolean[][] board, int GENERATIONS, int SPEED){
        int gen = 1;
        boolean run = true;
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
        }
    }

    private static boolean[][] createBoard(int width, int height) {
        return new boolean[height][width];
    }

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
            int numRows = rows.length;
            int numCols = rows[0].length();

            if (numRows > board.length || numCols > board[0].length){
                System.out.println("El patrón ingresado no cumple con los requerimientos de tamaño");
                return;
            }

            for (int row = 0; row < numRows; row++){
                for (int col = 0; col < numCols; col++){
                    char cellChar = rows[row].charAt(col);
                    if (cellChar == '1') board[row][col] = true;
                    else if (cellChar == '0') board[row][col] = false;
                    else {
                        System.out.println("El patrón contiene valores invalidos");
                        return;
                    }
                }
            }
        }
    }

    public static void printBoard(boolean[][] board){
        for (boolean[] row : board) {
            for (boolean cell : row) {
                char cellChar = cell ?  '0' : 'X';
                System.out.print(cellChar + " ");
            }
            System.out.println();
        }
    }

    private static boolean[][] updateBoard(boolean[][] board) {
        int rows = board.length;
        int cols = board[0].length;
        boolean[][] newBoard = new boolean[rows][cols];

        //Reglas:
        //Regla 1: Viva con < 2 vecinos vivos muere
        //Regla 2: Viva con > 4 vecinos vivos muere
        //Regla 3: Viva con 3 vecinos vivos sobrevive (No hace nada)
        //Regla 4: Muerta con 3 vecinos vivos nace

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int liveNeighbors = countLiveNeighbors(board, row, col);

                if (board[row][col]) {
                    // Reglas 1 y 2
                    newBoard[row][col] = liveNeighbors == 2 || liveNeighbors == 3;
                } else {
                    // Regla 4
                    newBoard[row][col] = liveNeighbors == 3;
                }
            }
        }

        return newBoard;
    }

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

    private static void validateValues(int WIDTH, int HEIGHT, int SPEED, int GENERATIONS, String pattern){
        if (!(WIDTH == 10 || WIDTH == 20 || WIDTH == 40 || WIDTH == 80)){
            System.out.println("Valor ancho no valido");
            return;
        }
        if (!(HEIGHT == 10 || HEIGHT == 20 || HEIGHT == 40)){
            System.out.println("Valor de altura no valido");
            return;
        }
        if (SPEED < 250 || SPEED > 1000){
            System.out.println("Valor de velocidad no valido");
            return;
        }
        if (GENERATIONS < 0){
            System.out.println("Valor de generaciones no valido");
            return;
        }
        if (!(pattern.equals("RND") || pattern.matches("[01#]+"))){
            System.out.println("Valores de patrón invalidos");
        }
    }
}
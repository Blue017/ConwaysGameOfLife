import java.util.Random;

public class GameOfLife {
    public static void main(String[] args){
        int WIDTH = 0;
        int HEIGHT = 0;
        int SPEED = 0;
        int GENERATIONS = 0;
        String pattern = "RND";
        boolean run = false;

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
        if (validateValues(WIDTH, HEIGHT, SPEED, GENERATIONS, pattern)) run = true;
        //if (validateValues(WIDTH, HEIGHT, SPEED, GENERATIONS, pattern))System.out.println(pattern);
        boolean[][] board = createBoard(WIDTH, HEIGHT);
        if (pattern.equals("RND")){
            fillRandomly(board);
        }
        printBoard(board);
    }

    private static boolean[][] createBoard(int width, int height) {
        return new boolean[height][width];
    }

    private static void fillRandomly(boolean[][] board){
        Random random = new Random();
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[0].length; col++){
                board[row][col] = random.nextBoolean();
            }
        }
    }

    public static void printBoard(boolean[][] board){
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[0].length; col++){
                char cellChar = board[row][col] ? '0' : 'X';
                System.out.print(cellChar + " ");
            }
            System.out.println();
        }
    }

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
        return true;
    }
}
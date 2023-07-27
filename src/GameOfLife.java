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

                switch (key){
                    case "w":
                        WIDTH = Integer.parseInt(value);
                        if (!(WIDTH == 10 || WIDTH == 20 || WIDTH == 40 || WIDTH == 80)){
                            System.out.println("El ancho no es valido");
                            return;
                        }
                        break;
                    case "h":
                        HEIGHT = Integer.parseInt(value);
                        if (!(HEIGHT == 10 || HEIGHT == 20 || HEIGHT == 40)){
                            System.out.println("La altura no es valida");
                            return;
                        }
                        break;
                    case "s":
                        SPEED = Integer.parseInt(value);
                        if (SPEED < 250 || SPEED > 1000){
                            System.out.println("La velocidad no es valida");
                            return;
                        }
                        break;
                    case "g":
                        GENERATIONS = Integer.parseInt(value);
                        if (GENERATIONS < 0){
                            System.out.println("Numero de generaciones no valido");
                        }
                        break;
                    case "p":
                        break;
                    default:
                        System.out.println("Clave desconocida" + key);
                        return;
                }
            }
        }
    }
}
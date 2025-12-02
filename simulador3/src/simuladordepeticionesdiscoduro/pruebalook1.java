//
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class pruebalook1{
    public static List<Integer> lookAlgorithm(List<Integer> requests, int head, String direction) {
        // Ordenar las solicitudes
        Collections.sort(requests);
        List<Integer> seekSequence = new ArrayList<>();

        // Si la dirección es a la derecha
        if (direction.equals("right")) {
            // Mover a la derecha
            for (int request : requests) {
                if (request >= head) {
                    seekSequence.add(request);
                }
            }

            // Mover a la izquierda
            for (int i = requests.size() - 1; i >= 0; i--) {
                if (requests.get(i) < head) {
                    seekSequence.add(requests.get(i));
                }
            }
        } 
        // Si la dirección es a la izquierda
        else if (direction.equals("left")) {
            // Mover a la izquierda
            for (int i = requests.size() - 1; i >= 0; i--) {
                if (requests.get(i) <= head) {
                    seekSequence.add(requests.get(i));
                }
            }

            // Mover a la derecha
            for (int request : requests) {
                if (request > head) {
                    seekSequence.add(request);
                }
            }
        }

        return seekSequence;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Solicitar la posición inicial de la cabeza
        System.out.print("Ingrese la posición inicial de la cabeza del disco: ");
        int head = scanner.nextInt();

        // Solicitar la dirección de movimiento
        System.out.print("Ingrese la dirección de movimiento (right o left): ");
        String direction = scanner.next();

        // Solicitar la lista de solicitudes
        System.out.print("Ingrese las solicitudes de cilindros (separadas por espacios): ");
        scanner.nextLine();  // Limpiar el buffer
        String input = scanner.nextLine();
        
        // Dividir la entrada en una lista de enteros
        String[] tokens = input.split(" ");
        List<Integer> requests = new ArrayList<>();
        for (String token : tokens) {
            requests.add(Integer.parseInt(token));
        }

        // Obtener la secuencia de búsqueda
        List<Integer> seekSequence = lookAlgorithm(requests, head, direction);

        // Mostrar la secuencia de búsqueda
        System.out.println("Secuencia de búsqueda: " + seekSequence);
        
        // Cerrar el scanner
        scanner.close();
    }
}
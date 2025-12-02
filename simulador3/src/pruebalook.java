
package simuladordepeticionesdiscoduro;

/**
 *
 * @author marco
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class pruebalook {
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
        // Solicitudes de cilindros
        List<Integer> requests = new ArrayList<>();
        Collections.addAll(requests, 10, 22, 35, 90, 150, 180);
        // Posición inicial de la cabeza
        int head = 50;
        // Dirección de movimiento (puede ser "right" o "left")
        String direction = "right";

        // Obtener la secuencia de búsqueda
        List<Integer> seekSequence = lookAlgorithm(requests, head, direction);

        // Mostrar la secuencia de búsqueda
        System.out.println("Secuencia de búsqueda: " + seekSequence);
    }
}
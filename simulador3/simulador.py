import tkinter as tk
from tkinter import ttk, messagebox
import time


class DiskSchedulerSimulator:
    def __init__(self, root):
        self.root = root
        self.root.title("S-PIEDRA - Simulador de Planificación de Disco")
        self.root.geometry("1000x700")
        self.root.configure(bg="#f0f0f0")

        self.results = []
        self.current_animation = []
        self.animation_index = 0
        self.animation_speed = 20
        self.is_playing = False

        self.create_widgets()

    def create_widgets(self):
        # Frame principal de entrada
        input_frame = tk.LabelFrame(self.root, text="Parámetros de Simulación",
                                    font=("Arial", 12, "bold"), bg="#f0f0f0", padx=10, pady=10)
        input_frame.pack(padx=10, pady=10, fill="x")

        # Pista inicial
        tk.Label(input_frame, text="Pista Inicial:", bg="#f0f0f0", font=("Arial", 10)).grid(row=0, column=0, sticky="w",
                                                                                            pady=5)
        self.initial_track = tk.Entry(input_frame, width=15, font=("Arial", 10))
        self.initial_track.grid(row=0, column=1, padx=5, pady=5)
        self.initial_track.insert(0, "50")

        # Cola de peticiones
        tk.Label(input_frame, text="Cola de Peticiones:", bg="#f0f0f0", font=("Arial", 10)).grid(row=1, column=0,
                                                                                                 sticky="w", pady=5)
        self.requests_entry = tk.Entry(input_frame, width=50, font=("Arial", 10))
        self.requests_entry.grid(row=1, column=1, columnspan=3, padx=5, pady=5)
        self.requests_entry.insert(0, "82, 170, 43, 140, 24, 16, 190")

        # Número de pistas
        tk.Label(input_frame, text="Número de Pistas:", bg="#f0f0f0", font=("Arial", 10)).grid(row=2, column=0,
                                                                                               sticky="w", pady=5)
        self.num_tracks = tk.Entry(input_frame, width=15, font=("Arial", 10))
        self.num_tracks.grid(row=2, column=1, padx=5, pady=5)
        self.num_tracks.insert(0, "200")

        # Algoritmo
        tk.Label(input_frame, text="Algoritmo:", bg="#f0f0f0", font=("Arial", 10)).grid(row=3, column=0, sticky="w",
                                                                                        pady=5)
        self.algorithm = ttk.Combobox(input_frame, values=["FCFS", "SSTF", "SCAN", "CSCAN", "LOOK", "CLOOK"],
                                      state="readonly", width=12, font=("Arial", 10))
        self.algorithm.grid(row=3, column=1, padx=5, pady=5)
        self.algorithm.set("FCFS")

        # Botón iniciar
        tk.Button(input_frame, text="Iniciar Simulación", command=self.run_simulation,
                  bg="#4CAF50", fg="white", font=("Arial", 10, "bold"), padx=10, pady=5).grid(row=3, column=2, padx=10)

        tk.Button(input_frame, text="Limpiar Resultados", command=self.clear_results,
                  bg="#f44336", fg="white", font=("Arial", 10, "bold"), padx=10, pady=5).grid(row=3, column=3, padx=10)

        # Frame de resultados
        results_frame = tk.LabelFrame(self.root, text="Resultados de Algoritmos Ejecutados",
                                      font=("Arial", 12, "bold"), bg="#f0f0f0", padx=10, pady=10)
        results_frame.pack(padx=10, pady=10, fill="both", expand=True)

        # Tabla de resultados
        columns = ("Algoritmo", "Movimientos", "Tiempo Promedio", "Mejor")
        self.results_tree = ttk.Treeview(results_frame, columns=columns, show="headings", height=6)

        for col in columns:
            self.results_tree.heading(col, text=col)
            self.results_tree.column(col, width=150, anchor="center")

        self.results_tree.pack(side="left", fill="both", expand=True)

        scrollbar = ttk.Scrollbar(results_frame, orient="vertical", command=self.results_tree.yview)
        scrollbar.pack(side="right", fill="y")
        self.results_tree.configure(yscrollcommand=scrollbar.set)

        # Botón eliminar
        tk.Button(results_frame, text="-", command=self.delete_selected,
                  bg="#ff9800", fg="white", font=("Arial", 12, "bold"), width=3).pack(pady=5)

        # Frame de detalles
        details_frame = tk.LabelFrame(self.root, text="Detalles de la Simulación",
                                      font=("Arial", 12, "bold"), bg="#f0f0f0", padx=10, pady=10)
        details_frame.pack(padx=10, pady=10, fill="both", expand=True)

        self.details_text = tk.Text(details_frame, height=8, width=80, font=("Courier", 9), wrap="word")
        self.details_text.pack(side="left", fill="both", expand=True)

        details_scroll = ttk.Scrollbar(details_frame, orient="vertical", command=self.details_text.yview)
        details_scroll.pack(side="right", fill="y")
        self.details_text.configure(yscrollcommand=details_scroll.set)

        # Frame de animación
        animation_frame = tk.LabelFrame(self.root, text="Simulación Animada",
                                        font=("Arial", 12, "bold"), bg="#f0f0f0", padx=10, pady=10)
        animation_frame.pack(padx=10, pady=10, fill="x")

        controls_frame = tk.Frame(animation_frame, bg="#f0f0f0")
        controls_frame.pack()

        tk.Button(controls_frame, text="<-", command=self.prev_step,
                  bg="#2196F3", fg="white", font=("Arial", 10, "bold"), width=3).pack(side="left", padx=5)

        tk.Button(controls_frame, text="▶", command=self.toggle_play,
                  bg="#4CAF50", fg="white", font=("Arial", 10, "bold"), width=3).pack(side="left", padx=5)

        tk.Button(controls_frame, text="->", command=self.next_step,
                  bg="#2196F3", fg="white", font=("Arial", 10, "bold"), width=3).pack(side="left", padx=5)

        tk.Label(controls_frame, text="Velocidad:", bg="#f0f0f0", font=("Arial", 10)).pack(side="left", padx=10)

        self.speed_var = tk.IntVar(value=20)
        self.speed_scale = tk.Scale(controls_frame, from_=1, to=100, orient="horizontal",
                                    variable=self.speed_var, command=self.update_speed, bg="#f0f0f0")
        self.speed_scale.pack(side="left", padx=5)

        self.animation_label = tk.Label(animation_frame, text="", font=("Courier", 10),
                                        bg="white", relief="solid", borderwidth=1, height=3, anchor="w", justify="left")
        self.animation_label.pack(fill="x", pady=10, padx=10)

    def run_simulation(self):
        try:
            initial = int(self.initial_track.get())
            requests_str = self.requests_entry.get()
            requests = [int(x.strip()) for x in requests_str.split(",")]
            num_tracks = int(self.num_tracks.get())
            algo = self.algorithm.get()

            if algo == "FCFS":
                movements, sequence = self.fcfs(initial, requests)
            elif algo == "SSTF":
                movements, sequence = self.sstf(initial, requests)
            elif algo == "SCAN":
                movements, sequence = self.scan(initial, requests, num_tracks)
            elif algo == "CSCAN":
                movements, sequence = self.cscan(initial, requests, num_tracks)
            elif algo == "LOOK":
                movements, sequence = self.look(initial, requests)
            elif algo == "CLOOK":
                movements, sequence = self.clook(initial, requests)

            avg_time = movements / len(requests) if requests else 0

            self.results.append({
                "algorithm": algo,
                "movements": movements,
                "avg_time": round(avg_time, 2),
                "sequence": sequence
            })

            self.update_results_table()
            self.show_details(algo, movements, avg_time, sequence)
            self.prepare_animation(initial, sequence)

        except Exception as e:
            messagebox.showerror("Error", f"Error en la simulación: {str(e)}")

    def fcfs(self, initial, requests):
        sequence = [initial] + requests
        movements = sum(abs(sequence[i + 1] - sequence[i]) for i in range(len(sequence) - 1))
        return movements, sequence

    def sstf(self, initial, requests):
        sequence = [initial]
        remaining = requests.copy()
        current = initial

        while remaining:
            nearest = min(remaining, key=lambda x: abs(x - current))
            sequence.append(nearest)
            current = nearest
            remaining.remove(nearest)

        movements = sum(abs(sequence[i + 1] - sequence[i]) for i in range(len(sequence) - 1))
        return movements, sequence

    def scan(self, initial, requests, num_tracks):
        left = sorted([r for r in requests if r < initial], reverse=True)
        right = sorted([r for r in requests if r >= initial])

        sequence = [initial] + right + [num_tracks - 1] + left
        movements = sum(abs(sequence[i + 1] - sequence[i]) for i in range(len(sequence) - 1))
        return movements, sequence

    def cscan(self, initial, requests, num_tracks):
        left = sorted([r for r in requests if r < initial])
        right = sorted([r for r in requests if r >= initial])

        sequence = [initial] + right + [num_tracks - 1, 0] + left
        movements = sum(abs(sequence[i + 1] - sequence[i]) for i in range(len(sequence) - 1))
        return movements, sequence

    def look(self, initial, requests):
        left = sorted([r for r in requests if r < initial], reverse=True)
        right = sorted([r for r in requests if r >= initial])

        sequence = [initial] + right + left
        movements = sum(abs(sequence[i + 1] - sequence[i]) for i in range(len(sequence) - 1))
        return movements, sequence

    def clook(self, initial, requests):
        left = sorted([r for r in requests if r < initial])
        right = sorted([r for r in requests if r >= initial])

        sequence = [initial] + right + left
        movements = sum(abs(sequence[i + 1] - sequence[i]) for i in range(len(sequence) - 1))
        return movements, sequence

    def update_results_table(self):
        for item in self.results_tree.get_children():
            self.results_tree.delete(item)

        best_algo = min(self.results, key=lambda x: x["movements"]) if self.results else None

        for result in self.results:
            is_best = "★" if best_algo and result["algorithm"] == best_algo["algorithm"] else ""
            self.results_tree.insert("", "end", values=(
                result["algorithm"],
                result["movements"],
                result["avg_time"],
                is_best
            ))

    def show_details(self, algo, movements, avg_time, sequence):
        self.details_text.delete(1.0, tk.END)
        details = f"Algoritmo: {algo}\n"
        details += f"Movimientos Totales: {movements}\n"
        details += f"Tiempo Promedio: {avg_time:.2f}\n"
        details += f"Secuencia de acceso: {' -> '.join(map(str, sequence))}\n"
        self.details_text.insert(1.0, details)

    def prepare_animation(self, initial, sequence):
        self.current_animation = []
        for i in range(len(sequence) - 1):
            move = abs(sequence[i + 1] - sequence[i])
            self.current_animation.append(f"Paso {i + 1}: {sequence[i]} -> {sequence[i + 1]} (Movimiento: {move})")
        self.animation_index = 0
        self.show_animation_step()

    def show_animation_step(self):
        if self.current_animation and 0 <= self.animation_index < len(self.current_animation):
            self.animation_label.config(text=f"{self.current_animation[self.animation_index]}\n"
                                             f"Paso {self.animation_index + 1} de {len(self.current_animation)}")
        else:
            self.animation_label.config(text="No hay animación disponible")

    def prev_step(self):
        if self.animation_index > 0:
            self.animation_index -= 1
            self.show_animation_step()

    def next_step(self):
        if self.animation_index < len(self.current_animation) - 1:
            self.animation_index += 1
            self.show_animation_step()

    def toggle_play(self):
        self.is_playing = not self.is_playing
        if self.is_playing:
            self.auto_play()

    def auto_play(self):
        if self.is_playing and self.animation_index < len(self.current_animation) - 1:
            self.next_step()
            delay = int(1000 / self.speed_var.get())
            self.root.after(delay, self.auto_play)
        else:
            self.is_playing = False

    def update_speed(self, value):
        self.animation_speed = int(value)

    def delete_selected(self):
        selected = self.results_tree.selection()
        if selected:
            index = self.results_tree.index(selected[0])
            del self.results[index]
            self.update_results_table()

    def clear_results(self):
        self.results = []
        self.update_results_table()
        self.details_text.delete(1.0, tk.END)
        self.animation_label.config(text="")
        self.current_animation = []


if __name__ == "__main__":
    root = tk.Tk()
    app = DiskSchedulerSimulator(root)
    root.mainloop()
package com.example.navidadapi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.navidadapi.Model.Jugador;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jugador")
public class JugadorController {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Jugador> memoria = new ConcurrentHashMap<>();
    private final String RUTA_FICHERO = "ranking.json";

    public JugadorController() {
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @PostMapping("/crear/{nombre}")
    public Jugador crearJugador(@PathVariable String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "JugadorSinNombre";
        }
        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setInicio(LocalDateTime.now());
        memoria.put(nombre, jugador);
        return jugador;
    }

    @GetMapping("/obtener/{nombre}")
    public Jugador obtenerJugador(@PathVariable String nombre) {
        return memoria.get(nombre);
    }

    @GetMapping("/pista/{codigoQR}")
    public String obtenerPista(@PathVariable String codigoQR) {
        if (codigoQR == null) return "Error";

        switch (codigoQR) {
            case "agua":
                return "Antes de ser famoso, no me llamaba Java. Mi primer nombre fue Oak.";
            case "extintor":
                return "Soy el lenguaje con el que se programó la versión original de Minecraft.";
            case "cocacola":
                return "Mi mascota es una criatura negra con forma de gota llamada Duke.";
            case "baños":
                return "Soy la base principal con la que se construyó el sistema operativo Android.";
            case "sillas":
                return "Java se ejecuta en más de 3 mil millones de dispositivos.";
            default:
                return "Este no es el QR que buscas, sigue buscando.";
        }
    }

    @PostMapping("/finalizar/{nombre}")
    public Jugador finalizarJuego(@PathVariable String nombre) {
        if (nombre == null) return null;
        Jugador jugador = memoria.get(nombre);
        if (jugador != null) {
            jugador.setFin(LocalDateTime.now());
            if (jugador.getInicio() != null) {
                long tiempoTotal = Duration.between(jugador.getInicio(), jugador.getFin()).toSeconds();
                jugador.setTotalTiempo(tiempoTotal);
            }
            guardarJugadorEnRanking(jugador);
            return jugador;
        }
        return null;
    }

    private void guardarJugadorEnRanking(Jugador jugador) {
        try {
            List<Jugador> lista = new ArrayList<>(memoria.values());
            objectMapper.writeValue(new File(RUTA_FICHERO), lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/Ranking")
    public List<Jugador> ranking() {
        return memoria.values().stream()
                .filter(l -> l.getTotalTiempo() != null)
                .sorted(Comparator.comparingLong(Jugador::getTotalTiempo))
                .collect(Collectors.toList());
    }
}
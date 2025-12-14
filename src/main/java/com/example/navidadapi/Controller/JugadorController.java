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
                return "Con eso el trineo ya no quedara en cenizas.";
            case "cafe":
                return "¿Sabias que el logo de Java es una taza de café? aunque tambien ayuda a Papa Noel a mantenerse despierto.";
            case "baston":
                return "Sin eso Papa Noel no puede caminar bien.";
            case "caja de herramientas":
                return "Menos mal, lo necesitabamos para arreglar el trineo.";
            case "gafas":
                return "Sin eso Papa Noel no puede ver bien.";
            default:
                return "Este no es el QR que buscas, sigue buscando.";
        }
    }

    private void guardarRankingCompleto(Jugador jugadorFinalizado) {
        List<Jugador> rankingCompleto = new ArrayList<>();
        File file = new File(RUTA_FICHERO);

        try {
            if (file.exists()) {
                Jugador[] jugadoresArray = objectMapper.readValue(file, Jugador[].class);
                rankingCompleto.addAll(Arrays.asList(jugadoresArray));
            }
            rankingCompleto.add(jugadorFinalizado);
            Map<String, Jugador> jugadoresUnicos = rankingCompleto.stream()
                    .collect(Collectors.toMap(
                            Jugador::getNombre,
                            j -> j,
                            (existing, replacement) ->
                                    replacement.getTotalTiempo() < existing.getTotalTiempo() ? replacement : existing
                    ));
            objectMapper.writeValue(file, jugadoresUnicos.values());
        } catch (IOException e) {
            e.printStackTrace();
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
            guardarRankingCompleto(jugador);
            return jugador;
        }
        return null;
    }



    @GetMapping("/Ranking")
    public List<Jugador> ranking() {
        try {
            File file = new File(RUTA_FICHERO);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            Jugador[] jugadoresArray = objectMapper.readValue(file, Jugador[].class);
            List<Jugador> jugadoresList = Arrays.asList(jugadoresArray);
            return jugadoresList.stream()
                    .sorted(Comparator.comparingLong(Jugador::getTotalTiempo))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
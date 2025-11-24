package com.example.navidadapi.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class Jugador {
    private String nombre;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inicio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fin;
    private Long TotalTiempo;
}

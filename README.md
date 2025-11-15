# Cincuentazo (50ZO)

Un juego de cartas donde la estrategia matemática es clave. ¡No dejes que la suma supere 50!

## Descripción

**Cincuentazo** es un juego de cartas basado en una baraja francesa estándar (52 cartas) donde los jugadores deben gestionar estratégicamente sus jugadas para mantener la suma de la mesa por debajo de 50 puntos. El último jugador que quede sin ser eliminado gana la partida.

Este proyecto fue desarrollado tomando como base los archivos del juego UNO creados por el profesor Fabian Stiven Valencia Cordoba del curso Fundamentos de Programacion Orientada a Eventos, transformándolo en un nuevo juego con mecánicas completamente diferentes basadas en suma aritmética y eliminación progresiva.

---

## Características Principales

### Mecánicas de Juego
- Sistema de suma progresiva (máximo 50 puntos)
- Eliminación automática de jugadores sin jugadas válidas
- Selección de 1 a 3 oponentes máquina
- Inteligencia artificial para jugadores automáticos
- Reciclaje automático del mazo cuando se agota

### Interfaz de Usuario
- Temporizador de juego y turnos
- Barra de progreso visual (0-50)
- Indicadores visuales para cartas jugables (brillo verde/rojo)
- Estado en tiempo real de todos los jugadores
- Contador de cartas restantes en el mazo y jugadores activos

---

## Reglas del Juego

### Valores de las Cartas

| Carta | Valor | Descripción |
|-------|-------|-------------|
| **2-8, 10** | Valor nominal | Suman su valor al total de la mesa |
| **9** | 0 puntos | Carta neutral, no cambia la suma |
| **J, Q, K** | -10 puntos | Restan 10 del total de la mesa |
| **A (As)** | 1 o 10 puntos | Se calcula automáticamente el valor óptimo según la suma actual |

### Objetivo

Ser el último jugador que quede sin ser eliminado. Los jugadores se eliminan cuando no pueden jugar ninguna carta sin exceder el límite de 50 puntos.

### Condiciones de Eliminación

Un jugador es eliminado cuando:
- No tiene ninguna carta que pueda jugar sin hacer que la suma exceda 50
- Después de tomar una carta del mazo, sigue sin tener jugadas válidas

---

## Cómo Jugar

### Inicio del Juego
1. Selecciona el número de oponentes máquina (1, 2 o 3)
2. Cada jugador recibe **4 cartas** iniciales
3. Se coloca una carta inicial en la mesa
4. Comienza el jugador humano

### Durante tu Turno
1. **Selecciona una carta jugable** (marcada con brillo verde)
   - La carta no debe hacer que la suma exceda 50 puntos
   - Ejemplo: Si la suma es 38, puedes jugar cartas que resulten en 50 o menos
   
2. **Toma una carta automáticamente** del mazo después de jugar

3. **Verificación de eliminación**:
   - Si después de tomar la carta no puedes jugar ninguna, serás eliminado
   - Tus cartas restantes se devuelven al mazo

### Cartas Jugables

Las cartas con **brillo verde** son jugables. Las cartas con **brillo rojo** no se pueden jugar porque excederían el límite.

### Victoria

El juego termina cuando solo queda un jugador activo. Ese jugador es declarado ganador.

---

## Cambios Respecto al UNO Original

### Lo que se Cambió
- **Sistema de cartas**: De cartas UNO de colores a baraja francesa estándar
- **Mecánica principal**: De coincidencia de color/número a suma aritmética
- **Objetivo**: De quedarse sin cartas primero a ser el último superviviente
- **Cartas iniciales**: De 10 a 4 cartas por jugador
- **Tomar carta**: De opcional a obligatorio después de cada jugada

### Lo que se Agregó
- Sistema de suma con límite de 50 puntos
- Eliminación progresiva de jugadores
- Cálculo automático del valor del As (1 o 10)
- Pantalla de selección de número de jugadores
- Temporizador y barra de progreso
- Sistema de excepciones personalizadas
- Reciclaje automático del mazo

### Lo que se Eliminó
- Cartas especiales de UNO (SKIP, REVERSE, WILD, DRAW)
- Sistema de coincidencia de colores
- Botón "UNO" (ya no es relevante)

---

## Créditos

- **Proyecto base**: Juego UNO desarrollado por el profesor Fabian Stiven Valencia Cordoba
- **Desarrollo de Cincuentazo**: Jairo Andrés Tegue Gomez
- **Repositorio**: https://github.com/Andres2902/Miniproyecto_50zo

---


<div align="center">

**Desarrollado como proyecto académico de Fundamentos de Programación Orientada a Eventos**

🎮 ¡Disfruta jugando Cincuentazo! 🎮

</div>

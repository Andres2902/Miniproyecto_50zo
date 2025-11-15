package org.example.eiscuno.model.unoenum;

/**
 * Enumeration of all cards in the Cincuentazo game.
 * Maps card identifiers to their image file paths for a standard 52-card French deck.
 *
 * @author Jairo Andrés Tegue
 * @version 1.0
 * @since 2025
 */
public enum EISCUnoEnum {

    COR_C2("/org/example/eiscuno/cards/Cartas_Corazon/2_Corazon.png"),
    COR_C3("/org/example/eiscuno/cards/Cartas_Corazon/3_Corazon.png"),
    COR_C4("/org/example/eiscuno/cards/Cartas_Corazon/4_Corazon.png"),
    COR_C5("/org/example/eiscuno/cards/Cartas_Corazon/5_Corazon.png"),
    COR_C6("/org/example/eiscuno/cards/Cartas_Corazon/6_Corazon.png"),
    COR_C7("/org/example/eiscuno/cards/Cartas_Corazon/7_Corazon.png"),
    COR_C8("/org/example/eiscuno/cards/Cartas_Corazon/8_Corazon.png"),
    COR_C9("/org/example/eiscuno/cards/Cartas_Corazon/9_Corazon.png"),
    COR_C10("/org/example/eiscuno/cards/Cartas_Corazon/10_Corazon.png"),
    COR_CJ("/org/example/eiscuno/cards/Cartas_Corazon/J_Corazon.png"),
    COR_CQ("/org/example/eiscuno/cards/Cartas_Corazon/Q_Corazon.png"),
    COR_CK("/org/example/eiscuno/cards/Cartas_Corazon/K_Corazon.png"),
    COR_CAS("/org/example/eiscuno/cards/Cartas_Corazon/AS_Corazon.png"),

    DIAM_D2("/org/example/eiscuno/cards/Cartas_Diamantes/2_Diamante.png"),
    DIAM_D3("/org/example/eiscuno/cards/Cartas_Diamantes/3_Diamantes.png"),
    DIAM_D4("/org/example/eiscuno/cards/Cartas_Diamantes/4_Diamantes.png"),
    DIAM_D5("/org/example/eiscuno/cards/Cartas_Diamantes/5_Diamantes.png"),
    DIAM_D6("/org/example/eiscuno/cards/Cartas_Diamantes/6_Diamantes.png"),
    DIAM_D7("/org/example/eiscuno/cards/Cartas_Diamantes/7_Diamantes.png"),
    DIAM_D8("/org/example/eiscuno/cards/Cartas_Diamantes/8_Diamantes.png"),
    DIAM_D9("/org/example/eiscuno/cards/Cartas_Diamantes/9_Diamantes.png"),
    DIAM_D10("/org/example/eiscuno/cards/Cartas_Diamantes/10_Diamantes.png"),
    DIAM_DJ("/org/example/eiscuno/cards/Cartas_Diamantes/J_Diamantes.png"),
    DIAM_DQ("/org/example/eiscuno/cards/Cartas_Diamantes/Q_Diamantes.png"),
    DIAM_DK("/org/example/eiscuno/cards/Cartas_Diamantes/K_Diamantes.png"),
    DIAM_DAS("/org/example/eiscuno/cards/Cartas_Diamantes/As_Diamante.png"),

    PIC_P2("/org/example/eiscuno/cards/Cartas_Picas/2_Picas.png"),
    PIC_P3("/org/example/eiscuno/cards/Cartas_Picas/3_Picas.png"),
    PIC_P4("/org/example/eiscuno/cards/Cartas_Picas/4_Picas.png"),
    PIC_P5("/org/example/eiscuno/cards/Cartas_Picas/5_Picas.png"),
    PIC_P6("/org/example/eiscuno/cards/Cartas_Picas/6_Picas.png"),
    PIC_P7("/org/example/eiscuno/cards/Cartas_Picas/7_Picas.png"),
    PIC_P8("/org/example/eiscuno/cards/Cartas_Picas/8_Picas.png"),
    PIC_P9("/org/example/eiscuno/cards/Cartas_Picas/9_Picas.png"),
    PIC_P10("/org/example/eiscuno/cards/Cartas_Picas/10_Picas.png"),
    PIC_PJ("/org/example/eiscuno/cards/Cartas_Picas/J_Picas.png"),
    PIC_PQ("/org/example/eiscuno/cards/Cartas_Picas/Q_Picas.png"),
    PIC_PK("/org/example/eiscuno/cards/Cartas_Picas/K_Picas.png"),
    PIC_PAS("/org/example/eiscuno/cards/Cartas_Picas/As_Picas.png"),

    TREB_T2("/org/example/eiscuno/cards/Cartas_Trebol/2_Trebol.png"),
    TREB_T3("/org/example/eiscuno/cards/Cartas_Trebol/3_Trebol.png"),
    TREB_T4("/org/example/eiscuno/cards/Cartas_Trebol/4_Trebol.png"),
    TREB_T5("/org/example/eiscuno/cards/Cartas_Trebol/5_Trebol.png"),
    TREB_T6("/org/example/eiscuno/cards/Cartas_Trebol/6_Trebol.png"),
    TREB_T7("/org/example/eiscuno/cards/Cartas_Trebol/7_Trebol.png"),
    TREB_T8("/org/example/eiscuno/cards/Cartas_Trebol/8_Trebol.png"),
    TREB_T9("/org/example/eiscuno/cards/Cartas_Trebol/9_Trebol.png"),
    TREB_T10("/org/example/eiscuno/cards/Cartas_Trebol/10_Trebol.png"),
    TREB_TJ("/org/example/eiscuno/cards/Cartas_Trebol/J_Trebol.png"),
    TREB_TQ("/org/example/eiscuno/cards/Cartas_Trebol/Q_Trebol.png"),
    TREB_TK("/org/example/eiscuno/cards/Cartas_Trebol/K_Trebol.png"),
    TREB_TAS("/org/example/eiscuno/cards/Cartas_Trebol/As_Trebol.png"),

    DECK_OF_CARDS("/org/example/eiscuno/images/Carta_oculta.png"),
    CARD_BACK("/org/example/eiscuno/images/Fondo_50ZO.png");

    private final String filePath;

    /**
     * Constructs an EISCUnoEnum with the specified file path.
     *
     * @param filePath the path to the card image file
     */
    EISCUnoEnum(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the file path for this card's image.
     *
     * @return the file path as a String
     */
    public String getFilePath() {
        return filePath;
    }
}
package com.example.backend_shopfromhome.Model;

public enum StatoOrdine {
    IN_LAVORAZIONE,
    PRONTO,
    RITIRATO,
    ANNULLATO;


    public static StatoOrdine fromString(String stato) {
        // Rimuoviamo virgolette e spazi extra
        String cleanedStato = stato.replaceAll("\"", "").trim();

        System.out.println("Stato ricevuto (dopo pulizia): '" + cleanedStato + "'");

        for (StatoOrdine s : StatoOrdine.values()) {
            System.out.println("Confrontando con stato enum: " + s.name());
            if (s.name().equalsIgnoreCase(cleanedStato)) {
                System.out.println("Trovato stato corrispondente: " + s);
                return s;
            }
        }
        throw new IllegalArgumentException("Stato non valido: " + cleanedStato);
    }

}

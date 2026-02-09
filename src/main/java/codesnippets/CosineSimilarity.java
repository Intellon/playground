package codesnippets;

import java.util.*;

public class CosineSimilarity {
    
    /**
     * Berechnet die Cosine Similarity zwischen zwei Strings
     * @param text1 Erster String
     * @param text2 Zweiter String
     * @return Cosine Similarity Wert zwischen 0.0 und 1.0
     */
    public static double calculateCosineSimilarity(String text1, String text2) {
        // Strings in Kleinbuchstaben umwandeln und in Wörter aufteilen
        String[] words1 = text1.toLowerCase().split("\\s+");
        String[] words2 = text2.toLowerCase().split("\\s+");
        
        // Alle einzigartigen Wörter sammeln
        Set<String> allWords = new HashSet<>();
        allWords.addAll(Arrays.asList(words1));
        allWords.addAll(Arrays.asList(words2));
        
        // Vektoren erstellen
        Map<String, Integer> vector1 = createVector(words1, allWords);
        Map<String, Integer> vector2 = createVector(words2, allWords);
        
        // Cosine Similarity berechnen
        return cosineSimilarity(vector1, vector2, allWords);
    }
    
    /**
     * Erstellt einen Vektor (Wort-Häufigkeits-Map) für einen Text
     */
    private static Map<String, Integer> createVector(String[] words, Set<String> allWords) {
        Map<String, Integer> vector = new HashMap<>();
        
        // Initialisiere alle Wörter mit 0
        for (String word : allWords) {
            vector.put(word, 0);
        }
        
        // Zähle die Häufigkeit jedes Wortes
        for (String word : words) {
            vector.put(word, vector.get(word) + 1);
        }
        
        return vector;
    }
    
    /**
     * Berechnet die Cosine Similarity zwischen zwei Vektoren
     */
    private static double cosineSimilarity(Map<String, Integer> vector1, 
                                         Map<String, Integer> vector2, 
                                         Set<String> allWords) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        
        for (String word : allWords) {
            int freq1 = vector1.get(word);
            int freq2 = vector2.get(word);
            
            dotProduct += freq1 * freq2;
            magnitude1 += freq1 * freq1;
            magnitude2 += freq2 * freq2;
        }
        
        if (magnitude1 == 0.0 || magnitude2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }
    
    /**
     * Hauptmethode mit Beispielen
     */
    public static void main(String[] args) {
        // Beispiel-Strings
        String text1 = "Das ist ein schöner Tag";
        String text2 = "Es ist ein wunderschöner Tag";
        String text3 = "Die Katze sitzt auf der Matte";
        String text4 = "Der Hund läuft im Park";
        String text5 = "Das ist ein schöner Tag heute";
        
        System.out.println("=== Cosine Similarity String-Vergleich ===\n");
        
        // Verschiedene Vergleiche durchführen
        System.out.printf("Text 1: \"%s\"\n", text1);
        System.out.printf("Text 2: \"%s\"\n", text2);
        System.out.printf("Similarity: %.4f\n\n", calculateCosineSimilarity(text1, text2));
        
        System.out.printf("Text 1: \"%s\"\n", text1);
        System.out.printf("Text 3: \"%s\"\n", text3);
        System.out.printf("Similarity: %.4f\n\n", calculateCosineSimilarity(text1, text3));
        
        System.out.printf("Text 1: \"%s\"\n", text1);
        System.out.printf("Text 4: \"%s\"\n", text4);
        System.out.printf("Similarity: %.4f\n\n", calculateCosineSimilarity(text1, text4));
        
        System.out.printf("Text 1: \"%s\"\n", text1);
        System.out.printf("Text 5: \"%s\"\n", text5);
        System.out.printf("Similarity: %.4f\n\n", calculateCosineSimilarity(text1, text5));
        
        // Identische Strings
        System.out.printf("Text 1: \"%s\"\n", text1);
        System.out.printf("Text 1: \"%s\"\n", text1);
        System.out.printf("Similarity: %.4f\n\n", calculateCosineSimilarity(text1, text1));
        
        // Demonstration der Interpretation
        System.out.println("=== Interpretation der Ergebnisse ===");
        System.out.println("1.0000 = Identische Texte");
        System.out.println("0.8000+ = Sehr ähnliche Texte");
        System.out.println("0.5000+ = Mäßig ähnliche Texte");
        System.out.println("0.2000+ = Wenig ähnliche Texte");
        System.out.println("0.0000 = Keine Gemeinsamkeiten");
    }
}
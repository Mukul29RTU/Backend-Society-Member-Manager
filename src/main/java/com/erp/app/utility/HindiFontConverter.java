package com.erp.app.utility;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HindiFontConverter {

    private static final Map<String, String> WORD_OVERRIDES = new LinkedHashMap<>();
    private static final Map<String, String> SPECIAL_COMBINED_CHARS = new LinkedHashMap<>();
    private static final String[] KRUTI_CHARS = {
            "vkS", "vks", "vk", "v", "bZ", "b", "m", "Å", "¡",
            "ks", "kS", "s", "S", ",", ",s",
            "d", "[k", "x", "?k", "p", "N", "t", ">", "V", "Bd",
            "M", "<", "r", "Fk", "n", "/k", "u",
            "i", "Q", "c", "Hk", "e", ";", "j", "y", "o",
            "'k", "\"k", "l", "g",
            "k", "f", "h", "q", "w", "`", "a", "%", "~", "Z"
    };
    private static final String[] UNICODE_CHARS = {
            "औ", "ओ", "आ", "अ", "ई", "इ", "उ", "ऊ", "ँ",
            "ो", "ौ", "े", "ै", "ए", "ऐ",
            "क", "ख", "ग", "घ", "च", "छ", "ज", "झ", "ट", "ठ",
            "ड", "ढ", "त", "थ", "द", "ध", "न",
            "प", "फ", "ब", "भ", "म", "य", "र", "ल", "व",
            "श", "ष", "स", "ह",
            "ा", "ि", "ी", "ु", "ू", "ृ", "ं", "ः", "्", "र्"
    };

    static {
        // Populate word-level overrides - add your names/phrases here
        WORD_OVERRIDES.put("xwnfj;k", "गूदरिया");
        WORD_OVERRIDES.put("Jh jkenkl caly Jh gqDepUn caly", "श्री रामदास बंसल श्री हुक्मचन्द बंसल");
        WORD_OVERRIDES.put("Jh fc'kEHkj n;ky eaxy Lo-", "श्री बिशम्भर दयाल मंगल स्व. श्री प्यारेलाल मंगल");
        // Add more mappings from your data here...

        // Special combined characters mappings
        SPECIAL_COMBINED_CHARS.put("Ø", "क्र");
        SPECIAL_COMBINED_CHARS.put("J", "श्री");
        SPECIAL_COMBINED_CHARS.put("L", "स्व");
        SPECIAL_COMBINED_CHARS.put("z", "्र");
        SPECIAL_COMBINED_CHARS.put("[", "ख्य");
        SPECIAL_COMBINED_CHARS.put("U", "न्");
        SPECIAL_COMBINED_CHARS.put("D", "क्");
        SPECIAL_COMBINED_CHARS.put("R", "त्त");
        SPECIAL_COMBINED_CHARS.put("E", "्व");
        SPECIAL_COMBINED_CHARS.put("I", "प्या");
        SPECIAL_COMBINED_CHARS.put("W", "ॉ");
        SPECIAL_COMBINED_CHARS.put("]", ",");
        SPECIAL_COMBINED_CHARS.put("+", "़");
    }

    public static String toUnicode(String krutiText) {
        if (krutiText == null || krutiText.isEmpty()) return "";

        // First, check if the whole text matches a word-level override
        if (WORD_OVERRIDES.containsKey(krutiText)) {
            return WORD_OVERRIDES.get(krutiText);
        }

        String processedText = krutiText;

        // Apply all word-level overrides (partial replacements)
        for (Map.Entry<String, String> entry : WORD_OVERRIDES.entrySet()) {
            processedText = processedText.replace(entry.getKey(), entry.getValue());
        }

        // Replace special combined characters
        for (Map.Entry<String, String> entry : SPECIAL_COMBINED_CHARS.entrySet()) {
            processedText = processedText.replace(entry.getKey(), entry.getValue());
        }

        // Replace main characters carefully
        for (int i = 0; i < KRUTI_CHARS.length; i++) {
            processedText = processedText.replace(KRUTI_CHARS[i], UNICODE_CHARS[i]);
        }

        // Post processing fixes
        processedText = fixChhotiEe(processedText);
        processedText = fixReph(processedText);

        return processedText.trim();
    }

    // Fix small 'ि' position
    private static String fixChhotiEe(String text) {
        Pattern pattern = Pattern.compile("ि(.)");
        Matcher matcher = pattern.matcher(text);
        StringBuilder sb = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            sb.append(text, lastEnd, matcher.start());
            sb.append(matcher.group(1)).append("ि");
            lastEnd = matcher.end();
        }
        sb.append(text.substring(lastEnd));
        return sb.toString();
    }

    // Fix Reph (र्)
    private static String fixReph(String text) {
        if (!text.contains("र्")) return text;
        Pattern pattern = Pattern.compile("(.)र्");
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("र्$1");
    }
}

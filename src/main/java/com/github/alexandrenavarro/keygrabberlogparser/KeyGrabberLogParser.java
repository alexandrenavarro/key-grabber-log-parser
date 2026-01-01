package com.github.alexandrenavarro.keygrabberlogparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class KeyGrabberLogParser {

    static void main(String... args) {
        if (args.length == 1) {
            final Path path = Paths.get(args[0]);
            if (path.toFile().exists()) {
                try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
                    final Map<KeyPressed, Integer> keyWithModifierPressedCountMap = new HashMap<>();
                    while (bufferedReader.ready()) {
                        final String lineToParse = bufferedReader.readLine();
                        final List<KeyPressed> keyPressedList = parseKeyWithModifierPressed(lineToParse);
                        for (final KeyPressed keyPressed : keyPressedList) {
                            keyWithModifierPressedCountMap.put(keyPressed, keyWithModifierPressedCountMap.getOrDefault(keyPressed, 0) + 1);
                        }
                    }
                    final DecimalFormat decimalFormat = new DecimalFormat("#.###");

                    // Summary of keys pressed
                    final List<Map.Entry<KeyPressed, Integer>> keyPressed = keyWithModifierPressedCountMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getKey().isSuperPressed() == false
                                    && entry.getKey().isCtrlPressed() == false)
                            .sorted(Comparator.comparingInt(entry -> -entry.getValue()))
                            .toList();
                    final double sumOfKeyPressed = keyPressed.stream()
                            .mapToDouble(e -> e.getValue().doubleValue()).sum();


                    IO.println("Summary of " + sumOfKeyPressed + " keys pressed (without Super or Ctrl) :");
                    IO.println("Position, Keys, NbOfTimes, %");
                    int keyPosition = 1;
                    for (Map.Entry<KeyPressed, Integer> keyWithModifierPressedIntegerEntry : keyPressed) {
                        final double percentageOfCurrentKeyPressed = (keyWithModifierPressedIntegerEntry.getValue().doubleValue() * 100.0 / sumOfKeyPressed);
                        IO.println(keyPosition++ + "," + keyWithModifierPressedIntegerEntry.getKey() + "," + keyWithModifierPressedIntegerEntry.getValue() + "," + decimalFormat.format(percentageOfCurrentKeyPressed));
                    }


                    // Summary of shortcuts pressed
                    final List<Map.Entry<KeyPressed, Integer>> shortcutsPressed = keyWithModifierPressedCountMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getKey().isSuperPressed()
                                    || entry.getKey().isCtrlPressed())
                            .sorted(Comparator.comparingInt(entry -> -entry.getValue()))
                            .toList();
                    final double sumOfShortcutsPressed = shortcutsPressed.stream()
                            .mapToDouble(e -> e.getValue().doubleValue()).sum();
                    IO.println("");
                    IO.println("Summary of " + sumOfShortcutsPressed + " shortcuts pressed (with Super or Ctrl)");
                    IO.println("Position, Keys, NbOfTimes, %");
                    int shortCutPosition = 1;
                    for (Map.Entry<KeyPressed, Integer> keyWithModifierPressedIntegerEntry : shortcutsPressed) {
                        final double percentageOfCurrentShortcutPressed = (keyWithModifierPressedIntegerEntry.getValue().doubleValue() * 100.0 / sumOfShortcutsPressed);
                        IO.println(shortCutPosition++ + "," + keyWithModifierPressedIntegerEntry.getKey() + "," + keyWithModifierPressedIntegerEntry.getValue() + "," + decimalFormat.format(percentageOfCurrentShortcutPressed));
                    }
                } catch (IOException e) {
                    final StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    IO.println("Failed to parse file:" + args[0] + " because a technical reason : " + sw);
                }
            } else {
                IO.println("Failed to parse file:" + args[0] + " because file does not exist.");
            }
        } else {
            IO.println("Failed to parse file because you must pass as first argument the file path.");
        }
    }

    static List<KeyPressed> parseKeyWithModifierPressed(final String lineToParse) {
        final List<KeyPressed> keyPressedList = new ArrayList<>();
        final KeyPressed.KeyPressedBuilder keyPressedBuilder = KeyPressed.builder();
        for (int i = 0; i < lineToParse.length(); i++) {
            final char currentCharacter = lineToParse.charAt(i);
            keyPressedBuilder.appendToKeyPressed(currentCharacter);
            if (keyPressedBuilder.getFirstKeyPressedChar() == '[') {
                if (currentCharacter == ']') {
                    if (keyPressedBuilder.getKeyPressed().equals("[Sh]")) {
                        keyPressedBuilder.shiftPressed(true);
                        keyPressedBuilder.clearKeyPressed();
                    } else if (keyPressedBuilder.getKeyPressed().equals("[Ctl]")) {
                        keyPressedBuilder.ctrlPressed(true);
                        keyPressedBuilder.clearKeyPressed();
                    } else if (keyPressedBuilder.getKeyPressed().equals("[Alt]")) {
                        keyPressedBuilder.altPressed(true);
                        keyPressedBuilder.clearKeyPressed();
                    } else if (keyPressedBuilder.getKeyPressed().equals("[Win]")) {
                        keyPressedBuilder.superPressed(true);
                        keyPressedBuilder.clearKeyPressed();
                    } else {
                        keyPressedList.add(keyPressedBuilder.build());
                        keyPressedBuilder.clear();
                    }
                }
            } else {
                keyPressedList.add(keyPressedBuilder.build());
                keyPressedBuilder.clear();
            }
        }
        return keyPressedList;
    }

}

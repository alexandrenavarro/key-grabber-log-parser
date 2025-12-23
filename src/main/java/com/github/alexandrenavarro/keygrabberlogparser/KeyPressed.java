package com.github.alexandrenavarro.keygrabberlogparser;

import java.util.Objects;

public final class KeyPressed {

    private final String keyPressed;
    private final boolean superPressed;
    private final boolean ctrlPressed;
    private final boolean altPressed;
    private final boolean shiftPressed;

    private KeyPressed(String keyPressed, boolean superPressed, boolean ctrlPressed, boolean altPressed, boolean shiftPressed) {
        this.keyPressed = keyPressed;
        this.superPressed = superPressed;
        this.ctrlPressed = ctrlPressed;
        this.altPressed = altPressed;
        this.shiftPressed = shiftPressed;
    }

    public static KeyPressed of(final String keyPressed,
                                final boolean superPressed,
                                final boolean ctrlPressed,
                                final boolean altPressed,
                                final boolean shiftPressed) {
        return new KeyPressed(keyPressed, superPressed, ctrlPressed, altPressed, shiftPressed);
    }

    public static KeyPressed of(String keyPressed) {
        return new KeyPressed(keyPressed, false, false, false, false);
    }

    public static KeyPressed ofShift(String keyPressed) {
        return new KeyPressed(keyPressed, false, false, false, true);
    }

    public static KeyPressed ofCtrl(String keyPressed) {
        return new KeyPressed(keyPressed, false, true, false, false);
    }

    public static KeyPressed ofAlt(String keyPressed) {
        return new KeyPressed(keyPressed, false, false, true, false);
    }

    public static KeyPressed ofSuper(String keyPressed) {
        return new KeyPressed(keyPressed, true, false, false, false);
    }

    public String getKeyPressed() {
        return keyPressed;
    }

    public boolean isSuperPressed() {
        return superPressed;
    }

    public boolean isCtrlPressed() {
        return ctrlPressed;
    }

    public boolean isAltPressed() {
        return altPressed;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KeyPressed that = (KeyPressed) o;
        return superPressed == that.superPressed && ctrlPressed == that.ctrlPressed && altPressed == that.altPressed && shiftPressed == that.shiftPressed && Objects.equals(keyPressed, that.keyPressed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyPressed, superPressed, ctrlPressed, altPressed, shiftPressed);
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        if (superPressed) {
            s.append("Super+");
        }
        if (ctrlPressed) {
            s.append("Ctrl+");
        }
        if (altPressed) {
            s.append("Alt+");
        }
        if (shiftPressed) {
            s.append("Shift+");
        }
        s.append(keyPressed);
        return s.toString();
    }

    public static KeyPressedBuilder builder() {
        return new KeyPressedBuilder();
    }

    public static class KeyPressedBuilder {
        private StringBuilder keyPressed = new StringBuilder();
        private boolean superPressed = false;
        private boolean ctrlPressed = false;
        private boolean altPressed = false;
        private boolean shiftPressed = false;

        private KeyPressedBuilder() {
        }

        public KeyPressedBuilder keyPressed(String keyPressed) {
            this.keyPressed.setLength(0);
            this.keyPressed.append(keyPressed);
            return this;
        }

        public KeyPressedBuilder clearKeyPressed() {
            this.keyPressed.setLength(0);
            return this;
        }

        public char getFirstKeyPressedChar() {
            return this.keyPressed.length() > 0 ? this.keyPressed.charAt(0) : 0;
        }

        public String getKeyPressed() {
            return this.keyPressed.toString();
        }

        public KeyPressedBuilder appendToKeyPressed(char keyPressed) {
            this.keyPressed.append(keyPressed);
            return this;
        }

        public KeyPressedBuilder superPressed(boolean superPressed) {
            this.superPressed = superPressed;
            return this;
        }

        public KeyPressedBuilder ctrlPressed(boolean ctrlPressed) {
            this.ctrlPressed = ctrlPressed;
            return this;
        }

        public KeyPressedBuilder altPressed(boolean altPressed) {
            this.altPressed = altPressed;
            return this;
        }

        public KeyPressedBuilder shiftPressed(boolean shiftPressed) {
            this.shiftPressed = shiftPressed;
            return this;
        }

        public KeyPressed build() {
            return new KeyPressed(this.keyPressed.toString(), this.superPressed, this.ctrlPressed, this.altPressed, this.shiftPressed);
        }

        public void clear() {
            keyPressed.setLength(0);
            superPressed = false;
            ctrlPressed = false;
            altPressed = false;
            shiftPressed = false;
        }

    }

}

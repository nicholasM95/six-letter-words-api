package be.nicholasmeyers.word.domain;

public class Part {
    private final String part;
    private boolean hasPair;

    public Part(String part) {
        this.part = part;
    }

    public String getPart() {
        return part;
    }

    public boolean hasPair() {
        return hasPair;
    }

    public void setHasPair(boolean hasPair) {
        this.hasPair = hasPair;
    }
}

package org.shareio.backend.core.model.vo;

public enum Condition {
    NEW, ALMOST_NEW, LIGHTLY_USED, VISIBLY_USED, BROKEN, DESTROYED;

    public String polishName() {
        return switch (this) {
            case NEW -> "Nowy";
            case ALMOST_NEW -> "Jak nowy";
            case LIGHTLY_USED -> "Lekko używany";
            case VISIBLY_USED -> "Widoczne ślady użycia";
            case BROKEN -> "Zepsuty";
            case DESTROYED -> "Zniszczony";
        };
    }
}

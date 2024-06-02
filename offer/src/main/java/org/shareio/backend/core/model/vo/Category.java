package org.shareio.backend.core.model.vo;

public enum Category {
    ELECTRONICS, LITERATURE, FURNITURE, CLOTHES, MUSIC, HOBBY, TOYS, MOTORIZATION, UTENSILS, OTHER;

    public String polishName()
    {
        return switch (this){
            case ELECTRONICS -> "Elektronika";
            case LITERATURE -> "Literatura";
            case FURNITURE -> "Meble";
            case CLOTHES -> "Ubrania";
            case MUSIC -> "Muzyka";
            case HOBBY -> "Hobby";
            case TOYS -> "Zabawki";
            case MOTORIZATION -> "Motoryzacja";
            case UTENSILS -> "Utensylia kuchenne";
            case OTHER -> "Inne";
        };
    }
}

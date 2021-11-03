package pl.java.homebudget.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomType {
    ROOM_XS("xs"),
    ROOM_S("s"),
    ROOM_M("m"),
    ROOM_L("l"),
    ROOM_XL("xl"),
    ROOM_XXL("xxl");

    private String roomSize;
}

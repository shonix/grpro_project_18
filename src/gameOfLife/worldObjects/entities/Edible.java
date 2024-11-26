package gameOfLife.worldObjects.entities;

import gameOfLife.worldObjects.entities.enums.EntityTypeID;

public interface Edible {
    EntityTypeID getEntityTypeID();
    int getProvidedSustenance();
}

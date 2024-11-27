package gameOfLife.worldObjects.entities;

import gameOfLife.worldObjects.entities.enums.EntityTypeID;

/*
TODO we should consider completely removing this interface.
 */

public interface Edible {
    EntityTypeID getEntityTypeID();
    boolean isEdible();
    int getProvidedSustenance();
}

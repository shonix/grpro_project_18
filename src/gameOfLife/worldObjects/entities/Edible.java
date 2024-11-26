package gameOfLife.worldObjects.entities;

import gameOfLife.worldObjects.entities.enums.EntityID;

public interface Edible {
    public EntityID getEntityID();
    int getProvidedSustenance();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

/**
 * A blueprint persistence implementation that stores blueprints in memory.
 * @author dsbaenar
 */

@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();

    /**
     * Constructor for InMemoryBlueprintPersistence.
     */
    public InMemoryBlueprintPersistence() {}    
    
    /**
     * @param bp the new blueprint
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists,
     *    or any other low-level persistence error occurs.
     */
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(),bp.getName()))){
            throw new BlueprintPersistenceException("The given blueprint already exists: "+ bp);
        }
        else{
            blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        }        
    }

    /** 
     * @param author
     * @param bprintname
     * @return Blueprint
     * @throws BlueprintNotFoundException
     */
    
    /**
     * @param author blueprint's author
     * @param bprintname blueprint's name
     * @return the blueprint of the given name and author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        if (blueprints.containsKey(new Tuple<>(author, bprintname))) {
            return blueprints.get(new Tuple<>(author, bprintname));
        }
        throw new BlueprintNotFoundException("Blueprint not found: " + author + ", " + bprintname);
    }

    /**
     * @return all the blueprints in the system
     * @throws BlueprintPersistenceException if there are no blueprints
     */
    @Override
    public Set<Blueprint> getAllBlueprints() throws BlueprintPersistenceException {
        if (blueprints.isEmpty()){
            throw new BlueprintPersistenceException("There are no Blueprints.");
        }
        return Set.copyOf(blueprints.values());
    }

    /** 
     * @param bp the blueprint to update
     * @throws BlueprintPersistenceException if a blueprint with the same name already exists
     * @throws BlueprintNotFoundException if the blueprint doesn't exist
     */
    @Override
    public void updateBlueprint(Blueprint bp) throws BlueprintPersistenceException, BlueprintNotFoundException {
        if (!blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintNotFoundException("Blueprint not found: " + bp);
        }
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
    }

    /** 
     * @param bp the blueprint to delete
     * @throws BlueprintNotFoundException if the blueprint doesn't exist
     * @throws BlueprintPersistenceException if there's an error during persistence
     */
    @Override
    public void deleteBlueprint(Blueprint bp) throws BlueprintNotFoundException, BlueprintPersistenceException {
        if (!blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintNotFoundException("Blueprint not found: " + bp);
        }
        blueprints.remove(new Tuple<>(bp.getAuthor(), bp.getName()));
    }

}

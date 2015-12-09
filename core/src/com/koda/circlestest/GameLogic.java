package com.koda.circlestest;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Sean on 12/6/2015.
 */
public class GameLogic {

    private final HashMap<Integer, Player> playerMap = new HashMap<Integer, Player>();
    private final Array<Integer> playerIds = new Array<Integer>();

    public GameLogic() {

    }

    public void addPlayer(Messages.NewPlayerMessage m) {
        playerMap.put(m.id, new Player(m.x, m.y, 200, 30, m.name));
        playerIds.add(m.id);
        
        System.out.println("Adding player " + m.name);
    }

    public void removePlayer(Messages.DisconnectMessage m) {
        playerMap.remove(m.id);
        playerIds.removeValue(m.id, true);
    }

    public Player getPlayer(int id) {
        return playerMap.get(id);
    }

    public int getNumPlayers() {
        return playerMap.size();
    }

    public Collection<Player> getPlayers() {
        return playerMap.values();
    }
    
    public Array<Integer> getIds() {
    	return playerIds;
    }

    public void update(float dt) {
        for (Player p : playerMap.values()) {
            p.update(dt);
        }
    }

    public void updatePlayerPosition(Messages.PositionUpdateMessage m) {
        Player p = playerMap.get(m.id);
        if (p != null) {
        	p.position.set(m.x, m.y);
        }
    }
    
    public void sortPlayers(int id) {
    	playerIds.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return a - b;
			}
    	});
    	
    	if (playerIds.contains(id, true)) {
    		playerIds.removeValue(id, true);
        	playerIds.insert(0, id);
        	System.out.println("Putting ourselves at the beginning of the list");
    	}
    }
}

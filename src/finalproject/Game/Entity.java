package finalproject.Game;

import org.lwjgl.util.vector.Vector3f;

import finalproject.GameTime;

/**
 * Base class for any entity in the game, allows homogeneous updating.
 *
 */
public abstract class Entity 
{
	public Vector3f position;
	private GameTime LocalGameTime;
	public boolean dead = false;
	
	public Entity(GameTime GlobalGameTime)
	{
		if(GlobalGameTime != null)
			LocalGameTime = new GameTime(GlobalGameTime);
	}
	
	/**
	 * If this entity's goal has been reached, returns true.
	 * @return true if goalRoached else false.
	 */
	public abstract boolean goalReached();
	
	/**
	 * Update the internal logic of this entity synchronized with a GameTime.
	 * @param GlobalGameTime The GameTime object to synch this Entity game logic.
	 */
	public abstract void update(GameTime GlobalGameTime);
}

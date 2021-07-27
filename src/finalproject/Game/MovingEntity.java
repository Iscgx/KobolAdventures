package finalproject.Game;

import org.lwjgl.util.vector.Vector3f;

import finalproject.GameTime;
import finalproject.World;

/**
 * Base class for any entity who also needs movement.
 *
 */
public abstract class MovingEntity extends DrawableEntity 
{
	public int health;
	public Vector3f size;
	public int[] collisionDirection = new int[7];
	public World Parent = null;
	public int shield = 0;
	
	public MovingEntity(GameTime GlobalGameTime) 
	{
		super(GlobalGameTime);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Updates the position of this entity.
	 * @param position The new position.
	 */
	public abstract void updatePosition(Vector3f position);
	
	/**
	 * Assign this entity to any given world.
	 * @param Mundus
	 */
	public void assignToWorld(World Mundus)
	{
		Parent = Mundus;
		Parent.Entities.add(this);
	}
}

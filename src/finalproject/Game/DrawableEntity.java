package finalproject.Game;

import java.nio.ByteBuffer;

import finalproject.GameTime;

/**
 * Base class for any entity who needs to draw itself, enables the use of Vertex Buffer Objects.
 *
 */
public abstract class DrawableEntity extends Entity
{
	protected short bufferID;
	protected short bufferSize;
	
	public DrawableEntity(GameTime GlobalGameTime)
	{
		super(GlobalGameTime);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Calls this object to draw in the main drawing queue.
	 */
	public abstract void draw();
	
	/**
	 * Saves this object vertex, text coordinates, normal in a VBO.
	 */
	public abstract void saveToBuffer();
	
	/**
	 * Updates this object VBO to the new status.
	 */
	public abstract void updateToBuffer();
	
	/**
	 * Fills the any buffer with this status.
	 * @param VertexBuffer The buffer to fill.
	 */
	protected abstract void fillBuffer(ByteBuffer VertexBuffer);
}

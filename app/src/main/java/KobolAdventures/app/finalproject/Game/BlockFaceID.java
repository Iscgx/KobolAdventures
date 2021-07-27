package finalproject.Game;

/**
 * Public enumeration enabling easier representation of a block face.
 *
 */
public enum BlockFaceID
{
	FRONT(0), BACK(1), TOP(2), BOTTOM(3), RIGHT(4), LEFT(5);
	int bufferOffset;
	
	private BlockFaceID(int bufferOffset)
	{
		this.bufferOffset = bufferOffset;
	}
	
	public int getOffset()
	{
		return bufferOffset;
	}
}

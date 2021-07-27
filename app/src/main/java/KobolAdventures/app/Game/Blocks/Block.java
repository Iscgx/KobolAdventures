package finalproject.Game.Blocks;

import finalproject.Game.TextureID;

/**
 * Base class for all the blocks in chunks.
 *
 */
public abstract class Block
{
	public static final int numberOfVertices = 24;
	public byte thoughness = 0;
//	
	public Block()
	{

	}
	
	public abstract TextureID getTexture();

}

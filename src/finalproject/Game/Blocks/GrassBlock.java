package finalproject.Game.Blocks;

import finalproject.Game.TextureID;

public class GrassBlock extends Block
{
	private TextureID textureID = TextureID.GRASS;
	byte thoughness = 50;

	public GrassBlock()
	{
		super();
		
	}
	
	@Override
	public TextureID getTexture()
	{
		return textureID;
	}
}

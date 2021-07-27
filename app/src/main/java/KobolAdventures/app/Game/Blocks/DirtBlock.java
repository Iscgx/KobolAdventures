package finalproject.Game.Blocks;

import finalproject.Game.TextureID;

public class DirtBlock extends Block
{
	private TextureID textureID = TextureID.DIRT;
	byte thoughness = 50;

	public DirtBlock()
	{
		super();
		
	}
	
	@Override
	public TextureID getTexture()
	{
		return textureID;
	}
}

package finalproject.Game.Blocks;

import finalproject.Game.TextureID;

public class MetalBlock extends Block
{
	private TextureID textureID = TextureID.METAL;
	byte thoughness = 50;
	
	@Override
	public TextureID getTexture()
	{
		return textureID;
	}
}

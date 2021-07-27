package finalproject.Game.Blocks;

import finalproject.Game.TextureID;

public class AdamantiumBlock  extends Block
{
	private TextureID textureID = TextureID.ADAMANTIUM;
	byte thoughness = 50;
	
	@Override
	public TextureID getTexture()
	{
		return textureID;
	}
}

package finalproject.Game;

/**
 * Allows for simple utilization of any texture in the main texture map.
 *
 */
public enum TextureID
{
	
	DIRT(0, 0, 0, false),
	METAL(1, 0, 1, false),
	ADAMANTIUM(2, 0, 2, false),
	GRASS(4, 0, 3, true),
	TEST(3, 0, 4, false),
	HEART(0, 3, 5, false),
	SUN(1, 3, 6, false),
	CROSSHAIR(2, 3, 7, false),
	BLUEMONSTER(0, 6, 8, true),
	ORANGEMONSTER(1, 6, 8, true);
	
	//coordinates in the texture map.
	private int x;
	private int y;
	//texture id.
	private byte id;
	//Allows for cubes with different faces ej: grass.
	private boolean isMultiTexture;
	
	//location of the main texture map.
	public static final String type = "PNG";
	public static final String location = "res/textures.png";
	
	TextureID(int x, int y, int id, boolean isMultiTexture)
	{
		this.x = x;
		this.y = y;
		this.id = (byte)id;
		this.isMultiTexture = isMultiTexture;
	}
	
	public String getFileName()
	{
		return location;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public byte getID()
	{
		return id;
	}
	
	public boolean isMultiTexture()
	{
		return isMultiTexture;
	}
	
}

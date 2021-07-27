package finalproject.Game;

public enum AudioID
{
	MINE(0),
	BACKGROUND(1),
	ENEMY(2);
	
	
	private int position;
	public static final int buffers = 3;
	public static final String type = ".wav";
	public static final String location = "res/";
	
	
	AudioID(int x)
	{
		position = x;
	}
	
	public int getPosition()
	{
		return position;
	}
}
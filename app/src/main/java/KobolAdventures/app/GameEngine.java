package finalproject;
import finalproject.Hardware.*;
import java.util.*;
import org.lwjgl.openal.AL; 

public class GameEngine 
{
	private Vector<World> Worlds = new Vector<World>();
	
	/**
	 * GameEngine constructor, utilizes hardware devices to handle IO.
	 * @param Graphics The hardware graphics card.
	 * @param Audio The hardware audio card.
	 */
	public GameEngine()
	{
		try
		{
			GraphicsDevice.createWindow();
			GraphicsDevice.initGL();
			GraphicsDevice.startScreen();
			GraphicsDevice.initGL();
			//GraphicsDevice.LoadTextures();
			AL.create();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Links a world to this GameEngine.
	 * @param WorldToAdd The World to link, cannot be repeated.
	 */
	public void addWorld(World WorldToAdd)
	{
		if(Worlds.contains(WorldToAdd) == false)
		{
			Worlds.add(WorldToAdd);
		}
	}
	
	/**
	 * Initializes all worlds currently linked to this GameEngine.
	 */
	public void start()
	{
		for(World w : Worlds)
		{
			w.begin();
		}
	}
	
	/**
	 * Stops all worlds currently linked to his GameEngine.
	 */
	public void stop()
	{
		for(World w : Worlds)
		{
			w.end();
		}
		AL.destroy();
	}
}
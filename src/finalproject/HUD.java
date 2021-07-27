package finalproject;


import org.lwjgl.util.vector.*;

import finalproject.Game.Player;
import finalproject.Game.TextureID;
import finalproject.Hardware.GraphicsDevice;

/**
 * Enables the drawing of the 2D HUD over the 2D world.
 *
 */
public class HUD
{
	private HUD(){}
	
	/**
	 * Draws hearts, crosshair and sun.
	 */
	static void drawHud()
	{
		GraphicsDevice.start2dDrawing();
		{
			//draw hearts
			for(int i = 0; i < Player.getThisPlayer().getHealth(); i++)
				GraphicsDevice.drawImage(15 + 35 * i, 45, 32, 32, TextureID.HEART);
			
			GraphicsDevice.drawImage(GraphicsDevice.SCREEN_WIDTH / 2, GraphicsDevice.SCREEN_HEIGHT / 2, 20, 20, TextureID.CROSSHAIR);
		}	
		GraphicsDevice.stop2dDrawing();
		
		//drawSun
		GraphicsDevice.drawSun(new Vector3f(Player.getThisPlayer().position.x, 20, Player.getThisPlayer().position.z));
	}
}

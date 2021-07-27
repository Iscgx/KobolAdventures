package finalproject;

import finalproject.Game.*;
import finalproject.Hardware.*;
import finalproject.Math.Noise;

import java.util.*;

import org.lwjgl.Sys;
import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class World 
{
	public int side;
	public final float GRAVITY=9.81f;
	private int Size; //Square
	public static final int INITIAL_SIZE = 700;
	public Vector<Entity> Entities = new Vector<Entity>();
	
	public HashMap<Integer,BlockChunk> ChunksMap = new HashMap<Integer,BlockChunk>();
	
	private GameTime WorldGameTime;
	//private CameraController Camera = new CameraController(-INITIAL_SIZE/2, 47, -INITIAL_SIZE/2);
	private CameraController Camera = new CameraController(-300, 5, -300);
	//private CameraController Camera = new CameraController(0, 5, 0);
	public World(GameTime WorldGameTime)
	{
		this.WorldGameTime = WorldGameTime;
		Size = INITIAL_SIZE;
		Noise.genGrad((long)(Math.random() * this.hashCode()));
	}
	boolean fullScreen = true;
	/**
	 * Main world logic (update, draw and input)
	 * @param Graphics
	 * @param Audio
	 */
	public void begin()
	{
		Player.getThisPlayer().setCam(this.Camera);
		side = (int)Math.sqrt(Size);
		BlockChunk[] Chunks = new BlockChunk[Size];
		
		//Background music
		AudioDevice.play(AudioID.BACKGROUND, Player.getThisPlayer().position.x, Player.getThisPlayer().position.y, Player.getThisPlayer().position.z );
		//Creating chunk and creating chunk map
		for(int i = 0; i < side; i++)
			for(int j = 0; j < side; j++)
			{
				Chunks[i*side + j] = new BlockChunk(WorldGameTime, j * 16, -64, i * 16);
				ChunksMap.put(i*side + j, Chunks[i*side + j]);
			}
		
		
		BlockChunk North = null;
		BlockChunk East = null;
		BlockChunk South = null;
		BlockChunk West = null;
		for(int i = 0; i < side; i++)
		{
			for(int j = 0, num = 0, n = 0; j < side; j++)
			{
				num = i*side + j;
				n = num + side;
				if(n > 0 && n < Chunks.length)
					North = Chunks[n];
				else
					North = null;
				n = num + 1;
				if(n > 0 && n < Chunks.length && ((num + 1) % side) != 0)
					East = Chunks[n];
				else
					East = null;
				
				n = num - side;
				if(n >= 0 && n < Chunks.length)
					South = Chunks[n];
				else
					South = null;
				
				n = num - 1;
				if(n >= 0 && n < Chunks.length && num != 0 && num % side != 0)
					West = Chunks[n];
				else
					West = null;
				
				Chunks[num].connect(North, East, South, West);	
				Chunks[num].saveToBuffer();
				Entities.add(Chunks[num]);
			}
		}
		Mouse.setGrabbed(true);
		
		Player.getThisPlayer().assignToWorld(this);
		for(int i=1;i<=100;i++)
			if(Monster.getMonster(i)!=null)
				Monster.getMonster(i).assignToWorld(this);
		

		while(GraphicsDevice.isDone() == false && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) == false && Player.getThisPlayer().health > 0)
		{
			//main draw-update loop
			GraphicsDevice.clearScreen();
			
			//move camera
			updateCamera();
			
			//Graphics.updateCull();
	        //end move camera
			for(Entity ent : Entities)
			{
				ent.update(WorldGameTime);
				if(ent instanceof DrawableEntity)
					((DrawableEntity)ent).draw();
			}
			GraphicsDevice.drawAll();
			HUD.drawHud();
			GraphicsDevice.update();
			//end main loop
		}
		Display.destroy();
		end();
	}

	
	private void updateCamera()
	{
		boolean updateCull = false;
		Camera.time = Sys.getTime();
		Camera.dt = (Camera.time - Camera.lastTime) / 1000.0f;
		Camera.lastTime = Camera.time;
		
		Camera.dx = Mouse.getDX();
		Camera.dy = Mouse.getDY();
		Camera.updateActivity();
		
		Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
		
		if(Camera.dx != 0 || Camera.dy != 0)
			updateCull = true;
		
		Camera.yaw(Camera.dx * Camera.mouseSensitivity);
		Camera.pitch(-(Camera.dy * Camera.mouseSensitivity));
		
		Camera.falling(GRAVITY,Player.getThisPlayer().collisionDirection);
		if (Keyboard.isKeyDown(Keyboard.KEY_E))
        {
			Camera.doubleSpeed(Player.getThisPlayer().collisionDirection);
        }
		
		Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
		
		if (Mouse.isButtonDown(0))
        {
			Camera.primaryAction();
        }
		
		if (Mouse.isButtonDown(1))
        {
			Camera.secondaryAction();
        }
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {
            Camera.jump(GRAVITY,Player.getThisPlayer().collisionDirection);
            updateCull = true;
            Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
        }
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
			if (Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_D))
				Camera.dt/=2;
            Camera.walkForward(Camera.movementSpeed * Camera.dt,Player.getThisPlayer().collisionDirection);
            updateCull = true;
            Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
        }
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_F9))
        {
			GraphicsDevice.setFullScreen(fullScreen);
			if(fullScreen == true)
				fullScreen = false;
			else
				fullScreen = true;
        }
		
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
        	if (Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_D))
				Camera.dt/=2;
        	Camera.walkBackwards(Camera.movementSpeed * Camera.dt,Player.getThisPlayer().collisionDirection);
        	updateCull = true;
        	Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
        	Camera.strafeLeft(Camera.movementSpeed * Camera.dt,Player.getThisPlayer().collisionDirection);
        	updateCull = true;
        	Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
        	Camera.strafeRight(Camera.movementSpeed * Camera.dt,Player.getThisPlayer().collisionDirection);
        	updateCull = true;
        	Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_R))
        {
        	Camera.moveDown(Camera.movementSpeed * Camera.dt,Player.getThisPlayer().collisionDirection);
        	updateCull = true;
        	Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F))
        {
        	Camera.moveUp(Camera.movementSpeed * Camera.dt,Player.getThisPlayer().collisionDirection);
        	updateCull = true;
        	Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
        }
        
		Camera.lookThrough();
		if(updateCull == true)
		{
			GraphicsDevice.updateCull();
			GraphicsDevice.updateLighting();
		}
		Player.getThisPlayer().updatePosition(new Vector3f(Camera.getPosition().x-.4f,Camera.getPosition().y,Camera.getPosition().z-.4f));
	}	
	public void end()
	{
		
	}

	public int getSize()
	{
		return Size;
	}

	public int getNumberOfEntities()
	{
		return Entities.size();
	}

	public int getSide()
	{
		return (int)Math.sqrt(Size);
	}
}

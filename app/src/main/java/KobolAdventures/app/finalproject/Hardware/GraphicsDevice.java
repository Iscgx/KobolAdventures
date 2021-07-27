package finalproject.Hardware;

import java.nio.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import org.lwjgl.util.vector.*;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.*;
import org.newdawn.slick.util.*;
import static org.lwjgl.opengl.GL11.*;
import finalproject.World;
import finalproject.Game.TextureID;
import finalproject.Math.*;

public class GraphicsDevice
{
	private static boolean fullscreen = false;
	private static DisplayMode displayMode;
	private static String windowTitle = "Kobol Adventures";
	private static int fps;
	private static long lastFPS;
	private static Texture textureMap = null; //main texture.
	private static Texture bgTexture = null; //startScreen texture.
	private static Texture startNormalTexture = null; //startScreen texture.
	private static Texture startHoverTexture = null; //startScreen texture.
	private static Texture logoTexture = null; //startScreen texture.
	private static Texture gradientTexture = null; //startScreen texture.
	private static CullInformation Cull = new CullInformation(); //Planes around the player camera.
	//lighting variables.
	private static float[] lightAmbient = { 1f, .77f, 0.1f, .56f };
	private static float[] lightDiffuse = { .78f, .88f, 1f, 1.0f };
	private static float[] lightPosition = {World.INITIAL_SIZE / 2.0f, World.INITIAL_SIZE / 2.0f, World.INITIAL_SIZE / 2.0f, 1.0f }; 
	private static float[] skyColor = {137f / 255f, 177f / 255f, 255f / 255f, 1.0f};
	
	//settings
	public static final boolean isLightingEnabled = true;
	public static final boolean isVBOIDCullingEnabled = false;
	
	//resolution.
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	//drawing 'queue'.
	private static ShortBuffer IdBuffer = BufferUtils.createShortBuffer(World.INITIAL_SIZE + 200);
	private static ShortBuffer FaceBuffer = BufferUtils.createShortBuffer(World.INITIAL_SIZE + 200);

	/**
	 * Loads the  main textureMap and binds it to opengl.
	 */
	public static void LoadTextures()
	{

		try
		{
			textureMap = TextureLoader.getTexture(TextureID.type, ResourceLoader.getResourceAsStream(TextureID.location));
			Color.white.bind();	
			textureMap.bind();
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Loads specific texture.
	 * @param location texture location.
	 * @return loaded texture.
	 */
	public static Texture LoadTextures(String location)
	{
		Texture toReturn = null;
		try
		{
			toReturn = TextureLoader.getTexture(TextureID.type, ResourceLoader.getResourceAsStream(location));
			Color.white.bind();	
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return toReturn;
	}
	
	static
	{
		lastFPS = Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	/**
	 * Changes the current state of the display to fullscreen or not.
	 * @param set fullscreen.
	 */
	public static void setFullScreen(boolean set)
	{
		try
		{
			Display.setFullscreen(set);
		} catch (LWJGLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the display window and initializes it for drawing.
	 * @throws Exception Throws exception if the window cannot be initialized.
	 */
	public static void createWindow() throws Exception
	{
		Display.setFullscreen(fullscreen);
		DisplayMode[] availableModes = Display.getAvailableDisplayModes();
		for(int i = 0; i < availableModes.length; i++)
		{
			if(availableModes[i].getWidth() == SCREEN_WIDTH && availableModes[i].getHeight() == SCREEN_HEIGHT && availableModes[i].getBitsPerPixel() == 32)
			{
				displayMode = availableModes[i];
				break;	
			}
		}
		Display.setDisplayMode(displayMode);
		Display.setTitle(windowTitle);
		Display.create();
	}
	
	/**
	 * Initializes openGL parameters, z-buffer, fog, lighting and perspective.
	 */
	public static void initGL()
	{
		glEnable(GL_TEXTURE_2D); // Enable Texture Mapping
        glShadeModel(GL_FLAT); // Disable Smooth Shading
        glClearColor(skyColor[0], skyColor[1], skyColor[2], skyColor[3]);
        glClearDepth(1.0); // Depth Buffer Setup
        glEnable(GL_DEPTH_TEST); // Enables Depth Testing
        glDepthMask(true);
        glDepthFunc(GL_LESS); // The Type Of Depth Testing To Do
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
        glLoadIdentity(); // Reset The Projection Matrix

        

        // Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(
          45.0f,
          (float) displayMode.getWidth() / (float) displayMode.getHeight(),
          0.1f,
          100.0f);
        glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix
        glLoadIdentity(); // Reset The Projection Matrix
        // Really Nice Perspective Calculations
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        if(isLightingEnabled)
        {
	        FloatBuffer temp = BufferUtils.createFloatBuffer(4);
	        temp.put(lightAmbient).flip();
	        glLight(GL_LIGHT1, GL_AMBIENT, temp);    // Setup The Ambient Light
	        temp.clear();
	        temp.put(lightDiffuse).flip();
	        glLight(GL_LIGHT1, GL_DIFFUSE, temp);    // Setup The Diffuse Light
	        temp.clear();
	        temp.put(lightPosition).flip();
	        glLight(GL_LIGHT1, GL_POSITION,temp);    // Position The Light
	        glEnable(GL_LIGHT1); 
	        glEnable(GL_LIGHTING);
        }
        
        FloatBuffer t = BufferUtils.createFloatBuffer(4);
        t.put(skyColor).flip();
        
        glFogi(GL_FOG_MODE, GL_LINEAR);                  // Fog Mode
        glFog(GL_FOG_COLOR, t);                // Set Fog Color
        glFogf(GL_FOG_DENSITY, 0.35f);                            // How Dense Will The Fog Be
        glHint(GL_FOG_HINT, GL_DONT_CARE);                   // Fog Hint Value
        glFogf(GL_FOG_START, 70.0f);                               // Fog Start Depth
        glFogf(GL_FOG_END, 95.0f);                                 // Fog End Depth
        glEnable(GL_FOG);
        
       // LoadTextures();
        updateCull();
 
       
	}
	
	/**
	 * Returns the light position to its original value.
	 */
	public static void updateLighting()
	{
		if(isLightingEnabled)
		{
			FloatBuffer temp = BufferUtils.createFloatBuffer(4);
			temp.put(lightPosition).flip();
	        glLight(GL_LIGHT1, GL_POSITION,temp);    // Position The Light
		}
	}
	
	/**
	 * Moves to the specified coordinates.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 */
	public static void moveCamera(float x, float y, float z)
	{
		glTranslatef(x, y, z);
	}
	
	/**
	 * Rotate the viewing camera by the specified angle and vector.
	 * @param angle Angle to rotate the camera.
	 * @param x X component of the vector.
	 * @param y Y component of the vector.
	 * @param z Z component of the vector.
	 */
	public static void rotateView(float angle, float x, float y, float z)
	{
		glRotatef(angle, x, y, z);
	}

	/**
	 * Synchronizes frames per second, and updates the display.
	 */
	public static void update()
	{
		updateFPS();
		
		Display.update();
		Display.sync(60);
	}
	
	/**
	 * Reset GL_MODELVIEW_MATRIX, clears the screen and depth buffer.
	 */
	public static void clearScreen()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();		
	}

	/**
	 * Retrieves GL_PROJECTION and GL_MODELVIEW matrices, and creates culling clipping planes.
	 */
	public static void updateCull()
	{
		float[] proj = new float[16];	
		FloatBuffer p = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_PROJECTION_MATRIX, p);
		p.get(proj);

		float[] modelview = new float[16];
		FloatBuffer m = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, m);
		m.get(modelview);

		Cull.fillPlanes(modelview, proj);
	}
	
	/**
	 * Asks if the given coordinate with the given offset is visible by the current camera.
	 * @param x
	 * @param y
	 * @param z
	 * @param distance
	 * @return
	 */
	public static boolean isVisible(float x, float y, float z, float distance)
	{
		return Cull.isVisible(x, y, z, distance);
	}
	
	/**
	 * Calculate current FPS and updates it to the title of the window.
	 */
	public static void updateFPS() {
	    if ((Sys.getTime() * 1000 / Sys.getTimerResolution()) - lastFPS > 1000) {
	        Display.setTitle(windowTitle + " - FPS: " + fps);
		fps = 0;
		lastFPS += 1000;
	    }
	    fps++;
	}

	/**
	 * Condition to terminate the game window.
	 * @return
	 */
	public static boolean isDone()
	{
		return Display.isCloseRequested();
	}
	
	/**
	 * Creates a Vertex Buffer Object in memory and returns that VBO index.
	 * @return VBO index.
	 */
	public static int createVBOID() 
	{
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) 
		{
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
			ARBVertexBufferObject.glGenBuffersARB(buffer);
			return buffer.get(0);
		}
		return -1;
	}

	/**
	 * Deletes specified Vertex Buffer Object by its index.
	 * @param indexID The index of the VBO.
	 */
	public static void deleteVBOID(int indexID)
	{
		GL15.glDeleteBuffers(indexID);
	}
	
	/**
	 * Sets the specified VBO index in the drawing queue.
	 * @param bufferID The VBO index.
	 * @param numberOfFaces The number of faces of that VBO in GL_QUADS.
	 */
	public static void draw(short bufferID, short numberOfFaces)
	{
		IdBuffer.put(bufferID);
		FaceBuffer.put(numberOfFaces);
	}
	
	/**
	 * Draws all Vertex Buffer Objects in the current drawing queue.
	 */
	public static void drawAll()
	{
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		for(int i = 0; i < IdBuffer.position(); i ++)
		{
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, IdBuffer.get(i));
			glVertexPointer(3, GL_FLOAT, 32, 0);
			glTexCoordPointer(2, GL_FLOAT, 32, 12);
			glNormalPointer(GL_FLOAT, 32, 20);
			glDrawArrays(GL_QUADS, 0, FaceBuffer.get(i) * 4);
			ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB,0);
		}
		IdBuffer.clear();
		FaceBuffer.clear();
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
		
	}


	/**
	 * Saves current projection matrix, and multiplies by the orthogonal matrix:
	 * { {1, 0, 0}
	 *   {0, 1, 0}
	 *   {0, 0, 0} }
	 *  Stops fog, z-buffer and lighting.
	 */
	public static void start2dDrawing()
	{
		glDepthMask(false); 
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
	}
	
	/**
	 * Returns the projection matrix to its original value and re-enables fog, lighting and z-buffer.
	 */
	public static void stop2dDrawing()
	{
		glDepthMask(true); 
		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glPopMatrix();

	}

	
	/**
	 * Draws specified textureID on the screen in X, Y space.
	 * @param x X component to draw.
	 * @param y Y component to draw.
	 * @param width width of the image.
	 * @param height height of the image.
	 * @param textureID Texture-cell to draw.
	 */
	public static void drawImage(float x, float y, float width, float height, TextureID textureID)
	{
		Vector2f texture = new Vector2f(textureID.getX() * .125f, textureID.getY() * .125f);	
		glBegin(GL_QUADS);
		{
			glTexCoord2f(texture.x, texture.y + .125f);
			glVertex2f(x, y);
			glTexCoord2f(texture.x + .125f, texture.y + .125f);
			glVertex2f(x + width, y);
			glTexCoord2f(texture.x + .125f, texture.y);
			glVertex2f(x + width, y - height);
			glTexCoord2f(texture.x, texture.y);
			glVertex2f(x, y - height);
		}
		glEnd();
	}
	
	/**
	 * Draws in X, Y space the current binded texture.
	 * @param x X component to draw.
	 * @param y Y component to draw.
	 * @param width width of the image.
	 * @param height height of the image.
	 * @param texture texture coordinates.
	 */
	public static void drawImage(float x, float y, float width, float height, Vector2f texture)
	{
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, texture.y);
			glVertex2f(x, y);
			glTexCoord2f(texture.x, texture.y);
			glVertex2f(x + width, y);
			glTexCoord2f(texture.x, 0);
			glVertex2f(x + width, y - height);
			glTexCoord2f(0, 0);
			glVertex2f(x, y - height);
		}
		glEnd();
	}
	
	/**
	 * Draws sun in X, Y space over a given position.
	 * @param position Position to draw the sun over.
	 */
	public static void drawSun(Vector3f position)
	{
		Vector2f texture = new Vector2f(TextureID.SUN.getX() * .125f, TextureID.SUN.getY() * .125f);	
		
		int fSize = 5;
		glDisable(GL_LIGHTING);
		glDisable(GL_FOG);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(texture.x, texture.y + .125f);
			glVertex3f(position.x - fSize, position.y, position.z + fSize);
			glTexCoord2f(texture.x + .125f, texture.y + .125f);
			glVertex3f(position.x - fSize, position.y, position.z - fSize);
			glTexCoord2f(texture.x + .125f, texture.y);
			glVertex3f(position.x + fSize, position.y, position.z - fSize);
			glTexCoord2f(texture.x, texture.y);
			glVertex3f(position.x + fSize, position.y, position.z + fSize);	
		}
		glEnd();
		glEnable(GL_LIGHTING);
		glEnable(GL_FOG);
	}
	
	/**
	 * Creates start screen and waits for the user to start the game.
	 * @return
	 */
	public static boolean startScreen()
	{
		start2dDrawing();
		{
			bgTexture = LoadTextures("res/bg.png");
			startHoverTexture = LoadTextures("res/startGameHover.png");
			startNormalTexture = LoadTextures("res/startGameNormal.png");
			logoTexture = LoadTextures("res/logo.png");
			gradientTexture  = LoadTextures("res/gradient.png");
			while(isDone() == false)
			{
				bgTexture.bind();
				drawImage(0, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT, new Vector2f(SCREEN_WIDTH / 16.0f, SCREEN_HEIGHT / 16.0f));
				int x = SCREEN_WIDTH / 2 - 204;
				int y = SCREEN_HEIGHT / 2 + 210;
				gradientTexture.bind();
				drawImage(0, SCREEN_HEIGHT / 2, SCREEN_WIDTH * 2, SCREEN_HEIGHT / 2, new Vector2f(1.0f, .9f));
				logoTexture.bind();
				drawImage(SCREEN_WIDTH / 2 - 210, SCREEN_HEIGHT / 2 - 124 + 100, 480, 260, new Vector2f(1.0f, 1.0f));
				if(Mouse.getX() > x + 14 && Mouse.getX() < x + 390 && Mouse.getY() <  y + 127 - 430 && Mouse.getY() >  y - 360)
				{
					startHoverTexture.bind();
					drawImage(x, y, 505, 127, new Vector2f(1.0f, 1.0f));
					if(Mouse.isButtonDown(0))
					{
						bgTexture.release();
						startHoverTexture.release();
						startNormalTexture.release();
						logoTexture.release();
						gradientTexture.release();
						stop2dDrawing();
						LoadTextures();
						return true;
						
					}
				}
				else
				{
					startNormalTexture.bind();
					drawImage(x, y , 505, 127, new Vector2f(1.0f, 1.0f));
				}
				update();
			}
		}
		stop2dDrawing();
		return true;
	}
	
}

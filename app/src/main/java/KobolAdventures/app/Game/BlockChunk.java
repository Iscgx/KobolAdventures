package finalproject.Game;

import java.nio.*;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import finalproject.GameTime;
import finalproject.Game.Blocks.*;
import finalproject.Hardware.GraphicsDevice;
import finalproject.Math.*;

public class BlockChunk extends DrawableEntity
{
	//Chunk size.
	public final static int IJ = 16;
	public final static int K = 64;
	
	//the offset of the viewing planes.
	public final static int offset = 30;
	
	//the offset of the distance to draw.
	public final static int distance = 120;
	
	//Array of all objects in this chunk.
	public Block[] Blocks = new Block[IJ * IJ * K];
	
	//allows the chunk to connect to other chunks, in a x, y, z space.
	public BlockChunk North = null;
	public BlockChunk East = null;
	public BlockChunk South = null;
	public BlockChunk West = null;
	
	private short visibleFaces;
	
	/**
	 * Creates a chunk in a given coordinate.
	 * @param GlobalGameTime
	 * @param x
	 * @param y
	 * @param z
	 */
	public BlockChunk(GameTime GlobalGameTime, int x, int y, int z)
	{
		super(GlobalGameTime);
		position = new Vector3f(x, y, z);
		double res = 64;
		double value;
		
		byte[] tempValues = new byte[IJ * IJ * K];
		
		//Does several Simplex noise functions and sums them to create a pseudo-random natural-looking world.
		for(int i = 0; i < IJ; i++)
		{
			for(int j = 0; j < IJ; j++)
			{
				
				for(int k = 0; k < K; k++)
				{
					if(k < 1)
					{
						tempValues[i*IJ*K + j*K + k] = 1;
					}
					else if(k < 10)
					{
						tempValues[i*IJ*K + j*K + k] = 3;
					}
					else if(k < 20)
					{
						value = (Noise.noise((x + i + 5) / res / 3, (z + j + 5) / res / 3, (y + k) / res) + 1);
						if(value < 1)
							tempValues[i*IJ*K + j*K + k] = 3;
						else
							tempValues[i*IJ*K + j*K + k] = 0;
					}
					if(k < 35)
					{
							value = (Noise.noise((x + i + 21) / res, (z + j - 13) / res, (y + k) / res) + 1);
							if(value < 1)
								tempValues[i*IJ*K + j*K + k] = 3;
					}
				}

			}
		}
		
		for(int i = 0; i < IJ; i++)
		{
			for(int j = 0; j < IJ; j++)
			{
				
				for(int k = 3; k < K; k++)
				{
					
					if(k == 8)
					{
						value = (Noise.noise((x + i - 30) / res * 4.2, (z + j - 30) / res * 4.2, (y + k) / res) + 1);
						if(value > 1f )
							tempValues[i*IJ*K + j*K + k] = 0;
					}
					else if(k == 9)
					{
						value = (Noise.noise((x + i - 30) / res * 4, (z + j - 30) / res * 4, (y + k) / res) + 1);
						if(value > 1f )
							tempValues[i*IJ*K + j*K + k] = 0;
					}
					else if (k < 15)
					{
						value = (Noise.noise((x + i - 78) / res, (z + j + 21) / res, (y + k) / res) + 1);
						if(value > 1f)
							tempValues[i*IJ*K + j*K + k] = 2;
					}
					
//					//torres
//					value = (Noise.noise((x + i + 2.7) / res * 2, (z + j - 1.3454) / res * 2, (y + 3.1416) / res * 6) + 1);
//					if(value > .3f && value < .4f)
//						tempValues[i*IJ*K + j*K + k] = 4;
//					if(k > 20)
//					{
//						
//					}
				}

			}
		}
		
		//Based on the Simples noise, assigns a type of block to that position. 
		for(int i = 0; i < IJ; i++)
		{
			for(int j = 0; j < IJ; j++)
			{
				
				for(int k = 0; k < K; k++)
				{
					switch(tempValues[i*IJ*K + j*K + k])
					{
					case 0:
							Blocks[i*IJ*K + j*K + k] = null; //air
						break;
					case 1:
							Blocks[i*IJ*K + j*K + k] = new AdamantiumBlock();
						break;
					case 2:
							Blocks[i*IJ*K + j*K + k] = new DirtBlock();
							break;
					case 3:
							Blocks[i*IJ*K + j*K + k] = new GrassBlock();
							break;
					case 4:
							Blocks[i*IJ*K + j*K + k] = new MetalBlock();
					}
				}

			}
		}
	}

	

	@Override
	/**
	 * Always returns false, this entity doesn't have a goal.
	 */
	public boolean goalReached()
	{
		return false;
	}

	/**
	 * Updates this chunk, no use now.
	 */
	@Override
	public void update(GameTime GlobalGameTime)
	{
		

	}

	/**
	 * Draw all the cubes in this cube chunk.
	 */
	@Override
	public void draw()
	{
		//calls draw if vbo exists, is in the culling distance and close to the player.
		if(this.bufferID != -1 && (LAlgebra.Distance2D(position.x, position.z, Player.getThisPlayer().position.x, Player.getThisPlayer().position.z)  < distance))
		{
			if(GraphicsDevice.isVisible(this.position.x + IJ / 2, this.position.y + IJ / 2 + IJ * 3, this.position.z + IJ / 2, offset) ||
					GraphicsDevice.isVisible(this.position.x + IJ / 2, this.position.y + IJ / 2 + IJ * 2, this.position.z + IJ / 2, offset) ||
					GraphicsDevice.isVisible(this.position.x + IJ / 2, this.position.y + IJ / 2 + IJ, this.position.z + IJ / 2, offset) ||
					GraphicsDevice.isVisible(this.position.x + IJ / 2, this.position.y + IJ / 2, this.position.z + IJ / 2, offset))
			{
				GraphicsDevice.draw(bufferID, visibleFaces);
			}
		}
	}

	/**
	 * Connects this chunk to its neighbors, null specifies the end of the world.
	 * @param North North chunk.
	 * @param East East chunk.
	 * @param South South chunk.
	 * @param West West chunk.
	 */
	public void connect(BlockChunk North, BlockChunk East, BlockChunk South, BlockChunk West)
	{
		this.North = North;
		this.East = East;
		this.South = South;
		this.West = West;
	}
	
	/**
	 * Runs several algorithms to tell the VBO the number of current visible faces of a block.
	 * @param i cube x.
	 * @param j cube z.
	 * @param k cube y.
	 * @return The number of visible faces.
	 */
	private int getVisibleNumberOfFaces(int i, int j, int k)
	{
		int ux;
		int uy;
		int uz;
		int temp;
		int current;
		ux = i*K*IJ;
		uy = j*K;
		uz = k;
		current = ux + uy + uz;
		int numberOfFaces = 0;
		if(Blocks[current] != null)
		{
			//X axis faces.
			if(j == 0) //first
			{
				temp = ux + (j + 1)*K + uz;
				if(Blocks[temp] == null)
					numberOfFaces++;
				if((South == null || South.Blocks[ux + (IJ - 1)*K + uz] == null))
					numberOfFaces++;				
			}
			else if(j == 15) //last
			{
				temp = ux + (j - 1)*K + uz;
				if(Blocks[temp] == null)
					numberOfFaces++;
				if((North == null || North.Blocks[ux + uz] == null))
					numberOfFaces++;
			}
			else //inside
			{
				temp = ux + (j - 1)*K + uz;
				if(Blocks[temp] == null)
					numberOfFaces++;
				temp = ux + (j + 1)*K + uz;
				if(Blocks[temp] == null)
					numberOfFaces++;
			}
				
			//Y Axis faces
			temp = ux + uy + k + 1;
			if(temp > 0 && temp < Blocks.length)//x top is air
			{
				if(Blocks[temp] == null)
					numberOfFaces++;
			}
			else
				numberOfFaces++;
			
			temp = ux + uy + k - 1;
			if(temp > 0 && temp < Blocks.length)//x bottom is air
			{
				if(Blocks[temp] == null)
					numberOfFaces++;
			}
			else
				numberOfFaces++;
			
			//Z Axis faces.
			
			temp = (i + 1)*K*IJ + uy + uz;
			if(temp > 0 && temp < Blocks.length)//x front is air
			{
				if(Blocks[temp] == null)
					numberOfFaces++;
			}
			else if(East == null || East.Blocks[uy + uz] == null)
				numberOfFaces++;
		
			temp = (i - 1)*K*IJ + uy + uz;
			if(temp > 0 && temp < Blocks.length)//x back is air
			{
				if(Blocks[temp] == null)
					numberOfFaces++;
			}
			else if(West == null || West.Blocks[(IJ - 1)*IJ*K + uy + uz] == null)
				numberOfFaces++;
		}
		return numberOfFaces;
	}
	
	/**
	 * Retrieves the total number of visible faces for this chunk.
	 * @return
	 */
	private int getTotalNumberOfFaces()
	{
		int numberOfFaces = 0;
		for(int i = 0; i < IJ; i++) //draw whole chunk
			for(int j = 0; j < IJ; j++)
				for(int k = 0; k < K; k++)
					numberOfFaces += getVisibleNumberOfFaces(i, j ,k);
		return numberOfFaces;
	}
	
	/**
	 * Saves this chunk state to a new VBO.
	 */
	@Override
	public void saveToBuffer()
	{	
		
		bufferID = (short)GraphicsDevice.createVBOID();
		updateToBuffer();
		
	}
	
	/**
	 * Updates this chunk state to its VBO.
	 */
	public void updateToBuffer()
	{
		visibleFaces = (short)getTotalNumberOfFaces();
		int size = visibleFaces * 48 + visibleFaces * 32 + visibleFaces * 48;
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bufferID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, size, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		ByteBuffer vertexPositionAttributes = ARBVertexBufferObject.glMapBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, ARBVertexBufferObject.GL_WRITE_ONLY_ARB, size, null).order(ByteOrder.nativeOrder());
		fillBuffer(vertexPositionAttributes);
		vertexPositionAttributes.flip();
		ARBVertexBufferObject.glUnmapBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
	}
	
	/**
	 * Fills the VBO buffer.
	 */
	protected void fillBuffer(ByteBuffer VertexBuffer)
	{
		for(int i = 0; i < IJ; i++) //draw whole chunk
			for(int j = 0; j < IJ; j++)
				for(int k = 0; k < K; k++)
					fillFaceBuffer(i, j, k, VertexBuffer);
	}
	
	/**
	 * Locally saves a cube faces to its parent chunk VBO.
	 * @param i cube x.
	 * @param j cube z.
	 * @param k cube y.
	 * @param VertexBuffer
	 */
	private void fillFaceBuffer(int i, int j, int k, ByteBuffer VertexBuffer)
	{
		
		int ux;
		int uy;
		int uz;
		float cubex = position.x + i;
		float cubey = position.y + k;
		float cubez = position.z + j;
		int temp;
		int current;
		ux = i*K*IJ;
		uy = j*K;
		uz = k;
		current = ux + uy + uz;
		if(Blocks[current] != null)
		{
			//X axis faces.
			if(j == 0) //first
			{
				temp = ux + (j + 1)*K + uz;
				if(Blocks[temp] == null)
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.RIGHT, Blocks[current].getTexture());
				}
				if((South == null || South.Blocks[ux + (IJ - 1)*K + uz] == null))
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.LEFT, Blocks[current].getTexture());
				}
					
			}
			else if(j == 15) //last
			{
				temp = ux + (j - 1)*K + uz;
				if(Blocks[temp] == null)
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.LEFT, Blocks[current].getTexture());
				}
				if((North == null || North.Blocks[ux + uz] == null))
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.RIGHT, Blocks[current].getTexture());
				}
			}
			else //inside
			{
				temp = ux + (j - 1)*K + uz;
				if(Blocks[temp] == null)
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.LEFT, Blocks[current].getTexture());
				}
				temp = ux + (j + 1)*K + uz;
				if(Blocks[temp] == null)
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.RIGHT, Blocks[current].getTexture());
				}
			}
				
			//Y Axis faces


			
			temp = ux + uy + k + 1;
			if(temp > 0 && temp < Blocks.length)//x top is air
			{
				if(Blocks[temp] == null)
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.TOP, Blocks[current].getTexture());
				}
			}
			else
			{
				insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.TOP, Blocks[current].getTexture());
				
			}

			
			temp = ux + uy + k - 1;
			if(temp > 0 && temp < Blocks.length)//x bottom is air
			{
				if(Blocks[temp] == null)
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.BOTTOM, Blocks[current].getTexture());
				}
			}
			else
			{
				insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.BOTTOM, Blocks[current].getTexture());
				
			}
		

			//Z Axis faces.
			temp = (i + 1)*K*IJ + uy + uz;
			if(temp > 0 && temp < Blocks.length)//x front is air
			{
				if(Blocks[temp] == null)
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.FRONT, Blocks[current].getTexture());
				}
			}
			else if(East == null || East.Blocks[uy + uz] == null)
			{
				insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.FRONT, Blocks[current].getTexture());
			}
			temp = (i - 1)*K*IJ + uy + uz;
			if(temp > 0 && temp < Blocks.length)//x back is air
			{
				if(Blocks[temp] == null)
				{
					insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.BACK, Blocks[current].getTexture());
				}
			}
			else if(West == null || West.Blocks[(IJ - 1)*IJ*K + uy + uz] == null)
			{
				insertFace(VertexBuffer, cubex, cubey, cubez, BlockFaceID.BACK, Blocks[current].getTexture());
			}
			
		}
		
		
		
	}

	/**
	 * Inserts a cube face to a buffer (vertex coordinates, normal vector direction and texture coordinates).
	 * @param VertexBuffer The buffer to save.
	 * @param cubex cube x.
	 * @param cubey cube y.
	 * @param cubez cube z.
	 * @param face Block Face to save.
	 * @param textureID Texture of that face.
	 */
	private void insertFace(ByteBuffer VertexBuffer, float cubex, float cubey, float cubez, BlockFaceID face, TextureID textureID)
	{
		float fSize = .5f;
		Vector2f texture = new Vector2f(textureID.getX() * .125f, textureID.getY() * .125f);
        switch(face)
        {
        case TOP:
        	
        	//GL11.glNormal3f( 0.0f, 1.0f, 0.0f);

        	// ** Write vertex data 1
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top) 
        	// ** Write texture data 1
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 1
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(1.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 2 
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez - fSize); // Top Left Of The Quad (Top)
        	// ** Write texture data 2
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 2
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(1.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 3
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez + fSize); // Bottom Left Of The Quad (Top)
        	// ** Write texture data 3
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 3
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(1.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 4
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez + fSize); // Bottom Right Of The Quad (Top)
        	// ** Write texture data 4
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 4
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(1.0f); VertexBuffer.putFloat(0.0f);
        	break;
        case BOTTOM:      	
        	if(textureID.isMultiTexture())
        		texture.y += 2  * .125;
        	//GL11.glNormal3f( 0.0f, -1.0f,0f);
        	// ** Write vertex data 1
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez + fSize); // Top Right Of The Quad (Top)  
        	// ** Write texture data 1
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 1
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(-1.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 2
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez + fSize); // Top Left Of The Quad (Top)
        	// ** Write texture data 2
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 2
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(-1.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 3
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez - fSize); // Bottom Left Of The Quad (Top)
        	// ** Write texture data 3
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 3
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(-1.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 4
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez - fSize); // Bottom Right Of The Quad (Top) 
        	// ** Write texture data 4
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 4
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(-1.0f); VertexBuffer.putFloat(0.0f);
        	break;
        case RIGHT: 
        	if(textureID.isMultiTexture())
        		texture.y += 1 * .125;
        	//GL11.glNormal3f(1.0f, 0f, 0.0f);
        	// ** Write vertex data 1
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez + fSize); // Top Right Of The Quad (Top)  
        	// ** Write texture data 1
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 1
        	VertexBuffer.putFloat(1.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 2
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez + fSize); // Top Left Of The Quad (Top)
        	// ** Write texture data 2
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 2
        	VertexBuffer.putFloat(1.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 3
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez + fSize); // Bottom Left Of The Quad (Top)
        	// ** Write texture data 3
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 3
        	VertexBuffer.putFloat(1.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 4
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez + fSize); // Bottom Right Of The Quad (Top)
        	// ** Write texture data 4
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 4
        	VertexBuffer.putFloat(1.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f);
        	break;
        case LEFT: 
        	if(textureID.isMultiTexture())
        		texture.y += 1 * .125;
        	//GL11.glNormal3f(-1.0f, 0f, 0f);
        	// ** Write vertex data 1
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top)
        	// ** Write texture data 1
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 1
        	VertexBuffer.putFloat(-1.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 2
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top) 
        	// ** Write texture data 2
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 2
        	VertexBuffer.putFloat(-1.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 3
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top) 
        	// ** Write texture data 3
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 3
        	VertexBuffer.putFloat(-1.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f);
        	// ** Write vertex data 4
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top) 
        	// ** Write texture data 4
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 4
        	VertexBuffer.putFloat(-1.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f);;
        	break;	
        case BACK:
        	if(textureID.isMultiTexture())
        		texture.y += 1 * .125;
        	//GL11.glNormal3f(0.0f, 0.0f, -1.0f);
        	// ** Write vertex data 1
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez + fSize); // Top Right Of The Quad (Top) 
        	// ** Write texture data 1
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 1
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(-1.0f);
        	// ** Write vertex data 2
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top) 
        	// ** Write texture data 2
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 2
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(-1.0f);
        	// ** Write vertex data 3
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top)
        	// ** Write texture data 3
        	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 3
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(-1.0f);
        	// ** Write vertex data 4
        	VertexBuffer.putFloat(cubex - fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez + fSize); // Top Right Of The Quad (Top) 
        	// ** Write texture data 4
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
        	// ** Write normal data 4
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(-1.0f);
        	break;
        case FRONT: 
        	if(textureID.isMultiTexture())
        		texture.y += 1 * .125;
        	//GL11.glNormal3f(0.0f, 0.0f, 1.0f);
        	// ** Write vertex data 1
        	VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top) 
        	// ** Write texture data 1
        	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
        	// ** Write normal data 1
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(1.0f);
        	// ** Write vertex data 2
            VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey + fSize); VertexBuffer.putFloat(cubez + fSize); // Top Right Of The Quad (Top) 
            // ** Write texture data 2
            VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
            // ** Write normal data 2
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(1.0f);
            // ** Write vertex data 3
            VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez + fSize); // Top Right Of The Quad (Top) 
            // ** Write texture data 3
            VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
            // ** Write normal data 3
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(1.0f);
            // ** Write vertex data 4
            VertexBuffer.putFloat(cubex + fSize); VertexBuffer.putFloat(cubey - fSize); VertexBuffer.putFloat(cubez - fSize); // Top Right Of The Quad (Top) 
            // ** Write texture data 4
            VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
            // ** Write normal data 4
        	VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(0.0f); VertexBuffer.putFloat(1.0f);
        	break;
        default:      	
        }
	}

}

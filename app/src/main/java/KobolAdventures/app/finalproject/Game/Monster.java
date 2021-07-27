package finalproject.Game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import finalproject.GameTime;
import finalproject.Hardware.AudioDevice;
import finalproject.Hardware.GraphicsDevice;
import finalproject.Math.*;

public class Monster extends MovingEntity
{
	private int monsterType;
	private float errorsize = .2f;
	private float da = 2f;
	private float distance = (float) (0.06f*Math.random()+.05f);
	private float angleXZ;
	public static HashMap<Integer, Monster> Monsters = null;
	public static int numberOfMonsters = 0;
	private int monsterID;
	
	public float fallingvelocity = 0.0f;
	private int jumping = 0, jump = 0;
	
	public Monster(GameTime GlobalGameTime, int monsterType) 
	{
		super(GlobalGameTime);
		monsterID = numberOfMonsters;
		numberOfMonsters++;
		addMonster(monsterID, this);
		this.monsterType = monsterType;
		
		if(monsterType==0)
			health = 5;
		else
			health=10;
		
		angleXZ = 0;
		size = new Vector3f(1,3,1);
		position = new Vector3f ((int)(Math.random()*450+30),-10,(int)(Math.random()*450+30));
		bufferSize = 6 * 48 + 6 * 32 + 6 * 48;
		saveToBuffer();
		
	}
	
	@Override
	protected void finalize ()  
	{
		GraphicsDevice.deleteVBOID(bufferID);
	}

	
	static
	{
		Monsters = new HashMap<Integer, Monster>();
	}

	public static void addMonster(int monsterID, Monster MonsterToAdd)
	{
		Monsters.put(monsterID, MonsterToAdd);
	}
	
	public static Monster getMonster(Integer index)
	{
		if(index > 0 && index < Monsters.size())
			return Monsters.get(index);
		else
			return null;
	}
	
	@Override
	public void draw() 
	{
		GraphicsDevice.draw(bufferID, (short) 6);
		
	}

	@Override
	public boolean goalReached() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(GameTime GlobalGameTime) 
	{
		if(dead == false)
		{
			move();
			attack();
			if(position.y < -100)
			{
				Monsters.remove(monsterID);
				dead = true;
			}
		}
		
		if(health<=0)
		{
			dead = true;
			position = new Vector3f(-300,300,-300);
			this.updateToBuffer();
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void updatePosition(Vector3f position) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void attack() 
	{
		// TODO Auto-generated method stub
		Player playerTemp;
		Monster monsterTemp;
		
		for(int i=0;i<Player.numberOfPlayers-1;i++)
		{
			playerTemp = Player.Players.get(i);
			if(Collision.areCollidingComplete(this.position,playerTemp.position,this.size,playerTemp.size)&&playerTemp.shield!=1)
			{
				playerTemp.health -=1;
				playerTemp.shield = 1;
				playerTemp.myCam.fallingvelocity = .5f;
				playerTemp.myCam.timeprotected=5;
				AudioDevice.play(AudioID.ENEMY, Player.getThisPlayer().position.x, Player.getThisPlayer().position.y, Player.getThisPlayer().position.z );
			}
		}
		
		for(int i=0;i<Monster.numberOfMonsters-1;i++)
		{
			monsterTemp = Monster.Monsters.get(i);
			
			if(monsterID!=i&&monsterTemp!=null&&Collision.areColliding(this.position,monsterTemp.position,this.size,monsterTemp.size)&&monsterTemp.shield!=1)
			{
				monsterTemp.health -=1;
				monsterTemp.shield = 1;
				monsterTemp.fallingvelocity = (float) (.5f*Math.random())+.1f;
			}
		}
	}
	
	public float[] distanceNangleNearestPlayer()
	{
		float [] distNangle = new float[2];
		float temp=0;
		float dx, dz;
		Player playerTemp;
		distNangle[0]=500;
		
		for(int i=0;i<Player.numberOfPlayers-1;i++)
		{
			playerTemp = Player.Players.get(i);
			dx=playerTemp.position.x-position.x;
			dz=playerTemp.position.z-position.z;
			temp = (float) Math.sqrt(Math.pow(dx,2)+Math.pow(playerTemp.position.y-position.y,2)+Math.pow(dz,2));
			
			if(temp<distNangle[0])
			{
				distNangle[0]=temp;
				
				if(dz!=0)
				{
					distNangle[1]=(float) Math.toDegrees(Math.atan(dx/dz));
					
					if(dz>0)
						distNangle[1]+=360;
					else
						distNangle[1]+=180;
					
					if(distNangle[1]>=360)
						distNangle[1]-=360;
				}
				else if(dx>=0)
					distNangle[1]=90;
				else if(dx<=0)
					distNangle[1]=270;
				
				distNangle[1]-=angleXZ;
			}
			
		}
		
		return distNangle;
	}
	
	public void updateAngle(float da)
	{
		angleXZ+=da;
	}
	
	public void move()
	{
		Vector3f temp=new Vector3f();
		float[] distNangle;
		distNangle=distanceNangleNearestPlayer();
		
		if(distNangle[0]<30)
		{
			if(distNangle[1]>0&&distNangle[1]<=180)
				updateAngle(Math.abs(da));
			else if(distNangle[1]>180&&distNangle[1]<360)
				updateAngle(-1*Math.abs(da));
			else if(distNangle[1]<0&&distNangle[1]>=-180)
				updateAngle(-1*Math.abs(da));
			else if(distNangle[1]<-180&&distNangle[1]>-360)
				updateAngle(Math.abs(da));
		}
		else
		{
			if(Math.random()*100>99)
			{
				da*=-1;
			}
			
			updateAngle(da*(float)Math.random());
		}
		
		falling(Parent.GRAVITY);
		
		temp.x=position.x;
		temp.y=position.y;
		temp.z=position.z;
		
		if(angleXZ<0)
			angleXZ+=360;
		
		if(angleXZ>=360)
			angleXZ-=360;
		
		if(angleXZ>=0&&angleXZ<=180)
			position.x+=errorsize;
		
		if(angleXZ<=360&&angleXZ>=180)
			position.x-=errorsize;
		
		if(angleXZ>=90&&angleXZ<=270)
			position.z-=errorsize;
		
		if((angleXZ<=90&&angleXZ>=0)||(angleXZ>=270&&angleXZ<=360))
			position.z+=errorsize;

		Collision.collidingWorld(this, this.Parent,this.size);
		
		if((angleXZ>=0&&angleXZ<=180&&collisionDirection[4]==0)||(angleXZ<=360&&angleXZ>=180&&collisionDirection[3]==0))
		{
			if(shield==0)
				temp.x += distance * (float)Math.sin(Math.toRadians(angleXZ));
			else
				temp.x -= distance * (float)Math.sin(Math.toRadians(angleXZ));
		}
		
		if((angleXZ>=90&&angleXZ<=270&&collisionDirection[6]==0)||(((angleXZ<=90&&angleXZ>=0)||(angleXZ>=270&&angleXZ<=360))&&collisionDirection[5]==0))
		{
			if(shield==0)
				temp.z += distance * (float)Math.cos(Math.toRadians(angleXZ));
			else
				temp.z -= distance * (float)Math.cos(Math.toRadians(angleXZ));
		}
		
		if(Collision.collidingWorld(this, this.Parent,this.size))
			jump(Parent.GRAVITY);
		
		position.x=temp.x;
		position.y=temp.y;
		position.z=temp.z;
		
		updateToBuffer();
	}
	
	public void falling(float gravity)
	{
		Vector3f temp=new Vector3f();
		temp.x=position.x;
		temp.y=position.y;
		temp.z=position.z;
		
		if(jump==1||fallingvelocity>0)
		{
			this.position.y+=errorsize;
			Collision.collidingWorld(this, this.Parent,this.size);
		}
		else
		{
			this.position.y-=errorsize*2;
			Collision.collidingWorld(this, this.Parent,this.size);
		}
		
		if(collisionDirection[1]==0)
		{
			if(fallingvelocity>-.4)
				fallingvelocity-=gravity/1000;
			
			if(fallingvelocity<0)
				jump=0;
		}
		else
		{
			jumping=0;
			
			if(jump==0)
			{
				fallingvelocity=0;
				shield=0;
			}
		}
		
		temp.y += fallingvelocity;
		position.x=temp.x;
		position.y=temp.y;
		position.z=temp.z;
		
		return;
	}
	
	public void jump(float gravity)
	{
		Vector3f temp=new Vector3f();
		temp.x=position.x;
		temp.y=position.y;
		temp.z=position.z;
		
		this.position.y+=errorsize*8;
		Collision.collidingWorld(this, this.Parent,this.size);
		
		if(collisionDirection[2]==0)
		{
			jump=1;
			
			if(jumping==0)
			{
				if(fallingvelocity<.2f)
				{
					fallingvelocity+= gravity/300;
				}
				else
				{
					jumping=1;
				}
			}
			else
				jump=0;
		}
		else
		{
			jumping=1;
		}

		temp.x=position.x;
		temp.y=position.y;
		temp.z=position.z;
		
		return;
	}

	@Override
	public void saveToBuffer()
	{
		bufferID = (short)GraphicsDevice.createVBOID();
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bufferID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bufferSize, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		ByteBuffer vertexPositionAttributes = ARBVertexBufferObject.glMapBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, ARBVertexBufferObject.GL_WRITE_ONLY_ARB, bufferSize, null).order(ByteOrder.nativeOrder());
		fillBuffer(vertexPositionAttributes);
		vertexPositionAttributes.flip();
		ARBVertexBufferObject.glUnmapBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
		
	}
	
	@Override
	protected void fillBuffer(ByteBuffer VertexBuffer)
	{
		Vector2f texture;
		//front face
		if(monsterType==0)
			texture = new Vector2f(TextureID.BLUEMONSTER.getX() * .125f, TextureID.BLUEMONSTER.getY() * .125f + .125f);
		else
			texture = new Vector2f(TextureID.ORANGEMONSTER.getX() * .125f, TextureID.ORANGEMONSTER.getY() * .125f + .125f);
		
		Vector3f fSize = new Vector3f(size.x / 2f, size.y / 2f, size.z / 2f);
		
		position.x-=.5f;
		position.z-=.5f;
		
		Vector3f transformedPosition = new Vector3f();
		Vector3f normal = new Vector3f();
		
    	//****
    	//TOP
    	//****
		normal.x = 0.0f; normal.y = 1.0f; normal.z = 0.0f;
		//LAlgebra.rotateByY(normal, angleXZ);
    	//GL11.glNormal3f( 0.0f, 1.0f, 0.0f);
    	// ** Write vertex data 1
		transformedPosition.x = fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
    	VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 1
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 2 
    	transformedPosition.x = -fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 2
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 3
    	transformedPosition.x = -fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 3
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 4
    	transformedPosition.x = fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 4
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	//****
    	//BOTTOM
    	//****:   
    	normal.x = 0.0f; normal.y = -1.0f; normal.z = 0.0f;
		//LAlgebra.rotateByY(normal, angleXZ);
    	//GL11.glNormal3f( 0.0f, -1.0f,0f);
    	// ** Write vertex data 1
    	transformedPosition.x = fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 1
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 2
    	transformedPosition.x = -fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 2
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 3
    	transformedPosition.x = -fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 3
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 4
    	transformedPosition.x = fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z); 
    	// ** Write texture data 4
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	texture.y -= .125f;
    	
    	//****
    	//RIGHT
    	//****
    	normal.x = 1.0f; normal.y = 0.0f; normal.z = 0.0f;
		LAlgebra.rotateByY(normal, angleXZ);
    	//GL11.glNormal3f(1.0f, 0f, 0.0f);
    	// ** Write vertex data 1
    	transformedPosition.x = fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 1
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 2
    	transformedPosition.x = -fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 2
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 3
    	transformedPosition.x = -fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 3
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .250f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 4
    	transformedPosition.x = fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 4
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .250f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	texture.y += .125f;
    	//****
    	//LEFT
    	//****
    	normal.x = -1.0f; normal.y = 0.0f; normal.z = 0.0f;
		LAlgebra.rotateByY(normal, angleXZ);
    	//GL11.glNormal3f(-1.0f, 0f, 0f);
    	// ** Write vertex data 1
    	transformedPosition.x = fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 1
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 2
    	transformedPosition.x = -fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 2
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 3
    	transformedPosition.x = -fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z); 
    	// ** Write texture data 3
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 4
    	transformedPosition.x = fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z); 
    	// ** Write texture data 4
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	
    	
    	//****
    	//BACK
    	//****
    	normal.x = 0.0f; normal.y = 0.0f; normal.z = -1.0f;
		LAlgebra.rotateByY(normal, angleXZ);
    	//GL11.glNormal3f(0.0f, 0.0f, -1.0f);
    	// ** Write vertex data 1
    	transformedPosition.x = -fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 1
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 2
    	transformedPosition.x = -fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 2
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 3
    	transformedPosition.x = -fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 3
    	VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 4
    	transformedPosition.x = -fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z); 
    	// ** Write texture data 4
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	
    	
    	//****
    	//FRONT
    	//****
    	normal.x = 0.0f; normal.y = 0.0f; normal.z = 1.0f;
		LAlgebra.rotateByY(normal, angleXZ);
    	//GL11.glNormal3f(0.0f, 0.0f, 1.0f);
    	// ** Write vertex data 1
    	transformedPosition.x = fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
    	// ** Write texture data 1
    	VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y);
    	VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
    	
    	// ** Write vertex data 2
    	transformedPosition.x = fSize.x; transformedPosition.y = fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z); 
        // ** Write texture data 2
        VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y);
        VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
        
        // ** Write vertex data 3
        transformedPosition.x = fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z);
        // ** Write texture data 3
        VertexBuffer.putFloat(texture.x); VertexBuffer.putFloat(texture.y + .125f);
        VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
        
        // ** Write vertex data 4
        transformedPosition.x = fSize.x; transformedPosition.y = -fSize.y; transformedPosition.z = -fSize.z;
		LAlgebra.rotateByY(transformedPosition, angleXZ);
		LAlgebra.translate(transformedPosition, position);
		VertexBuffer.putFloat(transformedPosition.x); VertexBuffer.putFloat(transformedPosition.y); VertexBuffer.putFloat(transformedPosition.z); 
        // ** Write texture data 4
        VertexBuffer.putFloat(texture.x + .125f); VertexBuffer.putFloat(texture.y + .125f);
        VertexBuffer.putFloat(normal.x); VertexBuffer.putFloat(normal.y); VertexBuffer.putFloat(normal.z);
        
        position.x+=.5f;
		position.z+=.5f;
	}

	@Override
	public void updateToBuffer()
	{
		// TODO Auto-generated method stub
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bufferID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, bufferSize, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		ByteBuffer vertexPositionAttributes = ARBVertexBufferObject.glMapBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, ARBVertexBufferObject.GL_WRITE_ONLY_ARB, bufferSize, null).order(ByteOrder.nativeOrder());
		fillBuffer(vertexPositionAttributes);
		vertexPositionAttributes.flip();
		ARBVertexBufferObject.glUnmapBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
	}



}

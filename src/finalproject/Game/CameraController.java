package finalproject.Game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.*;

import finalproject.Game.Blocks.AdamantiumBlock;
import finalproject.Hardware.AudioDevice;
import finalproject.Math.Collision;
 
public class CameraController
{
	private Vector3f position = null;
	
	public float fallingvelocity = 0.0f;
	private int jumping=0,jump=0;
	private float yaw = 0.0f;
	private float errorsize = .2f;
	private float pitch = 0.0f;
	private float activity=0;
	
	public float dx = 0.0f;
	public float dy = 0.0f;
	public float dt = 0.0f;
	public float lastTime = 0.0f;
	public float time = 0.0f;
	public float timeprotected = 0;
	
	public float mouseSensitivity = 0.05f;
	public float movementSpeed = 10.0f;
	
	public CameraController(float x, float y, float z)
	{
		position = new Vector3f(x, y, z);
	}
	
	public void yaw(float delta)
	{
		yaw += delta;
		
		if(yaw>=360)
			yaw-=360;
		else if(yaw<=0)
			yaw+=360;
	}
	
	public void pitch(float delta)
	{
		if((pitch > 90 && delta < 0) || (pitch < -90 && delta > 0) || (pitch < 91 && pitch > -91))
				pitch += delta;

	}
	
	public void updateActivity()
	{
		if(activity>0)
			activity-=.05;
		
		if(timeprotected>0)
			timeprotected-=.03f;
		
		if(timeprotected<=0)
			Player.getThisPlayer().shield=0;
	}
	
	public void doubleSpeed(int[] colliding)
	{
    	Player.getThisPlayer().position.y-=errorsize;
    	Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
		
		if(colliding[1]==1)
			dt*=2;
		else
			dt+=.2/10;
	}
	
	public void primaryAction()
	{
		if(activity<=0)
		{
			Vector3f tempPosition= new Vector3f(position.x,position.y,position.z);
			Vector3f tempSize= new Vector3f(1.1f,1.1f,1.1f);
			Monster monsterTemp;
			
			for(float i=1.1f;i<6;i+=.1f)
			{
				tempPosition.y = position.y + i * (float)Math.sin(Math.toRadians(pitch))+2.3f;
				tempPosition.x = position.x - i * (float)Math.sin(Math.toRadians(yaw))* (float)Math.cos(Math.toRadians(pitch))-.1f;
				tempPosition.z = position.z + i * (float)Math.cos(Math.toRadians(yaw))* (float)Math.cos(Math.toRadians(pitch))+.3f;
				
				if(Collision.destroyBlock(tempPosition,tempSize,Player.getThisPlayer().Parent))
				{
					activity=1;
					AudioDevice.play(AudioID.MINE, Player.getThisPlayer().position.x, Player.getThisPlayer().position.y, Player.getThisPlayer().position.z );
					return;
				}
				
				for(int j=0;j<Monster.numberOfMonsters-1;j++)
				{
					monsterTemp = Monster.Monsters.get(j);
					
					if(monsterTemp!=null&&Collision.areColliding(new Vector3f(tempPosition.x*-1,tempPosition.y*-1,tempPosition.z*-1),monsterTemp.position,new Vector3f(3,3,3),monsterTemp.size)&&monsterTemp.shield!=1)
					{
						monsterTemp.health -=1;
						monsterTemp.shield = 1;
						monsterTemp.fallingvelocity = (float) (.5f*Math.random())+.1f;
						activity=1;
						AudioDevice.play(AudioID.ENEMY, Player.getThisPlayer().position.x, Player.getThisPlayer().position.y, Player.getThisPlayer().position.z );
						return;
					}
				}
			}
		}
	}
	
	public void secondaryAction()
	{
		if(activity<=0)
		{
			Vector3f tempPosition= new Vector3f(position.x,position.y,position.z);
			Vector3f tempSize= new Vector3f(1.1f,1.1f,1.1f);
			
			for(float i=1.1f;i<6;i+=.1f)
			{
				tempPosition.y = position.y + i * (float)Math.sin(Math.toRadians(pitch))+2.3f;
				tempPosition.x = position.x - i * (float)Math.sin(Math.toRadians(yaw))* (float)Math.cos(Math.toRadians(pitch))-.1f;
				tempPosition.z = position.z + i * (float)Math.cos(Math.toRadians(yaw))* (float)Math.cos(Math.toRadians(pitch))+.3f;
				
				if(Collision.createBlock(tempPosition,tempSize,Player.getThisPlayer().Parent,false))
				{
					i-=.1f;
					tempPosition.y = position.y + i * (float)Math.sin(Math.toRadians(pitch))+2.3f;
					tempPosition.x = position.x - i * (float)Math.sin(Math.toRadians(yaw))* (float)Math.cos(Math.toRadians(pitch))-.1f;
					tempPosition.z = position.z + i * (float)Math.cos(Math.toRadians(yaw))* (float)Math.cos(Math.toRadians(pitch))+.1f;
					Collision.createBlock(tempPosition,tempSize,Player.getThisPlayer().Parent,true);
					activity=1;
		
					return;
				}
			}
		}
	}
	
	public void moveDown(float distance,int[] colliding)
	{
    	Player.getThisPlayer().position.y-=errorsize;
    	Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
    	fallingvelocity=0;
		
		if(colliding[1]==0)
			position.y += distance;
	}
	
	public void moveUp(float distance,int[] colliding)
	{
		Player.getThisPlayer().position.y+=errorsize;
		Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
		fallingvelocity=0;
		
		if(colliding[2]==0)
			position.y -= distance;
	}
	
	//moves the camera forward relative to its current rotation (yaw)
	public void walkForward(float distance,int[] colliding)
	{
		if(yaw>=0&&yaw<=180)
			Player.getThisPlayer().position.x+=errorsize;
		
		if(yaw<=360&&yaw>=180)
			Player.getThisPlayer().position.x-=errorsize;
		
		if(yaw>=90&&yaw<=270)
			Player.getThisPlayer().position.z+=errorsize;
		
		if((yaw<=90&&yaw>=0)||(yaw>=270&&yaw<=360))
			Player.getThisPlayer().position.z-=errorsize;

		Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
		
		if((yaw>=0&&yaw<=180&&colliding[4]==0)||(yaw<=360&&yaw>=180&&colliding[3]==0))
		{
			position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
		}
		
		if((yaw>=90&&yaw<=270&&colliding[6]==0)||(((yaw<=90&&yaw>=0)||(yaw>=270&&yaw<=360))&&colliding[5]==0))
		{
	    	position.z += distance * (float)Math.cos(Math.toRadians(yaw));
		}
	}
	 
	//moves the camera backward relative to its current rotation (yaw)
	public void walkBackwards(float distance,int[] colliding)
	{
		if(yaw>=0&&yaw<=180)
			Player.getThisPlayer().position.x-=errorsize;
		
		if(yaw<=360&&yaw>=180)
			Player.getThisPlayer().position.x+=errorsize;
		
		if(yaw>=90&&yaw<=270)
			Player.getThisPlayer().position.z-=errorsize;
		
		if((yaw<=90&&yaw>=0)||(yaw>=270&&yaw<=360))
			Player.getThisPlayer().position.z+=errorsize;

		Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
		
		if((yaw>=0&&yaw<=180&&colliding[3]==0)||(yaw<=360&&yaw>=180&&colliding[4]==0))
			position.x += distance * (float)Math.sin(Math.toRadians(yaw));
		
		if((yaw>=90&&yaw<=270&&colliding[5]==0)||(((yaw<=90&&yaw>=0)||(yaw>=270&&yaw<=360))&&colliding[6]==0))
			position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
	}
	 
	//strafes the camera left relative to its current rotation (yaw)
	public void strafeLeft(float distance,int[] colliding)
	{
		if(yaw>=0&&yaw<=180)
			Player.getThisPlayer().position.z-=errorsize;
		
		if(yaw<=360&&yaw>=180)
			Player.getThisPlayer().position.z+=errorsize;
		
		if(yaw>=90&&yaw<=270)
			Player.getThisPlayer().position.x+=errorsize;
		
		if((yaw<=90&&yaw>=0)||(yaw>=270&&yaw<=360))
			Player.getThisPlayer().position.x-=errorsize;

		Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
		
		if((yaw>=90&&yaw<=270&&colliding[4]==0)||(((yaw<=90&&yaw>=0)||(yaw>=270&&yaw<=360))&&colliding[3]==0))
			position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
		
		if((yaw>=0&&yaw<=180&&colliding[5]==0)||(yaw<=360&&yaw>=180&&colliding[6]==0))
	    	position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
	}
	 
	//strafes the camera right relative to its current rotation (yaw)
	public void strafeRight(float distance,int[] colliding)
	{
		if(yaw>=0&&yaw<=180)
			Player.getThisPlayer().position.z+=errorsize;
		
		if(yaw<=360&&yaw>=180)
			Player.getThisPlayer().position.z-=errorsize;
		
		if(yaw>=90&&yaw<=270)
			Player.getThisPlayer().position.x-=errorsize;
		
		if((yaw<=90&&yaw>=0)||(yaw>=270&&yaw<=360))
			Player.getThisPlayer().position.x+=errorsize;

		Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
		
		if((yaw>=90&&yaw<=270&&colliding[3]==0)||(((yaw<=90&&yaw>=0)||(yaw>=270&&yaw<=360))&&colliding[4]==0))
	    	position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
		
		if((yaw>=0&&yaw<=180&&colliding[6]==0)||(yaw<=360&&yaw>=180&&colliding[5]==0))
	    	position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
	}
	
	public void lookThrough()
    {
        //roatate the pitch around the X axis
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
    }
	
	public Vector3f getPosition()
	{
		return position;
	}
	
	public void falling(float gravity,int[] colliding)
	{
		if(jump==1||fallingvelocity>0)
		{
			Player.getThisPlayer().position.y+=errorsize*2;
			Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
			
			if(colliding[2]==1)
				fallingvelocity=-.03f;
		}
		else
		{
			Player.getThisPlayer().position.y-=errorsize*2;
			Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
			
		}
		
		if(colliding[1]==0)
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
			}
		}
		
		position.y -= fallingvelocity;
		
		return;
	}
	
	public void jump(float gravity,int[] colliding)
	{
		
		Player.getThisPlayer().position.y+=errorsize*2;
		Collision.collidingWorld(Player.getThisPlayer(), Player.getThisPlayer().Parent,Player.getThisPlayer().size);
		
		if(colliding[2]==0)
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
			fallingvelocity=-.01f;
			jumping=1;
		}

		return;
	}
	
}

package finalproject.Math;
import org.lwjgl.util.vector.Vector3f;

import finalproject.World;
import finalproject.Game.BlockChunk;
import finalproject.Game.MovingEntity;
import finalproject.Game.Blocks.AdamantiumBlock;

public class Collision  
{
	public static void directionCollision(MovingEntity movingEntity, Vector3f PositionObject1,Vector3f PositionObject2,Vector3f SizeObject1,Vector3f SizeObject2 )
	{
		Vector3f Position1Object1=new Vector3f(PositionObject1.x-SizeObject1.x/2,PositionObject1.y-SizeObject1.y/2,PositionObject1.z-SizeObject1.z/2);
		Vector3f Position1Object2=new Vector3f(PositionObject2.x-SizeObject2.x/2,PositionObject2.y-SizeObject2.y/2,PositionObject2.z-SizeObject2.z/2);
		Vector3f Position2Object1=new Vector3f(PositionObject1.x+SizeObject1.x/2,PositionObject1.y+SizeObject1.y/2,PositionObject1.z+SizeObject1.z/2);
		Vector3f Position2Object2=new Vector3f(PositionObject2.x+SizeObject2.x/2,PositionObject2.y+SizeObject2.y/2,PositionObject2.z+SizeObject2.z/2);
		
		if((Position1Object1.y<Position2Object2.y&&Position2Object1.y>Position1Object2.y)&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			movingEntity.collisionDirection[1]=1;
		
		if((Position2Object1.y>Position1Object2.y&&Position1Object1.y<Position2Object2.y)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			movingEntity.collisionDirection[2]=1;
		
		if((Position1Object1.x<Position2Object2.x&&Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			movingEntity.collisionDirection[3]=1;
		
		if((Position2Object1.x>Position1Object2.x&&Position1Object1.x<(Position2Object2.x))&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.y>Position1Object2.y)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			movingEntity.collisionDirection[4]=1;
		
		if((Position1Object1.z<Position2Object2.z&&Position2Object1.z>Position1Object2.z)&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y))
			movingEntity.collisionDirection[5]=1;
		
		if((Position2Object1.z>Position1Object2.z&&Position1Object1.z<(Position2Object2.z))&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y))
			movingEntity.collisionDirection[6]=1;

		return;
	}
	
	public static boolean areColliding(Vector3f Position1Object1,Vector3f Position1Object2,Vector3f SizeObject1,Vector3f SizeObject2 )
	{
		Vector3f Position2Object1=new Vector3f(Position1Object1.x+SizeObject1.x,Position1Object1.y+SizeObject1.y,Position1Object1.z+SizeObject1.z);
		Vector3f Position2Object2=new Vector3f(Position1Object2.x+SizeObject2.x,Position1Object2.y+SizeObject2.y,Position1Object2.z+SizeObject2.z);
		
		if((Position1Object1.y<Position2Object2.y&&Position2Object1.y>Position1Object2.y)&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			return true;
		
		if((Position2Object1.y>Position1Object2.y&&Position1Object1.y<Position2Object2.y)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			return true;
		
		if((Position1Object1.x<Position2Object2.x&&Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			return true;
		
		if((Position2Object1.x>Position1Object2.x&&Position1Object1.x<(Position2Object2.x))&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.y>Position1Object2.y)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			return true;
		
		if((Position1Object1.z<Position2Object2.z&&Position2Object1.z>Position1Object2.z)&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y))
			return true;
		
		if((Position2Object1.z>Position1Object2.z&&Position1Object1.z<(Position2Object2.z))&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y))
			return true;

		return false;
	}
	
	public static boolean areCollidingComplete(Vector3f Position1Object1,Vector3f Position1Object2,Vector3f SizeObject1,Vector3f SizeObject2 )
	{
		Vector3f Position2Object1=new Vector3f(Position1Object1.x+SizeObject1.x,Position1Object1.y+SizeObject1.y,Position1Object1.z+SizeObject1.z);
		Vector3f Position2Object2=new Vector3f(Position1Object2.x+SizeObject2.x,Position1Object2.y+SizeObject2.y,Position1Object2.z+SizeObject2.z);
		
		if((Position1Object1.x<Position2Object2.x&&Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			return true;
		
		if((Position2Object1.x>Position1Object2.x&&Position1Object1.x<(Position2Object2.x))&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.y>Position1Object2.y)&&(Position1Object1.z<Position2Object2.z)&&(Position2Object1.z>Position1Object2.z))
			return true;
		
		if((Position1Object1.z<Position2Object2.z&&Position2Object1.z>Position1Object2.z)&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y))
			return true;
		
		if((Position2Object1.z>Position1Object2.z&&Position1Object1.z<(Position2Object2.z))&&(Position1Object1.x<Position2Object2.x)&&(Position2Object1.x>Position1Object2.x)&&(Position1Object1.y<Position2Object2.y)&&(Position2Object1.y>Position1Object2.y))
			return true;

		return false;
	}
	
	public static boolean collidingWorld(MovingEntity movingEntity, World Mundus, Vector3f Size)
	{
		boolean colliding=false;
		Vector3f temp = new Vector3f();
		
		if((movingEntity.position.x-(int)movingEntity.position.x)>=.5f)
			temp.x=movingEntity.position.x+1;
		else
			temp.x=movingEntity.position.x;
		
		if((movingEntity.position.y-(int)movingEntity.position.y)>=.5f)
			temp.y=movingEntity.position.y+1;
		else
			temp.y=movingEntity.position.y;
		
		if((movingEntity.position.z-(int)movingEntity.position.z)>=.5f)
			temp.z=movingEntity.position.z+1;
		else
			temp.z=movingEntity.position.z;
		
		BlockChunk Space = Mundus.ChunksMap.get(((int)temp.x/BlockChunk.IJ)+((int)temp.z/BlockChunk.IJ)*Mundus.side);
		BlockChunk SpaceTemp = null;
		
		for(int a=0;a<7;a++)
			movingEntity.collisionDirection[a]=0;
		
		if(Space!=null&&temp.x>=0&&temp.z>=0)
		{
		
			int f,c,v,b;
			
			for(int i=0;i<Size.x+2;i++)
			{
				for(int j=0;j<Size.z+2;j++)
				{
					for(int k=0;k<Size.y+2;k++)
					{
						if(!(i==1&&j==1&&k>=1&&k<=Size.y))
						{
							c = ((int)temp.x-(((int)temp.x/BlockChunk.IJ)*BlockChunk.IJ));
							v = ((int)temp.z-(((int)temp.z/BlockChunk.IJ)*BlockChunk.IJ));
							b = BlockChunk.K+(int)temp.y;

							f = ((c+i-1-(int)(Size.x/2))*BlockChunk.IJ*BlockChunk.K)+((v+j-1-(int)(Size.z/2))*BlockChunk.K)+(b+k-1-(int)(Size.y/2));
							
							if(c>=1+(int)(Size.x/2)&&b>=1+(int)(Size.y/2)&&v>=1+(int)(Size.z/2)&&c<=14-(int)(Size.x/2)&&b<=62-(int)(Size.y/2)&&v<=14-(int)(Size.z/2)&&Space!=null&&Space.Blocks[f]!=null)
							{
								directionCollision(movingEntity,movingEntity.position,new Vector3f((int)movingEntity.position.x+i,(int)movingEntity.position.y+k,(int)movingEntity.position.z+j),Size,new Vector3f(1f,1f,1f));
							}
							else
							{
								SpaceTemp=Space;
								
								if(c==0&&i==0)
								{
									SpaceTemp=Space.West;
									c=16;
								}
								else if(c==15&&i==2)
								{
									SpaceTemp=Space.East;
									c=-1;
								}
								
								if(v==0&&j==0)
								{
									SpaceTemp=Space.South;
									v=16;
								}
								else if(v==15&&j==2)
								{
									SpaceTemp=Space.North;
									v=-1;
								}
	
								
								
								if(b<1+(int)(Size.y/2))
									b=1+k+(int)(Size.y/2);
								else if(b>63-(int)(Size.y/2))
									b=63-k-(int)(Size.y/2);
								
								f = ((c+i-1-(int)(Size.x/2))*BlockChunk.IJ*BlockChunk.K)+((v+j-1-(int)(Size.z/2))*BlockChunk.K)+(b+k-1-(int)(Size.y/2));
								if(SpaceTemp!=null&&f>0&&f<16384&&SpaceTemp.Blocks[f]!=null)
								{
									directionCollision(movingEntity,movingEntity.position,new Vector3f((int)movingEntity.position.x+i,(int)movingEntity.position.y+k,(int)movingEntity.position.z+j),Size,new Vector3f(1f,1f,1f));
								}
							}
						}
					}
				}
			}
		}
		
		for(int a=3;a<7;a++)
		{
			if(movingEntity.collisionDirection[a]==1)
				colliding=true;
		}
		
		return colliding;
	}
	
	public static boolean destroyBlock(Vector3f position,Vector3f size, World Mundus)
	{
		Vector3f temp = new Vector3f();
		
		if((position.x-(int)position.x)>=.5f)
			temp.x=position.x+1*-1;
		else
			temp.x=position.x*-1;
		
		if((position.y-(int)position.y)>=.5f)
			temp.y=position.y+1*-1;
		else
			temp.y=position.y*-1;
		
		if((position.z-(int)position.z)>=.5f)
			temp.z=position.z+1*-1;
		else
			temp.z=position.z*-1;
		
		BlockChunk Space = Mundus.ChunksMap.get(((int)temp.x/BlockChunk.IJ)+((int)temp.z/BlockChunk.IJ)*Mundus.side);
	
		if(Space!=null&&temp.x>=0&&temp.z>=0)
		{
			int f,c,v,b;
			
			c = ((int)temp.x-(((int)temp.x/BlockChunk.IJ)*BlockChunk.IJ));
			v = ((int)temp.z-(((int)temp.z/BlockChunk.IJ)*BlockChunk.IJ));
			b = BlockChunk.K-(int)temp.y;
			f = (c*BlockChunk.IJ*BlockChunk.K)+(v*BlockChunk.K)+(b);
							
			if(Space!=null&&f>0&&f<16384&&Space.Blocks[f]!=null)
			{
				if(areCollidingComplete(position,new Vector3f((int)position.x,(int)position.y,(int)position.z),size,new Vector3f(1f,1f,1f)))
				{
					Space.Blocks[f]= null;
					Space.updateToBuffer();
					if(Space.North != null)
						Space.North.updateToBuffer();
					if(Space.South != null)
						Space.South.updateToBuffer();
					if(Space.East != null)
						Space.East.updateToBuffer();
					if(Space.West != null)
						Space.West.updateToBuffer();
					return true;
				}
			}
							
		}
		
		return false;
	}
	
	public static boolean createBlock(Vector3f position,Vector3f size, World Mundus, boolean create)
	{
		Vector3f temp = new Vector3f();

		if(create)
		{
			if((position.x-(int)position.x)>=.5f)
				temp.x=position.x+1*-1;
			else
				temp.x=position.x*-1;
			
			if((position.y-(int)position.y)>=.5f)
				temp.y=position.y+1*-1;
			else
				temp.y=position.y*-1;
			
			if((position.z-(int)position.z)>=.5f)
				temp.z=position.z+1*-1;
			else
				temp.z=position.z*-1;
			
			BlockChunk Space = Mundus.ChunksMap.get(((int)temp.x/BlockChunk.IJ)+((int)temp.z/BlockChunk.IJ)*Mundus.side);
		
			if(Space!=null&&temp.x>=0&&temp.z>=0)
			{
				int f,c,v,b;
				
				c = ((int)temp.x-(((int)temp.x/BlockChunk.IJ)*BlockChunk.IJ));
				v = ((int)temp.z-(((int)temp.z/BlockChunk.IJ)*BlockChunk.IJ));
				b = BlockChunk.K-(int)temp.y;
				f = (c*BlockChunk.IJ*BlockChunk.K)+(v*BlockChunk.K)+(b);
				if(f>0&&f<16384)
					Space.Blocks[f]= new AdamantiumBlock();
				Space.updateToBuffer();			
			}

		}
		else
		{
		
			if((position.x-(int)position.x)>=.5f)
				temp.x=position.x+1*-1;
			else
				temp.x=position.x*-1;
			
			if((position.y-(int)position.y)>=.5f)
				temp.y=position.y+1*-1;
			else
				temp.y=position.y*-1;
			
			if((position.z-(int)position.z)>=.5f)
				temp.z=position.z+1*-1;
			else
				temp.z=position.z*-1;
			
			BlockChunk Space = Mundus.ChunksMap.get(((int)temp.x/BlockChunk.IJ)+((int)temp.z/BlockChunk.IJ)*Mundus.side);
		
			if(Space!=null&&temp.x>=0&&temp.z>=0)
			{
				int f,c,v,b;
				
				c = ((int)temp.x-(((int)temp.x/BlockChunk.IJ)*BlockChunk.IJ));
				v = ((int)temp.z-(((int)temp.z/BlockChunk.IJ)*BlockChunk.IJ));
				b = BlockChunk.K-(int)temp.y;
				f = (c*BlockChunk.IJ*BlockChunk.K)+(v*BlockChunk.K)+(b);
								
				if(Space!=null&&f>0&&f<16384&&Space.Blocks[f]!=null)
				{
					if(areCollidingComplete(position,new Vector3f((int)position.x,(int)position.y,(int)position.z),size,new Vector3f(1f,1f,1f)))
					{
						return true;
					}
				}
								
			}
			
			return false;
		}
		return false;
	}
}

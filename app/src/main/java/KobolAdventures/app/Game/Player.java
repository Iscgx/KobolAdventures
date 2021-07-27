package finalproject.Game;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import finalproject.GameTime;

/**
 * Class for all players in the game, enables the retrieval of the current game player (for future multiplayer).
 *
 */
public class Player extends MovingEntity
{
	private static int thisPlayerNumber = 0;
	public static HashMap<Integer, Player> Players = null;
	public static int numberOfPlayers = 1;
	private static Player ThisPlayer = null;
	public CameraController myCam;
	
	static
	{
		Players = new HashMap<Integer, Player>();
	}

	public static Player addPlayer()
	{
		Player temp = new Player(null);
		Players.put(numberOfPlayers - 1, temp);
		numberOfPlayers++;
		return temp;
	}
	
	public static Player getPlayer(Integer index)
	{
		if(index > 0 && index < Players.size())
			return Players.get(index);
		else
			return null;
	}
	
	public static void setPlayer(Integer index)
	{
		thisPlayerNumber = index;
		ThisPlayer = Players.get(thisPlayerNumber);
	}
	
	public void setCam(CameraController myCam)
	{
		this.myCam = myCam;
	}
	
	public static int getNumberOfPlayers()
	{
		return numberOfPlayers;
	}
	
	public static Player getThisPlayer()
	{
		return ThisPlayer;
	}
	
	private Player(GameTime GlobalGameTime)
	{
		super(GlobalGameTime);
		position = new Vector3f(0,0,0);
		size = new Vector3f(1f,5,1f);
		health=10;
	}

	@Override
	public void draw()
	{
		// TODO Auto-generated method stub
		
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
		
	}

	public void updatePosition(Vector3f position)
	{
		this.position.x = position.x * -1f;
		this.position.y = position.y * -1f;
		this.position.z = position.z * -1f;
	}
	
	public int getPlayerNumber()
	{
		return thisPlayerNumber;
	}

	@Override
	public void saveToBuffer()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateToBuffer()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void fillBuffer(ByteBuffer VertexBuffer)
	{
		// TODO Auto-generated method stub
		
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public void setHealth(int newHealth)
	{
		if(newHealth > 0 && newHealth < 11)
			health = newHealth;
	}

}

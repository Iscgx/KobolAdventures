package finalproject;

import finalproject.Game.Monster;
import finalproject.Game.Player;

public class Main
{
 
	public static void main(String[] args)
	{
		SharedLibraryLoader.load()
		System.out.println("start");
		
		GameTime GlobalGameTime = GameTime.getSystemTime();

		GameEngine Engine = new GameEngine();
		World Mundus = new World(GlobalGameTime);
		
		Player.addPlayer(); //player1
		Player.setPlayer(0);
		for(int i=1;i<=50;i++)
			Monster.addMonster(i,new Monster(GlobalGameTime, (int) (Math.random()*2)));
		Engine.addWorld(Mundus);
		
		Engine.start();
		
		System.out.println("end");
	}

}

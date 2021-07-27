package finalproject;
 
import java.util.Calendar;

/**
 * Not used right now, class to control the timing of updating objects.
 *
 */
public class GameTime
{
	//private double timeSinceLastUpdate;
	private int milliseconds = 0, seconds = 0, minutes = 0, hours = 0;
	
	public GameTime()
	{
		
	}
	
	public GameTime(GameTime GameTimeToClone)
	{
		milliseconds = GameTimeToClone.milliseconds;
		seconds = GameTimeToClone.seconds;
		minutes = GameTimeToClone.minutes;
		hours = GameTimeToClone.hours;
	}
	
	public GameTime(int milliseconds, int seconds, int minutes, int hours)
	{
		set(milliseconds, seconds, minutes, hours);
	}
	
	public void reset() //Reset game timing.
	{
		milliseconds = 0;
		seconds = 0;
		minutes = 0;
		hours = 0;
	}
	
	public void set(int milliseconds, int seconds, int minutes, int hours) //Set game timing.
	{
		this.milliseconds = milliseconds;
		this.seconds = seconds;
		this.minutes = minutes;
		this.hours = hours;
	}
	
	public static GameTime getSystemTime()
	{
		GameTime ToReturn = new GameTime();
		ToReturn.milliseconds = Calendar.MILLISECOND;
		ToReturn.seconds = Calendar.SECOND;
		ToReturn.minutes = Calendar.MINUTE;
		ToReturn.hours = Calendar.HOUR;
		return ToReturn;
		
	}
	
	/**
	 * Calculate the time difference between two GameTime objects.
	 * @param OtherGameTime GameTime object to calculate the difference. 
	 * @return Returns the time difference in milliseconds.
	 */
	public long getTimeDelta(GameTime OtherGameTime)
	{
		long delta = 0;
		delta += Math.abs(OtherGameTime.milliseconds - this.milliseconds);
		delta += Math.abs(OtherGameTime.seconds - this.seconds) * 1000;
		delta += Math.abs(OtherGameTime.minutes - this.minutes) * 60000;
		delta += Math.abs(OtherGameTime.hours - this.hours) * 360000;
		return delta;
	}
	

}

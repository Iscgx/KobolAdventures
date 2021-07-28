package finalproject.Hardware;

import finalproject.Game.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class AudioDevice 
{
	// Maximum emissions we will need, cannot be more than 16
	  static int singleton = 1;
	  public static final int NUM_SOURCES = 3;
	  
	  //Buffers hold sound data
	  private static IntBuffer buffer = null;
	  
	  //Sources are points emitting sound
	  private static IntBuffer source = null;
	  
	  //Position of the source sound 
	  private static FloatBuffer sourcePos = null;
	  
	  // Velocity of the source sound
	  private static FloatBuffer sourceVel = null;
	  
	  // Position of the listener
	  private static FloatBuffer listenerPos = null;
	  
	  // Velocity of the listener
	  private static FloatBuffer listenerVel = null;
	  
	  // Orientation of the listener. First 3 elements are a string attached to the nose 
	  //the second 3 are a string attached to the top of the head
	  private static FloatBuffer listenerOri = null;
	  
	
	static
	{
		buffer = BufferUtils.createIntBuffer(AudioID.buffers);
		source= BufferUtils.createIntBuffer(NUM_SOURCES);
		// *3 because each one stores 3 values (X,Y,Z) 
		sourcePos = BufferUtils.createFloatBuffer(3*NUM_SOURCES);
		sourceVel = BufferUtils.createFloatBuffer(3*NUM_SOURCES);
		//Creates the buffer and  adds the player position
		listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { Player.getThisPlayer().position.x, Player.getThisPlayer().position.y, Player.getThisPlayer().position.z });
		//Cambiar despues estos valores por los de la cabeza
		listenerVel =BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f });
	    // any buffer that has data added, must be flipped to establish its position and limits
	    listenerPos.flip();
	    listenerVel.flip();
	    listenerOri.flip();
	}
	
	 public static int loadALData() 
	  {
	    // Load wav data into a buffers.
	    AL10.alGenBuffers(buffer);

	    if(AL10.alGetError() != AL10.AL_NO_ERROR)
	      return AL10.AL_FALSE;
	    
	    //Gets all the wav files from the folder
	    WaveData waveFile = WaveData.create(AudioID.location + "mine"+AudioID.type);
	    AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
	    waveFile.dispose();
	    
	    waveFile =  WaveData.create(AudioID.location + "fondo"+AudioID.type);
	    AL10.alBufferData(buffer.get(1), waveFile.format, waveFile.data, waveFile.samplerate);
	    waveFile.dispose();

	    waveFile =  WaveData.create(AudioID.location + "enemy"+AudioID.type);
	    AL10.alBufferData(buffer.get(2), waveFile.format, waveFile.data, waveFile.samplerate);
	    waveFile.dispose();

	    // Bind buffers into audio sources.
	    AL10.alGenSources(source);

	    if(AL10.alGetError() != AL10.AL_NO_ERROR)
	      return AL10.AL_FALSE;

	    //We define which music will the source play and some properties like looping the sound
	    AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(AudioID.MINE.getPosition()) );
	    //Music properties 
	    AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f);
	    AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f);
	    //Defines the position and the velocity of this source
	    AL10.alSource (source.get(0), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(0*3));
	    AL10.alSource (source.get(0), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(0*3));
	    AL10.alSourcei(source.get(0), AL10.AL_LOOPING,  AL10.AL_FALSE);
	    
	    AL10.alSourcei(source.get(1), AL10.AL_BUFFER,   buffer.get(AudioID.BACKGROUND.getPosition()));
	    AL10.alSourcef(source.get(1), AL10.AL_PITCH,    1.0f);
	    AL10.alSourcef(source.get(1), AL10.AL_GAIN,     1.0f);
	    AL10.alSource (source.get(1), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(1*3));
	    AL10.alSource (source.get(1), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(1*3));
	    AL10.alSourcei(source.get(1), AL10.AL_LOOPING,  AL10.AL_TRUE  );
	    
	    AL10.alSourcei(source.get(2), AL10.AL_BUFFER,   buffer.get(AudioID.ENEMY.getPosition()));
	    AL10.alSourcef(source.get(2), AL10.AL_PITCH,    1.0f);
	    AL10.alSourcef(source.get(2), AL10.AL_GAIN,     1.0f);
	    AL10.alSource (source.get(2), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(2*3));
	    AL10.alSource (source.get(2), AL10.AL_VELOCITY, (FloatBuffer) sourceVel.position(2*3));
	    AL10.alSourcei(source.get(2), AL10.AL_LOOPING,  AL10.AL_FALSE  );    
	    
	    // Do another error check and return.
	    if(AL10.alGetError() == AL10.AL_NO_ERROR)
	      return AL10.AL_TRUE;

	    return AL10.AL_FALSE;
	  }  
	 
	 public static void setListenerValues()
	  {
	    AL10.alListener(AL10.AL_POSITION,    listenerPos);
	    AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
	    AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	  }  
	
	 public static void killALData() 
	  {
	    AL10.alDeleteSources(source);
	    AL10.alDeleteBuffers(buffer);
	
	  }
	  
	  public static void play(AudioID music,float x, float y, float z ) 
	  {
		//   int actual;
		//   int position = music.getPosition();
	
		//   // Load the wav data.
		//   if(singleton == 1)
		//   {
		//     if(loadALData() == AL10.AL_FALSE) {
		//       System.out.println("Error loading data.");
		//       return;
		//     }
		//     singleton = 2;
		//   }
		//     setListenerValues();
		
		//     actual  = AL10.alGetSourcei(source.get(position), AL10.AL_SOURCE_STATE);
		// 	 if(actual != AL10.AL_PLAYING)
		// 	  {
		// 	    	sourcePos.put(position*3+0,0);
		// 	    	sourcePos.put(position*3+1,0);
		// 	    	sourcePos.put(position*3+2,0);
			    	
		// 	    	AL10.alSource(source.get(position), AL10.AL_POSITION, (FloatBuffer) sourcePos.position(position*3));
		// 	        AL10.alSourcePlay(source.get(position));
		// 	   }	
			
			
	  }

}

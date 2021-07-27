package finalproject.Math;
 
/**
 * Manages culling.
 *
 */
public class CullInformation
{
	public float[] modelView = null;
	public float[] neaClip = null;
	public float[] farClip = null;
	public float[] lftClip = null;
	public float[] rgtClip = null;
	public float[] botClip = null;
	public float[] topClip = null;
	private boolean isFilled = false;
	
	/**
	 * Recieves the current modelview and projection matrices.
	 */
	public CullInformation()
	{
		modelView = new float[16];
		neaClip = new float[4];
		farClip = new float[4];
		lftClip = new float[4];
		rgtClip = new float[4];
		botClip = new float[4];
		topClip = new float[4];
	}
	
	/**
	 * Creates planes in space based on a modelView matrix and a projectionMatrix.
	 * @param modelViewMatrix
	 * @param projectionMatrix
	 */
	public void fillPlanes(float[] modelViewMatrix, float[] projectionMatrix)
	{
		if(projectionMatrix.length == 16 && modelViewMatrix.length == 16)
		{
			
			modelView = modelViewMatrix.clone();
			
			lftClip[0] = projectionMatrix[0] + projectionMatrix[3];
			lftClip[1] = projectionMatrix[4] + projectionMatrix[7];
			lftClip[2] = projectionMatrix[8] + projectionMatrix[11];
			lftClip[3] = projectionMatrix[12] + projectionMatrix[15];
			
			rgtClip[0] = -projectionMatrix[0] + projectionMatrix[3];
			rgtClip[1] = -projectionMatrix[4] + projectionMatrix[7];
			rgtClip[2] = -projectionMatrix[8] + projectionMatrix[11];
			rgtClip[3] = -projectionMatrix[12] + projectionMatrix[15];
			
			botClip[0] = projectionMatrix[1] + projectionMatrix[3];
			botClip[1] = projectionMatrix[5] + projectionMatrix[7];
			botClip[2] = projectionMatrix[9] + projectionMatrix[11];
			botClip[3] = projectionMatrix[13] + projectionMatrix[15];
			
			
			topClip[0] = -projectionMatrix[1] + projectionMatrix[3];
			topClip[1] = -projectionMatrix[5] + projectionMatrix[7];
			topClip[2] = -projectionMatrix[9] + projectionMatrix[11];
			topClip[3] = -projectionMatrix[13] + projectionMatrix[15];
			
			neaClip[0] = projectionMatrix[2] + projectionMatrix[3];
			neaClip[1] = projectionMatrix[6] + projectionMatrix[7];
			neaClip[2] = projectionMatrix[10] + projectionMatrix[11];
			neaClip[3] = projectionMatrix[14] + projectionMatrix[15];
			
			farClip[0] = -projectionMatrix[2] + projectionMatrix[3];
			farClip[1] = -projectionMatrix[6] + projectionMatrix[7];
			farClip[2] = -projectionMatrix[10] + projectionMatrix[11];
			farClip[3] = -projectionMatrix[14] + projectionMatrix[15];
			
			
			
			isFilled = true;
		}
	}

	/**
	 * Checks coordinate visibility considering current projection and modelview matrix.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param offset visibility offset.
	 * @return
	 */
	public boolean isVisible(float x, float y, float z, float offset)
	{
		if(isFilled)
		{
			//transform to eye coordinates
			float eyeX = x * modelView[0] + y * modelView[4] + z * modelView[8] + modelView[12];
			float eyeY = x * modelView[1] + y * modelView[5] + z * modelView[9] + modelView[13];
			float eyeZ = x * modelView[2] + y * modelView[6] + z * modelView[10] + modelView[14];
			
			//apply the plane equations
			if((eyeX * neaClip[0] + eyeY * neaClip[1] + eyeZ * neaClip[2] + neaClip[3] + offset) < 0)
				return false;
			if((eyeX * farClip[0] + eyeY * farClip[1] + eyeZ * farClip[2] + farClip[3] + offset) < 0)
				return false;
			if((eyeX * lftClip[0] + eyeY * lftClip[1] + eyeZ * lftClip[2] + lftClip[3] + offset) < 0)
				return false;
			if((eyeX * rgtClip[0] + eyeY * rgtClip[1] + eyeZ * rgtClip[2] + rgtClip[3] + offset) < 0)
				return false;
			if((eyeX * botClip[0] + eyeY * botClip[1] + eyeZ * botClip[2] + botClip[3] + offset) < 0)
				return false;
			if((eyeX * topClip[0] + eyeY * topClip[1] + eyeZ * topClip[2] + topClip[3] + offset) < 0)
				return false;
			
		}
		return true;
	}
}

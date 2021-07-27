package finalproject.Math;

import static java.lang.Math.*;

import org.lwjgl.util.vector.Vector3f;
 
public class LAlgebra
{
	
	public static float Distance2D(float ax, float ay, float bx, float by)
	{
		return (float)sqrt(pow(ax - bx, 2) +pow(ay - by, 2));
	}
	
	public static float Distance3D(float ax, float ay, float az, float bx, float by, float bz)
	{
		return (float)sqrt(pow(ax - bx, 2) + pow(ay - by, 2) + pow(az - bz, 2));
	}
	
	public static Vector3f unitVector(Vector3f v)
	{
		return new Vector3f(v.x / v.length(), v.y / v.length(), v.z / v.length());
	}
	
	public static void translate(Vector3f dest, Vector3f translation)
	{
		dest.x += translation.x;
		dest.y += translation.y;
		dest.z += translation.z;
	}
	public static void rotateByY(Vector3f v, double degTheta)
	{
		double radTheta = toRadians(degTheta);
		double c = cos(radTheta);
		double s = sin(radTheta);
		double posX = v.z * s + v.x * c;
		double posZ = v.z * c - v.x * s;
		v.x = (float)posX;
		v.z = (float)posZ;
		
	}
	public static Vector3f rotateByAxis(Vector3f v, Vector3f axis, double degTheta)
	{
		double radTheta = toRadians(degTheta);
		double c = cos(radTheta);
		double s = sin(radTheta);
		double t = (1 - c);
		double posX = (t * axis.x * axis.x + c) * v.x + (t * axis.x * axis.y * s * axis.z) * v.y + (t * axis.x * axis.z - s * axis.y) * v.z;
		double posY = (t * axis.x * axis.y - s * axis.z) * v.x + (t * axis.y * axis.y + c) * v.y + (t * axis.y * axis.z + s * axis.x) * v.z;
		double posZ = (t * axis.x * axis.z + s * axis.y) * v.x + (t * axis.y * axis.z - s * axis.x) * v.y + (t * axis.z * axis.z + c) * v.z; 
		
		Vector3f transformedPosition = new Vector3f((float)posX, (float)posY, (float)posZ);
		
		return transformedPosition;
	}
	
}

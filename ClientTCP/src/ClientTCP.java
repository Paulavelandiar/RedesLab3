import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class ClientTCP 
{
	public static void main(String[] args) throws NoSuchAlgorithmException 
	{
		try 
		{
			Scanner scanner = new Scanner(System.in);
			Socket socketCliente = new Socket("localhost", 8080);
			
			DataOutputStream escritor = new DataOutputStream(socketCliente.getOutputStream());
			DataInputStream lector = new DataInputStream(socketCliente.getInputStream());
			
			System.out.println("Solicito conexi�n" + "\n");
			System.out.println("Esperando confirmaci�n..." + "\n");
			
			String confirmacionServidor = lector.readUTF();
			
			if(confirmacionServidor.equals("OK"))
			{
				System.out.println("Recib� confirmaci�n"+ "\n");
				escritor.writeUTF("OK");
				System.out.println("Estoy listo para recibir archivos"+ "\n");
				escritor.writeUTF("OK");
					
				byte[] arrayRecibido = lector.readAllBytes();
				System.out.println("Recib� este archivo: " + arrayRecibido);
				
				System.out.println("Procedo a hacer la prueba de integridad" + "\n");						
				
				MessageDigest md = MessageDigest.getInstance("SHA-256");

			    md.update(arrayRecibido);
			    byte[] digest = md.digest();      
			   
			    StringBuffer hexString = new StringBuffer();
			      
			    for (int i = 0;i<digest.length;i++) 
			    {
			         hexString.append(Integer.toHexString(0xFF & digest[i]));
			    }
			    System.out.println("Resultado Digest: " + hexString.toString());
			    			    
			    //String integridad = lector.readUTF();
			     
			  }
			
		socketCliente.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}
}

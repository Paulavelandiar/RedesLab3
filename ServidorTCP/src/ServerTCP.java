import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ServerTCP 
{
	public static void main(String[] args) throws NoSuchAlgorithmException 
	{
		try 
		{
			Scanner scanner = new Scanner(System.in);
			ServerSocket socketServer = new ServerSocket(8080);
			
			Socket socketServidor = socketServer.accept();
			
			DataOutputStream escritor = new DataOutputStream(socketServidor.getOutputStream());
			DataInputStream lector = new DataInputStream(socketServidor.getInputStream());
			
			System.out.println("He aceptado la conexión" + "\n");
			
			System.out.println("Enviando confirmación..." + "\n");
			escritor.writeUTF("OK");
			
			String mensajeServidor = lector.readUTF();
			
			if(mensajeServidor.equals("OK"))
			{
				String confirmacionArchivos = lector.readUTF();
				
				if(confirmacionArchivos.equals("OK"))
				{
					File archivo = new File("ArchivoEnviar.txt");
					byte[] arrayFile = new byte[(int) archivo.length()];
					
					escritor.write(arrayFile, 0, arrayFile.length);
					
					System.out.println("Envié archivo: " + arrayFile + "\n");
					
					System.out.println("Procedo a hacer la prueba de integridad" + "\n");						
					
					MessageDigest md = MessageDigest.getInstance("SHA-256");

				    md.update(arrayFile);
				    byte[] digest = md.digest();      
				   
				    StringBuffer hexString = new StringBuffer();
				      
				    for (int i = 0;i<digest.length;i++) 
				    {
				         hexString.append(Integer.toHexString(0xFF & digest[i]));
				    }
				    System.out.println("Resultado Digest: " + hexString.toString() + "\n");
				    
				    String prueba = hexString.toString();
				    
				    escritor.writeUTF(prueba);
				   
				}
			}
			
		socketServidor.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
}
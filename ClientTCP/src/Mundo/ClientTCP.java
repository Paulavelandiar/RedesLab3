package Mundo;
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
	Scanner scanner;
	Socket socketCliente;
	DataOutputStream escritor;
	DataInputStream lector; 
	
	byte[] archivo;
	String prueba;
	
	public ClientTCP()
	{
		try 
		{
			socketCliente = new Socket("localhost", 8080);
			scanner = new Scanner(System.in);
			escritor = new DataOutputStream(socketCliente.getOutputStream());
			lector = new DataInputStream(socketCliente.getInputStream());
			
			String confirmacionServidor = lector.readUTF();
			if(confirmacionServidor.equals("OK"))
			{
				
				recibirConfirmacion();
				escritor.writeUTF("OK");
			
				avisarArchivos();
				escritor.writeUTF("OK");
										
				byte[] arrayRecibido = lector.readAllBytes();
				archivo = arrayRecibido;
				avisarRecibido();
				
				avisarInicioPrueba();					
				
				MessageDigest md = MessageDigest.getInstance("SHA-256");

			    md.update(arrayRecibido);
			    byte[] digest = md.digest();      
			   
			    StringBuffer hexString = new StringBuffer();
			      
			    for (int i = 0;i<digest.length;i++) 
			    {
			         hexString.append(Integer.toHexString(0xFF & digest[i]));
			    }
			    prueba = hexString.toString();
			    
			    //-------------------AQUÍ ESTALLA-------------------------
			    //String integridad = lector.readUTF();
			    //--------------------------------------------------------
			     
			}
			
		socketCliente.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}
	
	public String recibirConfirmacion()
	{
		return "Me he conectado con el servidor";
	}
	
	public String avisarArchivos()
	{
		return "He recibido un archivo";
	}
	
	public String avisarRecibido()
	{
		return "He avisado que recibí un archivo: " + archivo;
	}
	
	public String avisarInicioPrueba()
	{
		return "Procedo a hacer prueba de integridad";
	}
	
	public String avisarFinalPrueba()
	{
		return "Hice prueba:" + prueba;
	}
}

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
	String llego;

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

				while(true) {

					byte[] tamano = new byte[lector.readInt()];

					lector.read(tamano, 0, tamano.length);
					archivo = tamano;
					avisarRecibido();

					avisarInicioPrueba();					

					MessageDigest md = MessageDigest.getInstance("SHA-256");

					md.update(tamano);
					byte[] digest = md.digest();      

					StringBuffer hexString = new StringBuffer();

					for (int i = 0;i<digest.length;i++) 
					{
						hexString.append(Integer.toHexString(0xFF & digest[i]));
					}
					prueba = hexString.toString();

					//-------------------AQUÍ ESTALLA-------------------------
					try {
						// 	llego = lector.readUTF();
						byte[] integridad = new byte[lector.read()];


						lector.read(integridad, 0, integridad.length);

						//StringBuffer llegada = new StringBuffer();

						llego = new String(integridad);

						System.out.println(llego);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					//--------------------------------------------------------

					//Comparación

					if(llego.equals(prueba)) 
					{
						escritor.writeUTF("OK");
						break;
					}
					else {
						escritor.writeUTF("ERROR");
					}
				}

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
	
	public String meLlegoEsto()
	{
		return "PRUEBA CORRECTA, resultado: " + llego;
	}
}

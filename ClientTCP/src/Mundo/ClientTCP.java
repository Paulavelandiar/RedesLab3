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
	int archivoSeleccionado = 0;
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
				this.procesarArchivo();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
	}


	private void procesarArchivo()
	{
		try 
		{		
			avisarArchivos();
			escritor.writeUTF("OK");



			while(true) {

				byte[] tamano = new byte[lector.read()];

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
					int len = lector.read();
					byte[] integridad = new byte[len];

					if(len == 0) {
						System.out.println("La longitud a leer es: " + len);
						Thread.currentThread().sleep(500);
						len = lector.read();
					}

					lector.read(integridad);
					llego = new String(integridad);
					escritor.writeUTF("YA");


					//StringBuffer llegada = new StringBuffer();


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
					System.out.println("son iguales: " + llego+" : " + prueba);
					break;
				}
				else {
					escritor.writeUTF("ERROR");
					System.out.println("no son iguales: **" + llego+"** : " + prueba);
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

	public void seleccionarArchivo(int archivo2) {

		this.archivoSeleccionado = archivo2;

		this.procesarArchivo();

	}
}

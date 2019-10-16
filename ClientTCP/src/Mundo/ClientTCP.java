package Mundo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;
import java.security.MessageDigest;
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
	int cont=0;
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

				
				byte[] sizeAr = new byte[8192];
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        
				int read;
		        while((read = lector.read(sizeAr)) != -1){
		            bos.write(sizeAr, 0, read);
		            System.out.println(lector.available());
		            bos.flush();
		            if(lector.available() == 0) {break;}
		        }
		        System.out.println("Aquí");
		        
		        escritor.writeUTF("OK");
		        
				//archivo = sizeAr;
				avisarRecibido();

				avisarInicioPrueba();					

				MessageDigest md = MessageDigest.getInstance("SHA-256");

				md.update(bos.toByteArray());
				byte[] digest = md.digest();      

				StringBuffer hexString = new StringBuffer();

				for (int i = 0;i<digest.length;i++) 
				{
					hexString.append(Integer.toHexString(0xFF & digest[i]).toLowerCase());
				}if(cont==0) {
					prueba=(String)hexString.toString();
					cont++;
				}
				prueba = hexString.toString();
				
				System.out.println("La prueba es: " + prueba);
				
				try {
					
					llego = lector.readUTF();
					System.out.println(llego);
					
					escritor.writeUTF("YA");

					System.out.println(llego);

					System.out.println("la que llega es "+llego);
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
				catch(Exception e)
				{
					e.printStackTrace();
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
		return "He avisado que recibÃ­ un archivo: " + archivo;
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

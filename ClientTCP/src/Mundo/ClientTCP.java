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
					hexString.append(Integer.toHexString(0xFF & digest[i]));
				}
				prueba = hexString.toString();
				
				System.out.println("La prueba es: " + prueba);
				
				try {
					
//					byte[] integridad = new byte[256];
//					
//					ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
//			        
//					int read1;
//			        while((read1 = lector.read(integridad)) != 1){
//			        	System.out.println(read1);
//			            bos2.write(integridad, 0, read1);
//			        }
//					llego = new String(integridad);
					
					llego = lector.readUTF();
					System.out.println(llego);
					
					escritor.writeUTF("YA");

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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
			System.out.println("Elija un archivo");
			System.out.println("1-100MiB");
			System.out.println("2-250MiB");
			int arch = Integer.parseInt(scanner.nextLine());
			int clientes = 0;
			while(true) {

				PrintWriter bf = new PrintWriter(new BufferedWriter(new FileWriter("./data/logs.txt",true)));
				bf.write(new Date() + " \n");
				bf.flush();
				Socket socketServidor = socketServer.accept();
				clientes ++;
				String idCliente = clientes + ":" + (int)(Math.random()*20);
				DataOutputStream escritor = new DataOutputStream(socketServidor.getOutputStream());
				DataInputStream lector = new DataInputStream(socketServidor.getInputStream());

				System.out.println("He aceptado la conexion" + "\n");

				System.out.println("Enviando confirmacion..." + "\n");
				escritor.writeUTF("OK");

				String mensajeServidor = lector.readUTF();

				if(mensajeServidor.equals("OK"))
				{	

					String confirmacionArchivos = lector.readUTF();

					if(confirmacionArchivos.equals("OK"))
					{
						while(true) {
							long tiempoInicial =0;
							long tiempoFinal =0;
							File archivo = null;
							if(arch == 1) {
								archivo = new File("./data/100p.png");
							}
							else {
								archivo = new File("./data/250p.png");
							}

					        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					        
							bf.write("Se va a enviar el archivo "+ archivo.getName() +" de tamano " + byteArrayOutputStream.toByteArray().length + " \n");

							tiempoInicial = System.nanoTime();
							
							
							
							BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archivo));
							
							int in = 0;
							byte[] byteArray = new byte[8192];
							 while ((in = bis.read(byteArray)) != -1){
								 escritor.write(byteArray,0,in);
								 byteArrayOutputStream.write(byteArray,0,in);
							 }
							 
							 bis.close();

							System.out.println(byteArrayOutputStream.toByteArray().length);
							if(lector.readUTF().equals("OK")) {

							System.out.println("Envio archivo: " + byteArrayOutputStream.toByteArray().length + "\n");

							System.out.println("Procedo a hacer la prueba de integridad" + "\n");						
							
							MessageDigest md = MessageDigest.getInstance("SHA-256");
							
							md.update(byteArrayOutputStream.toByteArray());
							byte[] digest = md.digest();      

							StringBuffer hexString = new StringBuffer();

							for (int i = 0;i<digest.length;i++) 
							{
								hexString.append(Integer.toHexString(0xFF & digest[i]));
							}
							System.out.println("Resultado Digest: " + hexString.toString() + "\n");

							String prueba = hexString.toString();
							
							System.out.println("Estoy en el servidor, mando len : **" + prueba.getBytes().length + "***" +" Con arreglo de Bytes :" + prueba.getBytes());
							
							escritor.writeUTF(prueba);

							if(lector.readUTF().equals("YA")){
								tiempoFinal = System.nanoTime();
								System.out.println("Estoy en el servidor, en el YA");
							}

							//Si no es correcto la integridad, se vuelve a enviar el archivo
							String conf = lector.readUTF();
							if(conf.equals("OK")) {

								bf.write("El archivo fue confirmado por parte del cliente "+ idCliente + "\n");

								bf.write("El tiempo que se demoro el archivo en escribirse y llegar fue de " + (tiempoFinal-tiempoInicial)/(1000000) + " segundos "+" \n");

								break;
							}
							else {
								bf.write("El archivo no fue confirmado por parte del cliente "+ idCliente + " \n");

								bf.write("Se procede a volver a enviar el archivo" + " \n");

							}
							}
						}



					}
				}
				bf.flush();
				bf.close();
				socketServidor.close();
			} 
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}


	}
}
package Interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import Mundo.ClientTCP;

public class Interfaz extends JFrame
{
	ClientTCP cliente;
	JTextField textServer1;
	JTextField textServer2;
	JTextField textServer3;
	JTextField textServer4;
	JTextField textServer5;
	JTextField textServer6;
	JTextField textServer7;
	JTextField textServer8;
	JTextField textServer9;
	JTextField textServer10;
	
	
	
	public static void main(String[] args) 
	{
		Interfaz ventanaP = new Interfaz();
	}
	
	public Interfaz()
	{

		cliente = new ClientTCP();
		setTitle ("Cliente");
		setSize (500, 453);
		setResizable(false);
		setDefaultCloseOperation (EXIT_ON_CLOSE);
		setLayout (new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));	
		TitledBorder borde = BorderFactory.createTitledBorder("Datos conexión");
		panel.setBorder(borde);
		
		JLabel labelIP = new JLabel("Conexión a");
		panel.add(labelIP);
		
		JTextField textIP = new JTextField("Localhost");
		textIP.disable();
		panel.add(textIP);
		
		JLabel puerto = new JLabel("Puerto");
		panel.add(puerto);
		
		JTextField textPuerto = new JTextField("8080");
		textPuerto.disable();
		panel.add(textPuerto);
		
		JPanel panelServer = new JPanel();
		panelServer.setLayout(new GridLayout(20,1));	
		TitledBorder bordeServer = BorderFactory.createTitledBorder("Respuesta Servidor");
		panelServer.setBorder(bordeServer);
		
		textServer1 = new JTextField("");
		textServer2 = new JTextField("");
		textServer3 = new JTextField("");
		textServer4 = new JTextField("");
		textServer5 = new JTextField("");
		textServer6 = new JTextField("");
		textServer7 = new JTextField("");
		textServer8 = new JTextField("");
		textServer9 = new JTextField("");
		textServer10 = new JTextField("");
		
		panelServer.add(textServer1);
		panelServer.add(textServer2);
		panelServer.add(textServer3);
		panelServer.add(textServer4);
		panelServer.add(textServer5);
		panelServer.add(textServer6);
		panelServer.add(textServer7);
		panelServer.add(textServer8);
		panelServer.add(textServer9);
		panelServer.add(textServer10);
		
		JPanel panelConectar = new JPanel();
		panelConectar.setLayout(new GridLayout(1,1));	
			
		add(panel, BorderLayout.NORTH);
		add(panelServer, BorderLayout.CENTER);
		add(panelConectar, BorderLayout.SOUTH);
		
		textServer1.setText(cliente.recibirConfirmacion());
		setVisible(true);
		

		continuar();
	}
	
	public void continuar()
	{
//		int archivo = Integer.parseInt(JOptionPane.showInputDialog(this, "Que archivo desea? 1 o 2?", "Seleccionar Archivo", JOptionPane.QUESTION_MESSAGE));	
			
//		cliente.seleccionarArchivo(archivo);
		textServer2.setText(cliente.avisarRecibido());
		textServer3.setText(cliente.avisarArchivos());
		
		int confirmacion1 = JOptionPane.showConfirmDialog(this, "¿Desea hacer la prueba de integridad?");	
		if(confirmacion1 == 0)
		{
			textServer4.setText(cliente.avisarInicioPrueba());
			textServer5.setText(cliente.avisarFinalPrueba());
			textServer6.setText(cliente.meLlegoEsto());
		}
	}
	
		
	

	
	
}

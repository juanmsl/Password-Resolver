<<<<<<< HEAD
package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class ClientGUI extends JFrame {
	
	public static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
	public static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String NUMEROS = "0123456789";
	public static final String ESPECIALES = ",.;:-_!\"#$%&/()=?'\\¡¿ÑñáéíóúÁÉÍÓÚ´";
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private JButton btnConectar;
	private JSpinner portInput;
	private JTextField hostInput;
	private JLabel lblHost;
	private JLabel lblPort;
	private JTextField hashInput;
	private JLabel lblHashADescifrar;
	private JLabel lblCaracteres;
	private JSpinner charsInput;
	private JPanel panel;
	private JSeparator separator;
	private JPanel panel_1;
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	private JCheckBox chMayusculas;
	private JCheckBox chMinusculas;
	private JCheckBox chNumeros;
	private JCheckBox chEspeciales;
	private JButton btnDesconectar;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ClientGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception event1) {
			System.out.println("Error: [" + event1.getMessage() + "]");
		}
		this.socket = null;
		this.out = null;
		this.in = null;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 570, 311);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Color.DARK_GRAY);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[grow]", "[][][]"));
		
		this.panel_1 = new JPanel();
		this.panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Conection to the server", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.contentPane.add(this.panel_1, "cell 0 0,grow");
		this.panel_1.setLayout(new MigLayout("", "[][grow]", "[][][]"));
		
		this.lblHost = new JLabel("Host");
		this.panel_1.add(this.lblHost, "cell 0 0,alignx right");
		
		this.hostInput = new JTextField();
		this.panel_1.add(this.hostInput, "cell 1 0,growx");
		this.hostInput.setText("localhost");
		this.hostInput.setColumns(10);
		
		this.lblPort = new JLabel("Port");
		this.panel_1.add(this.lblPort, "cell 0 1,alignx right");
		
		this.portInput = new JSpinner();
		this.panel_1.add(this.portInput, "cell 1 1,growx");
		this.portInput.setModel(new SpinnerNumberModel(new Integer(3000), new Integer(3000), null, new Integer(1)));
		
		this.horizontalStrut = Box.createHorizontalStrut(90);
		this.panel_1.add(this.horizontalStrut, "cell 0 2");
		
		this.btnConectar = new JButton("Conectar");
		this.panel_1.add(this.btnConectar, "flowx,cell 1 2,growx");
		
		this.btnDesconectar = new JButton("Desconectar");
		this.btnDesconectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ClientGUI.this.socket != null) {
					try {
						ClientGUI.this.socket.close();
						ClientGUI.this.btnConectar.setEnabled(true);
						ClientGUI.this.btnDesconectar.setEnabled(false);
					}
					catch (IOException event) {
						System.out.println("Error: [" + event.getMessage() + "]");
					}
				}
			}
		});
		this.btnDesconectar.setEnabled(false);
		this.panel_1.add(this.btnDesconectar, "cell 1 2,growx");
		this.btnConectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String host = ClientGUI.this.hostInput.getText();
				int port = Integer.parseInt(ClientGUI.this.portInput.getValue().toString());
				ClientGUI.this.initClient(host, port);
			}
		});
		
		this.separator = new JSeparator();
		this.contentPane.add(this.separator, "cell 0 1,growx");
		
		this.panel = new JPanel();
		this.panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Password from any hash", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.contentPane.add(this.panel, "cell 0 2,grow");
		this.panel.setLayout(new MigLayout("", "[][grow]", "[][][][]"));
		
		this.lblHashADescifrar = new JLabel("Hash a descifrar");
		this.panel.add(this.lblHashADescifrar, "cell 0 0,alignx right");
		
		this.hashInput = new JTextField();
		this.panel.add(this.hashInput, "cell 1 0,growx");
		this.hashInput.setColumns(10);
		
		this.lblCaracteres = new JLabel("Caracteres");
		this.panel.add(this.lblCaracteres, "cell 0 1,alignx right");
		
		this.charsInput = new JSpinner();
		this.panel.add(this.charsInput, "cell 1 1,growx");
		this.charsInput.setModel(new SpinnerNumberModel(2, 2, 10, 1));
		
		this.horizontalStrut_1 = Box.createHorizontalStrut(90);
		this.panel.add(this.horizontalStrut_1, "cell 0 2");
		
		this.chMinusculas = new JCheckBox("Minusculas");
		this.chMinusculas.setSelected(true);
		this.panel.add(this.chMinusculas, "flowx,cell 1 2,growx");
		
		this.chMayusculas = new JCheckBox("Mayusculas");
		this.panel.add(this.chMayusculas, "cell 1 2,growx");
		
		JButton btnDescifrar = new JButton("Descrifrar");
		this.panel.add(btnDescifrar, "cell 1 3,growx");
		btnDescifrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ClientGUI.this.socket != null) {
					try {
						String hash = ClientGUI.this.hashInput.getText();
						int characters = (int) ClientGUI.this.charsInput.getValue();
						String dictinonary = "";
						dictinonary += ((ClientGUI.this.chMinusculas.isSelected()) ? ClientGUI.MINUSCULAS : "");
						dictinonary += ((ClientGUI.this.chMayusculas.isSelected()) ? ClientGUI.MAYUSCULAS : "");
						dictinonary += ((ClientGUI.this.chNumeros.isSelected()) ? ClientGUI.NUMEROS : "");
						dictinonary += ((ClientGUI.this.chEspeciales.isSelected()) ? ClientGUI.ESPECIALES : "");
						ClientGUI.this.out.writeUTF(hash);
						ClientGUI.this.out.writeInt(characters);
						ClientGUI.this.out.writeUTF(dictinonary);
						JOptionPane.showMessageDialog(ClientGUI.this, "Hash: " + hash + "\nChars in the password: " + characters + "\nDictionary: " + dictinonary);
						String password = ClientGUI.this.in.readUTF();
						JOptionPane.showMessageDialog(ClientGUI.this, password);
					}
					catch (IOException event) {
						System.out.println("Error: [" + event.getMessage() + "]");
					}
				} else {
					JOptionPane.showMessageDialog(ClientGUI.this, "You must conect first to the server");
				}
			}
		});
		
		this.chNumeros = new JCheckBox("N\u00FAmeros");
		this.panel.add(this.chNumeros, "cell 1 2,growx");
		
		this.chEspeciales = new JCheckBox("Caracteres especiales");
		this.panel.add(this.chEspeciales, "cell 1 2,growx");
	}
	
	private void initClient(String host, int port) {
		try {
			this.socket = new Socket(host, port);
			this.out = new DataOutputStream(ClientGUI.this.socket.getOutputStream());
			this.in = new DataInputStream(this.socket.getInputStream());
			this.btnDesconectar.setEnabled(true);
			this.btnConectar.setEnabled(false);
		}
		catch (IOException event) {
			JOptionPane.showMessageDialog(ClientGUI.this, "No server founded in host " + host + " in the port " + port);
		}
	}
}
=======
package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class ClientGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket;
	private DataOutputStream out;
	private JSpinner number;
	private JButton btnIniciar;
	private JSpinner portInput;
	private JTextField hostInput;
	private JLabel lblHost;
	private JLabel lblPort;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ClientGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception event1) {
			System.out.println("Error: [" + event1.getMessage() + "]");
		}
		this.socket = null;
		this.out = null;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 245, 182);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[grow][grow]", "[][][][][]"));
		
		this.lblHost = new JLabel("Host");
		this.contentPane.add(this.lblHost, "cell 0 0,alignx trailing");
		
		this.hostInput = new JTextField();
		this.hostInput.setText("localhost");
		this.contentPane.add(this.hostInput, "cell 1 0,growx");
		this.hostInput.setColumns(10);
		
		this.btnIniciar = new JButton("Iniciar");
		this.btnIniciar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String host = ClientGUI.this.hostInput.getText();
				int port = Integer.parseInt(ClientGUI.this.portInput.getValue().toString());
				ClientGUI.this.lblHost.setEnabled(false);
				ClientGUI.this.lblPort.setEnabled(false);
				ClientGUI.this.hostInput.setEnabled(false);
				ClientGUI.this.portInput.setEnabled(false);
				ClientGUI.this.btnIniciar.setEnabled(false);
				ClientGUI.this.initClient(host, port);
			}
		});
		
		this.lblPort = new JLabel("Port");
		this.contentPane.add(this.lblPort, "cell 0 1,alignx trailing");
		
		this.portInput = new JSpinner();
		this.portInput.setModel(new SpinnerNumberModel(new Integer(3000), null, null, new Integer(1)));
		this.contentPane.add(this.portInput, "cell 1 1,growx");
		this.contentPane.add(this.btnIniciar, "cell 0 2 2 1,growx");
		
		JButton btnContar = new JButton("Contar");
		btnContar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ClientGUI.this.socket != null) {
					try {
						String linea = ClientGUI.this.number.getValue().toString();
						ClientGUI.this.out.writeUTF(linea);
						System.out.println("Enviado: " + linea);
					}
					catch (IOException event) {
						System.out.println("Error: [" + event.getMessage() + "]");
					}
				}
			}
		});
		
		this.number = new JSpinner();
		this.contentPane.add(this.number, "cell 0 3 2 1,growx");
		this.contentPane.add(btnContar, "cell 0 4 2 1,growx");
	}
	
	private void initClient(String host, int port) {
		try {
			this.socket = new Socket(host, port);
			this.out = new DataOutputStream(ClientGUI.this.socket.getOutputStream());
		}
		catch (UnknownHostException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
		catch (IOException event) {
			System.out.println("Error: [" + event.getMessage() + "]");
		}
	}
}
>>>>>>> origin/master

package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class ClientGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;
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
	private JLabel label;
	
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
		this.objectOutput = null;
		this.objectInput = null;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 570, 330);
		this.contentPane = new JPanel();
		this.contentPane.setBackground(Color.DARK_GRAY);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[grow]", "[][][][]"));
		
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
				ClientGUI.this.desconectar();
			}
		});
		this.btnDesconectar.setEnabled(false);
		this.panel_1.add(this.btnDesconectar, "cell 1 2,growx");
		this.btnConectar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientGUI.this.conectar();
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
		this.hashInput.setText("b221d9dbb083a7f33428d7c2a3c3198ae925614d70210e28716ccaa7cd4ddb79");
		this.panel.add(this.hashInput, "cell 1 0,growx");
		this.hashInput.setColumns(10);
		
		this.lblCaracteres = new JLabel("Caracteres");
		this.panel.add(this.lblCaracteres, "cell 0 1,alignx right");
		
		this.charsInput = new JSpinner();
		this.panel.add(this.charsInput, "cell 1 1,growx");
		this.charsInput.setModel(new SpinnerNumberModel(4, 2, 10, 1));
		
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
				ClientGUI.this.descifrar(btnDescifrar);
			}
		});
		
		this.chNumeros = new JCheckBox("N\u00FAmeros");
		this.panel.add(this.chNumeros, "cell 1 2,growx");
		
		this.chEspeciales = new JCheckBox("Caracteres especiales");
		this.panel.add(this.chEspeciales, "cell 1 2,growx");
		
		this.label = new JLabel("");
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.label.setForeground(Color.WHITE);
		this.contentPane.add(this.label, "cell 0 3,growx");
	}
	
	private void conectar() {
		String host = this.hostInput.getText();
		int port = Integer.parseInt(this.portInput.getValue().toString());
		try {
			this.label.setText("Conecting to the server on host " + host + " in the port " + port);
			this.socket = new Socket(host, port);
			this.label.setText("Getting the stream of input and output of the comunication");
			this.objectOutput = new ObjectOutputStream(this.socket.getOutputStream());
			this.objectInput = new ObjectInputStream(this.socket.getInputStream());
			this.btnDesconectar.setEnabled(true);
			this.btnConectar.setEnabled(false);
			this.label.setText("Communication established");
			JOptionPane.showMessageDialog(this, "Communication established");
		}
		catch (IOException event) {
			this.label.setText("No server founded in host " + host + " in the port " + port);
			JOptionPane.showMessageDialog(ClientGUI.this, "No server founded in host " + host + " in the port " + port);
		}
	}
	
	private void desconectar() {
		if (this.socket != null) {
			try {
				this.socket.close();
				this.btnConectar.setEnabled(true);
				this.btnDesconectar.setEnabled(false);
				this.label.setText("Communication finished");
				JOptionPane.showMessageDialog(this, "Communication finished");
			}
			catch (IOException event) {
				this.label.setText("Error: [" + event.getMessage() + "]");
			}
		}
	}
	
	private DecriptMessage getDecriptMessage() {
		String hash = this.hashInput.getText();
		int characters = (int) this.charsInput.getValue();
		boolean minus = this.chMinusculas.isSelected();
		boolean mayus = this.chMayusculas.isSelected();
		boolean number = this.chNumeros.isSelected();
		boolean special = this.chEspeciales.isSelected();
		DecriptMessage message = new DecriptMessage(hash, characters, minus, mayus, number, special);
		return message;
	}
	
	private void descifrar(JButton btnDescifrar) {
		if (this.socket != null) {
			try {
				DecriptMessage message = this.getDecriptMessage();
				this.label.setText("Confirm the data");
				int option = JOptionPane.showConfirmDialog(this, message);
				if (option == 0) {
					btnDescifrar.setEnabled(false);
					this.btnDesconectar.setEnabled(false);
					this.objectOutput.writeObject(message);
					this.label.setText("Enviado");
					int confirmation = (int) this.objectInput.readObject();
					String confirmationMessage = (String) this.objectInput.readObject();
					this.label.setText(confirmationMessage);
					JOptionPane.showMessageDialog(this, confirmationMessage);
					if (confirmation == 0) {
						String password = (String) this.objectInput.readObject();
						this.label.setText(password);
						JOptionPane.showMessageDialog(this, password);
					}
					btnDescifrar.setEnabled(true);
					this.btnDesconectar.setEnabled(true);
				} else {
					this.label.setText("Cancelado");
				}
			}
			catch (IOException event) {
				this.label.setText("Error: [" + event.getMessage() + "]");
			}
			catch (ClassNotFoundException event) {
				this.label.setText("Error: [" + event.getMessage() + "]");
			}
		} else {
			this.label.setText("You must conect first to the server");
			JOptionPane.showMessageDialog(this, "You must conect first to the server");
		}
	}
}

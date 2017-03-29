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

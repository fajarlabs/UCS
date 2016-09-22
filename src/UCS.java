import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.BoxLayout;

import java.awt.FlowLayout;

public class UCS {

	private JFrame frmUnitTesting;
	private JTextField ipTF;
	private JTextField portTF;
	private JTextArea wacomTA;
	private JPanel panelImage;
	private JLabel labelTest;
	private JTextArea biomorfTA;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UCS window = new UCS();
					window.frmUnitTesting.setVisible(true);
					window.frmUnitTesting.setExtendedState(JFrame.MAXIMIZED_BOTH); 
					//window.frmUnitTesting.setUndecorated(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UCS() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmUnitTesting = new JFrame();
		frmUnitTesting.setTitle("Unit Control Simulation (Client)");
		frmUnitTesting.setBounds(100, 100, 1366, 768);
		frmUnitTesting.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUnitTesting.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/pp_100x100px.jpg"));
		frmUnitTesting.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(UIManager.getColor("Button.background"));
		frmUnitTesting.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 61, 709, 141);
		panel_1.setBorder(new TitledBorder(null, "Wacom Signature Test",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBackground(UIManager.getColor("Button.background"));
		mainPanel.add(panel_1);
		panel_1.setLayout(null);

		JButton BtnSign = new JButton("Sign");
		BtnSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				request(ipTF.getText(), Integer.parseInt(portTF.getText()),
						"SIGNATURE_PAD_WACOM", "SIGN", "");
			}
		});
		BtnSign.setFont(new Font("Dialog", Font.PLAIN, 11));
		BtnSign.setBounds(10, 21, 119, 23);
		panel_1.add(BtnSign);

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				request(ipTF.getText(), Integer.parseInt(portTF.getText()),
						"SIGNATURE_PAD_WACOM", "CLEAR", "");
			}
		});
		btnClear.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnClear.setBounds(10, 49, 119, 23);
		panel_1.add(btnClear);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				request(ipTF.getText(), Integer.parseInt(portTF.getText()),
						"SIGNATURE_PAD_WACOM", "CANCEL", "");
			}
		});
		btnCancel.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnCancel.setBounds(10, 78, 119, 23);
		panel_1.add(btnCancel);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = request(ipTF.getText(),
						Integer.parseInt(portTF.getText()),
						"SIGNATURE_PAD_WACOM", "OK", "");
				wacomTA.setText(result);
				BufferedImage scaledImage = null;
				try {
					JSONObject jsonImage = new JSONObject(wacomTA.getText());
					try {
						byte[] bytes = DatatypeConverter
								.parseBase64Binary(jsonImage.getString("result"));
						ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
						scaledImage = ImageIO.read(bin);
						Image dimg = scaledImage.getScaledInstance(panelImage.getWidth(), panelImage.getHeight(),
						        Image.SCALE_SMOOTH);
						ImageIcon pic = new ImageIcon(dimg);
						panelImage.removeAll();
						panelImage.add(new JLabel(pic));
					} catch (IOException ex) {
						// handle exception...
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnOk.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnOk.setBounds(10, 107, 119, 23);
		panel_1.add(btnOk);

		wacomTA = new JTextArea();
		wacomTA.setLineWrap(true);
		wacomTA.setBounds(139, 21, 388, 109);
		panel_1.add(wacomTA);
		
		panelImage = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelImage.getLayout();
		panelImage.setBackground(UIManager.getColor("Button.shadow"));
		panelImage.setBounds(537, 21, 162, 109);
		panel_1.add(panelImage);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 11, 594, 49);
		panel_2.setBackground(UIManager.getColor("Button.background"));
		panel_2.setBorder(new TitledBorder(null, "Connection",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mainPanel.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(10, 24, 23, 14);
		panel_2.add(lblIp);

		ipTF = new JTextField();
		ipTF.setText("127.0.0.1");
		ipTF.setBounds(32, 21, 193, 20);
		panel_2.add(ipTF);
		ipTF.setColumns(10);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(258, 24, 46, 14);
		panel_2.add(lblPort);

		portTF = new JTextField();
		portTF.setText("13000");
		portTF.setBounds(288, 21, 86, 20);
		panel_2.add(portTF);
		portTF.setColumns(10);
		
		JButton btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				request(ipTF.getText(), Integer.parseInt(portTF.getText()),"","","");
			}
		});
		btnTest.setBounds(392, 20, 91, 23);
		panel_2.add(btnTest);
		
		labelTest = new JLabel("");
		labelTest.setBounds(498, 24, 81, 14);
		panel_2.add(labelTest);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 213, 709, 192);
		panel_3.setBorder(new TitledBorder(null, "Biomorf E-KTP Test",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mainPanel.add(panel_3);
		panel_3.setLayout(null);

		JButton btnConnectAuth = new JButton("Connect & Auth");
		btnConnectAuth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = request(ipTF.getText(), Integer.parseInt(portTF.getText()),"EKTP_BIOMORF", "CONNECT_AND_AUTH", "");
				biomorfTA.append(result+"\n\n");
			}
		});
		btnConnectAuth.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnConnectAuth.setBounds(10, 23, 116, 23);
		panel_3.add(btnConnectAuth);

		JButton btnReadCard = new JButton("Read Card");
		btnReadCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = request(ipTF.getText(), Integer.parseInt(portTF.getText()),"EKTP_BIOMORF", "READ_CARD", "");
				biomorfTA.append(result+"\n\n");
			}
		});
		btnReadCard.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnReadCard.setBounds(10, 54, 116, 23);
		panel_3.add(btnReadCard);

		JButton btnVerify = new JButton("Verify");
		btnVerify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = request(ipTF.getText(), Integer.parseInt(portTF.getText()),"EKTP_BIOMORF", "VERIFY", "");
				biomorfTA.append(result+"\n\n");
			}
		});
		btnVerify.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnVerify.setBounds(10, 88, 116, 23);
		panel_3.add(btnVerify);

		JButton btnNewButton = new JButton("Get Data");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = request(ipTF.getText(), Integer.parseInt(portTF.getText()),"EKTP_BIOMORF", "GET_DATA", "");
				biomorfTA.append(result+"\n\n");
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnNewButton.setBounds(10, 120, 116, 23);
		panel_3.add(btnNewButton);

		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				request(ipTF.getText(), Integer.parseInt(portTF.getText()),"EKTP_BIOMORF", "DISCONNECT", "");
			}
		});
		btnDisconnect.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDisconnect.setBounds(10, 154, 116, 23);
		panel_3.add(btnDisconnect);

		biomorfTA = new JTextArea();
		biomorfTA.setLineWrap(true);
		biomorfTA.setBounds(136, 23, 563, 154);
		panel_3.add(biomorfTA);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Canon Camera 1200D", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 416, 709, 172);
		mainPanel.add(panel);
		panel.setLayout(null);
		
		JButton btnCameraConnect = new JButton("Connect");
		btnCameraConnect.setBounds(10, 21, 115, 23);
		panel.add(btnCameraConnect);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(135, 21, 392, 140);
		panel.add(textArea);
		
		JButton btnCameraCapture = new JButton("Capture");
		btnCameraCapture.setBounds(10, 50, 115, 23);
		panel.add(btnCameraCapture);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(UIManager.getColor("Button.shadow"));
		panel_4.setBounds(537, 21, 162, 140);
		panel.add(panel_4);
		
		JButton btnCameraDisconnect = new JButton("Disconnect");
		btnCameraDisconnect.setBounds(10, 79, 115, 23);
		panel.add(btnCameraDisconnect);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Scriptel Signature", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(729, 61, 619, 141);
		mainPanel.add(panel_5);
		panel_5.setLayout(null);
		
		JButton btnSignScriptel = new JButton("Sign");
		btnSignScriptel.setBounds(10, 21, 112, 23);
		panel_5.add(btnSignScriptel);
		
		JButton btnClearScriptel = new JButton("Clear");
		btnClearScriptel.setBounds(10, 49, 112, 23);
		panel_5.add(btnClearScriptel);
		
		JButton btnCancelScriptel = new JButton("Cancel");
		btnCancelScriptel.setBounds(10, 78, 112, 23);
		panel_5.add(btnCancelScriptel);
		
		JButton btnOk_1 = new JButton("Ok");
		btnOk_1.setBounds(10, 107, 112, 23);
		panel_5.add(btnOk_1);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(132, 21, 329, 109);
		panel_5.add(textArea_1);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(UIManager.getColor("Button.shadow"));
		panel_6.setBounds(471, 21, 138, 109);
		panel_5.add(panel_6);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "Biomorf Fingerprint", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBounds(729, 213, 619, 375);
		mainPanel.add(panel_7);
		panel_7.setLayout(null);
	
	}

	public String request(String serverName, int port, String request,
			String action, String value) {
		String result = "";
		try {

			/* Instansiasi kelas Socket */
			Socket client = new Socket(serverName, port);

			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			JSONObject sendJSON = new JSONObject();
			try {
				sendJSON.put("request", request);
				sendJSON.put("action", action);
				sendJSON.put("value", value);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			out.writeUTF(sendJSON.toString());

			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			result = in.readUTF();
			
			/* Tutup socket */
			client.close();
			
			labelTest.setForeground(Color.GREEN);
			labelTest.setText("Success!");

		} catch (IOException e) {
			e.printStackTrace();
			labelTest.setForeground(Color.RED);
			labelTest.setText("Failed!");
		}

		return result;
	}
}

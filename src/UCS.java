import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

public class UCS {

	private JFrame frmUnitTesting;
	private JTextField ipTF;
	private JTextField portTF;
	private JTextArea wacomTA;
	private JPanel panelImage;
	private JLabel labelTest;
	private JTextArea biomorfTA;
	private JTextField tF_fileOutput;
	private JPanel p_ImageOutput;

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

		JButton BtnSign = new JButton("Connect");
		BtnSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSONObject oJsonData = new JSONObject();
				try {
					oJsonData.put("name", "Fajar Rizki");
					oJsonData.put("nik", "129800000343000");
					oJsonData.put("operator", "Administrator");
				} catch (JSONException e1) {
					System.err.println(e1.getMessage());
				}
				String result = request(ipTF.getText(), Integer.parseInt(portTF.getText()),"SIGNATURE_PAD_WACOM", "SIGN", oJsonData.toString());
				try {
					JSONObject parse = new JSONObject(result);
					String img = parse.getString("result");
					panelImage.removeAll();
					try {
						byte[] btDataFile = Base64.decodeBase64(img);
						BufferedImage image = ImageIO.read(new ByteArrayInputStream(btDataFile));
						ImageIcon i = new ImageIcon(image);
						JLabel il = new JLabel();
						il.setIcon(i);
						il.setAlignmentX(JLabel.CENTER_ALIGNMENT);
						panelImage.add(il);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (JSONException e) {
					System.err.println(e.getMessage());
				}
				wacomTA.setText("");
				wacomTA.setText(result);
			}
		});
		BtnSign.setFont(new Font("Dialog", Font.PLAIN, 11));
		BtnSign.setBounds(10, 21, 119, 23);
		panel_1.add(BtnSign);

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
		panel_2.setBorder(new TitledBorder(null, "Connection",TitledBorder.LEADING, TitledBorder.TOP, null, null));
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

		JButton btnSignatureWacom = new JButton("Connect");
		
		btnSignatureWacom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnSignatureWacom.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnSignatureWacom.setBounds(10, 23, 116, 23);
		panel_3.add(btnSignatureWacom);

		biomorfTA = new JTextArea();
		biomorfTA.setLineWrap(true);
		biomorfTA.setBounds(136, 23, 563, 154);
		panel_3.add(biomorfTA);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Canon Camera 1200D", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 416, 709, 314);
		mainPanel.add(panel);
		panel.setLayout(null);
		
		JTextArea tA_OutputConsole = new JTextArea();
		tA_OutputConsole.setBounds(135, 50, 253, 253);
		panel.add(tA_OutputConsole);
		
		JButton btnCameraCapture = new JButton("Capture");
		btnCameraCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String results = request(ipTF.getText(), Integer.parseInt(portTF.getText()),"CANON_1200D", "CAPTURE", "");
				tA_OutputConsole.setText(results);
				JSONObject json = null;
				try {
					p_ImageOutput.removeAll();
					json = new JSONObject(results);
					String result = json.getString("result");
					BufferedImage myPicture = ImageIO.read(new File(result));
					Image newimg = myPicture.getScaledInstance(p_ImageOutput.getWidth(), p_ImageOutput.getHeight(),  java.awt.Image.SCALE_SMOOTH);
					p_ImageOutput.add(new JLabel(new ImageIcon(newimg)));
					tF_fileOutput.setText(result);
				} catch (JSONException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCameraCapture.setBounds(10, 21, 115, 23);
		panel.add(btnCameraCapture);
		
		tF_fileOutput = new JTextField();
		tF_fileOutput.setBounds(243, 22, 456, 20);
		panel.add(tF_fileOutput);
		tF_fileOutput.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("File Path Output");
		lblNewLabel.setBounds(135, 25, 98, 14);
		panel.add(lblNewLabel);
		
		p_ImageOutput = new JPanel();
		p_ImageOutput.setBackground(Color.WHITE);
		p_ImageOutput.setBounds(446, 50, 253, 253);
		panel.add(p_ImageOutput);
		
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
